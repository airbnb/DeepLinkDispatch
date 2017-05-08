package com.airbnb.deeplinkdispatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;

import javax.lang.model.element.TypeElement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class DeepLinkAnnotatedElementTest {
  @Mock TypeElement element;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test public void testValidUri() throws MalformedURLException {
    DeepLinkAnnotatedElement annotatedElement = new DeepLinkAnnotatedElement(
        "airbnb://example.com/{foo}/bar", element, DeepLinkEntry.Type.CLASS);

    assertThat(annotatedElement.getUri()).isEqualTo("airbnb://example.com/{foo}/bar");
  }

  @Test public void testQueryParam() throws MalformedURLException {
    DeepLinkAnnotatedElement annotatedElement = new DeepLinkAnnotatedElement(
        "airbnb://classDeepLink?foo=bar", element, DeepLinkEntry.Type.CLASS);

    assertThat(annotatedElement.getUri()).isEqualTo("airbnb://classDeepLink?foo=bar");
  }

  @Test public void testInvalidUri() {
    try {
      new DeepLinkAnnotatedElement("http", element, DeepLinkEntry.Type.CLASS);
      fail();
    } catch (MalformedURLException ignored) {
    }
  }

  @Test public void testMissingScheme() {
    try {
      new DeepLinkAnnotatedElement("example.com/something", element, DeepLinkEntry.Type.CLASS);
      fail();
    } catch (MalformedURLException ignored) {
    }
  }

  @Test public void testPrefixes() throws MalformedURLException {
    DeepLinkAnnotatedElement annotatedElement = new DeepLinkAnnotatedElement(
              "example.com/{foo}/bar", element, DeepLinkEntry.Type.CLASS, new String[]{"ex://",
              "ex://d/"});
    String[] prefixes = annotatedElement.getPrefixes();
    assertThat(prefixes[0] + annotatedElement.getUriForDeepLinkEntry())
            .isEqualTo("ex://example.com/{foo}/bar");

    assertThat(prefixes[1] + annotatedElement.getUriForDeepLinkEntry())
            .isEqualTo("ex://d/example.com/{foo}/bar");
  }

  @Test public void testInvalidPrefixes() {
    try {
      new DeepLinkAnnotatedElement("example.com/{foo}/bar", element, DeepLinkEntry.Type.CLASS,
              new String[]{"airbnb://", "invalid prefix"});
      fail();
    } catch (MalformedURLException ignored) {
    }
  }
}
