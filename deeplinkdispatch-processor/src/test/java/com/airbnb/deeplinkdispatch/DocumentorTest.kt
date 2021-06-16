package com.airbnb.deeplinkdispatch;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class DocumentorTest {
  private static final String FILE_PATH = System.getProperty("user.dir") + "/doc/deeplinks.txt";
  @Mock private ProcessingEnvironment processingEnv;
  @Mock private Messager messager;
  @Mock private Map<String, String> options;
  @Mock private Elements elements;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(processingEnv.getMessager()).thenReturn(messager);
    when(processingEnv.getOptions()).thenReturn(options);
    when(processingEnv.getElementUtils()).thenReturn(elements);
  }

  @Test public void testInitWithRightFilePath() {
    when(options.get(Documentor.DOC_OUTPUT_PROPERTY_NAME)).thenReturn(FILE_PATH);
    Documentor documentor = new Documentor(processingEnv);
    assertThat(documentor.getFile()).isNotNull();
  }

  @Test public void testInitWithoutFilePath() {
    Documentor documentor = new Documentor(processingEnv);
    assertThat(documentor.getFile()).isNull();
  }

  @Test public void testWrite() throws IOException {
    when(options.get(Documentor.DOC_OUTPUT_PROPERTY_NAME)).thenReturn(FILE_PATH);
    Documentor documentor = new Documentor(processingEnv);
    documentor.write(getElements());

    String actual = Files.toString(new File(FILE_PATH), Charsets.UTF_8);
    String expected = "airbnb://example.com/{foo}/bar\\n|#|\\nSample doc\\n|#|\\nDocClass\\"
        + "n|##|\\nairbnb://example.com/{foo}/bar\\n|#|\\n\\n|#|\\nDocClass#DocMethod\\n|##|\\n";
    assertEquals(expected, actual);
  }

  private List<DeepLinkAnnotatedElement> getElements() throws MalformedURLException {
    TypeElement element1 = mock(TypeElement.class);
    Name name1 = mock(Name.class);
    when(elements.getDocComment(element1))
        .thenReturn("Sample doc \n @param empty \n @return nothing");
    when(element1.getSimpleName()).thenReturn(name1);
    when(name1.toString()).thenReturn("DocClass");

    TypeElement element2 = mock(TypeElement.class);
    TypeElement element2Enclosed = mock(TypeElement.class);
    Name name2 = mock(Name.class);
    Name name2Enclosed = mock(Name.class);
    when(element2.getSimpleName()).thenReturn(name2);
    when(name2.toString()).thenReturn("DocMethod");
    when(element2.getEnclosingElement()).thenReturn(element2Enclosed);
    when(element2Enclosed.getSimpleName()).thenReturn(name2Enclosed);
    when(name2Enclosed.toString()).thenReturn("DocClass");

    DeepLinkAnnotatedElement deepLinkElement1 =
        new DeepLinkAnnotatedElement("airbnb://example.com/{foo}/bar",
            element1, DeepLinkEntry.Type.CLASS);
    DeepLinkAnnotatedElement deepLinkElement2 =
        new DeepLinkAnnotatedElement("airbnb://example.com/{foo}/bar",
            element2, DeepLinkEntry.Type.METHOD);
    return ImmutableList.of(deepLinkElement1, deepLinkElement2);
  }
}
