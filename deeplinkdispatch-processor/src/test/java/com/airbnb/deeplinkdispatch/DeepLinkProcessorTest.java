package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class DeepLinkProcessorTest {
  @Test public void testProcessor() {
    JavaFileObject sampleActivity = JavaFileObjects
        .forSourceString("SampleActivity", "package com.example;"
            + "import com.airbnb.deeplinkdispatch.DeepLink; "
            + "@DeepLink(\"example.com/deepLink\") public class SampleActivity {}");

    assert_().about(javaSource())
        .that(sampleActivity)
        .processedWith(new DeepLinkProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(
            JavaFileObjects.forResource("DeepLinkActivity.java"),
            JavaFileObjects.forSourceString("DeepLinkLoader",
                "package com.airbnb.deeplinkdispatch;\n"
    + "import com.example.SampleActivity;\n"
    + "public final class DeepLinkLoader implements Loader {\n"
    + "  public void load(DeepLinkRegistry registry) {\n"
    + "    registry.registerDeepLink(\"example.com/deepLink\", DeepLinkEntry.Type.CLASS, "
    + "SampleActivity.class, null);\n"
    + "  }\n"
    + "}\n"));
  }
}
