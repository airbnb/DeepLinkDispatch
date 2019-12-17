package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class DeepLinkProcessorIncrementalTest {
  private final JavaFileObject customAnnotationAppLink = JavaFileObjects
      .forSourceString("AppDeepLink", "package com.example;\n"
          + "import com.airbnb.deeplinkdispatch.DeepLinkSpec;\n"
          + "@DeepLinkSpec(prefix = { \"example://\" })\n"
          + "public @interface AppDeepLink {\n"
          + "    String[] value();\n"
          + "}");

  private final JavaFileObject sampleActivityWithStandardAndCustomDeepLink = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
          + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
          + "@DeepLink(\"airbnb://example.com/deepLink\")\n"
          + "@AppDeepLink({\"example.com/deepLink\",\"example.com/another\"})\n"
          + "public class SampleActivity {\n"
          + "}");

  private final JavaFileObject sampleActivityWithOnlyCustomDeepLink = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
          + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
          + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
          + "import com.example.SampleModule;\n\n"
          + "@AppDeepLink({\"example.com/deepLink\"})\n"
          + "@DeepLinkHandler({ SampleModule.class })\n"
          + "public class SampleActivity {\n"
          + "}");

  private final JavaFileObject module = JavaFileObjects
      .forSourceString("SampleModule", "package com.example;"
          + "import com.airbnb.deeplinkdispatch.DeepLinkModule;\n\n"
          + "@DeepLinkModule\n"
          + "public class SampleModule {\n"
          + "}");

  @Test public void testIncrementalProcessorWithCustomDeepLinkRegistration() {
    assertAbout(javaSources())
        .that(Arrays.asList(customAnnotationAppLink, module, sampleActivityWithOnlyCustomDeepLink))
        .withCompilerOptions("-AdeepLink.incremental=true")
        .withCompilerOptions("-AdeepLink.customAnnotations=com.example.AppDeepLink")
        .processedWith(new DeepLinkProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(
            JavaFileObjects.forResource("DeepLinkDelegate.java"),
            JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example.SampleModuleLoader",
                "package com.example;\n"
                    + "\n"
                    + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
                    + "import com.airbnb.deeplinkdispatch.Parser;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.Collections;\n"
                    + "\n"
                    + "public final class SampleModuleLoader extends Parser {\n"
                    + "  public SampleModuleLoader() {"
                    + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
                    + "    new DeepLinkEntry(\"example://example.com/deepLink\", DeepLinkEntry.Type"
                    + ".CLASS, SampleActivity.class, null)));\n"
                    + "  }\n"
                    + "}"
            ));
  }

  @Test public void testIncrementalProcessorWithoutCustomDeepLinkRegistration() {
    assertAbout(javaSources())
        .that(Arrays.asList(customAnnotationAppLink, module, sampleActivityWithOnlyCustomDeepLink))
        .withCompilerOptions("-AdeepLink.incremental=true")
        .processedWith(new DeepLinkProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(
            JavaFileObjects.forResource("DeepLinkDelegate.java"),
            JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example.SampleModuleLoader",
                "package com.example;\n"
                    + "\n"
                    + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
                    + "import com.airbnb.deeplinkdispatch.Parser;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.Collections;\n"
                    + "\n"
                    + "public final class SampleModuleLoader extends Parser {\n"
                    + "  public SampleModuleLoader() {"
                    + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
                    + "    )));"
                    + "  }\n"
                    + "}"
            ));
  }

  @Test public void testCustomAnnotationMissingFromCompilerOptionsErrorMessage() {
    assertAbout(javaSources())
        .that(Arrays.asList(customAnnotationAppLink, sampleActivityWithStandardAndCustomDeepLink))
        .withCompilerOptions("-AdeepLink.incremental=true")
        .processedWith(new DeepLinkProcessor())
        .failsToCompile()
        .withErrorContaining(
            "Unable to find annotation 'com.example.AppDeepLink' you must update "
                + "'deepLink.customAnnotations' within the build.gradle");
  }
}
