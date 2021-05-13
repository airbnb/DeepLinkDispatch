package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class DeepLinkProcessorIncrementalTest extends BaseDeepLinkProcessorTest {
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

  @Test
  public void testIncrementalProcessorWithCustomDeepLinkRegistration() {
    assertAbout(javaSources())
      .that(Arrays.asList(customAnnotationAppLink, module, sampleActivityWithOnlyCustomDeepLink, fakeBaseDeeplinkDelegate))
      .withCompilerOptions("-AdeepLink.incremental=true")
      .withCompilerOptions("-AdeepLink.customAnnotations=com.example.AppDeepLink")
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forResource("DeepLinkDelegate.java"),
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example"
            + ".SampleModuleRegistry",
          "package com.example;\n"
            + "\n"
            + "import com.airbnb.deeplinkdispatch.BaseRegistry;\n"
            + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
            + "import java.lang.String;\n"
            + "\n"
            + "public final class SampleModuleRegistry extends BaseRegistry {\n"
            + "  public SampleModuleRegistry() {\n"
            + "    super(Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),\n"
            + "    new String[]{});\n"
            + "  }\n"
            + "\n"
            + "  private static String matchIndex0() {\n"
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000or\\u0002\\u0007"
            + "\\u0000\\u0000\\u0000\\u0000\\u0000`example\\u0004\\u000b\\u0000\\u0000\\u0000"
            + "\\u0000\\u0000Mexample.com\\b\\b\\u0000=\\u0000\\u0000\\u0000\\u0000deepLink\\u0000"
            + "\\u001eexample://example.com/deepLink"
            + "\\u0000\\u001acom.example.SampleActivity\\u0000\";\n"
            + "  }\n"
            + "}"
        ));
  }

  @Test
  public void testIncrementalProcessorWithoutCustomDeepLinkRegistration() {
    assertAbout(javaSources())
      .that(Arrays.asList(customAnnotationAppLink, module, sampleActivityWithOnlyCustomDeepLink, fakeBaseDeeplinkDelegate))
      .withCompilerOptions("-AdeepLink.incremental=true")
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forResource("DeepLinkDelegate.java"),
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example"
            + ".SampleModuleRegistry",
          "package com.example;\n"
            + "\n"
            + "import com.airbnb.deeplinkdispatch.BaseRegistry;\n"
            + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
            + "import java.lang.String;\n"
            + "\n"
            + "public final class SampleModuleRegistry extends BaseRegistry {\n"
            + "  public SampleModuleRegistry() {\n"
            + "    super(Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),\n"
            + "    new String[]{});\n"
            + "  }\n"
            + "\n"
            + "  private static String matchIndex0() {\n"
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000r\";\n"
            + "  }\n"
            + "}"));
  }

  @Test
  public void testCustomAnnotationMissingFromCompilerOptionsErrorMessage() {
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
