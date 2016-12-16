package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class DeepLinkProcessorTest {
  @Test public void testProcessor() {
    JavaFileObject sampleActivity = JavaFileObjects
        .forSourceString("SampleActivity", "package com.example;"
            + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
            + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
            + "import com.example.SampleModule;\n\n"
            + "@DeepLink(\"airbnb://example.com/deepLink\")\n"
            + "@DeepLinkHandler({ SampleModule.class })\n"
            + "public class SampleActivity {\n"
            + "}");

    JavaFileObject module = JavaFileObjects.forSourceString(
        "SampleModule", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLinkModule;\n\n"
        + "@DeepLinkModule\n"
        + "public class SampleModule {\n"
        + "}");

    assertAbout(javaSources())
        .that(Arrays.asList(module, sampleActivity))
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
                    + "import java.lang.Override;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.List;\n"
                    + "\n"
                    + "public final class SampleModuleLoader implements Parser {\n"
                    + "  private static final List<DeepLinkEntry> REGISTRY = Arrays.asList("
                    + "new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry.Type"
                    + ".CLASS, SampleActivity.class, null));\n"
                    + "\n"
                    + "  @Override"
                    + "  public DeepLinkEntry parseUri(String uri) {\n"
                    + "    for (DeepLinkEntry entry : REGISTRY) {\n"
                    + "      if (entry.matches(uri)) {\n"
                    + "        return entry;\n"
                    + "      }\n"
                    + "    }\n"
                    + "    return null;\n"
                    + "  }\n"
                    + "}"));
  }

  @Test public void uppercasePackage() {
    JavaFileObject activityWithUppercasePackage = JavaFileObjects
        .forSourceString("SampleActivity", "package com.Example;"
            + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
            + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
            + "import com.Example.SampleModule;\n\n"
            + "@DeepLink(\"airbnb://example.com/deepLink\")"
            + "@DeepLinkHandler({ SampleModule.class })\n"
            + "public class SampleActivity {\n"
            + "}");

    JavaFileObject module = JavaFileObjects.forSourceString(
        "SampleModule", "package com.Example;"
            + "import com.airbnb.deeplinkdispatch.DeepLinkModule;\n\n"
            + "@DeepLinkModule\n"
            + "public class SampleModule {\n"
            + "}");

    assertAbout(javaSources())
        .that(Arrays.asList(module, activityWithUppercasePackage))
        .processedWith(new DeepLinkProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(
            JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.Example.SampleModuleLoader",
                "package com.Example;\n"
                    + "\n"
                    + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
                    + "import com.airbnb.deeplinkdispatch.Parser;\n"
                    + "import java.lang.Override;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.List;\n"
                    + "\n"
                    + "public final class SampleModuleLoader implements Parser {\n"
                    + "  private static final List<DeepLinkEntry> REGISTRY = Arrays.asList("
                    + "new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry.Type"
                    + ".CLASS, SampleActivity.class, null));\n"
                    + "\n"
                    + "  @Override"
                    + "  public DeepLinkEntry parseUri(String uri) {\n"
                    + "    for (DeepLinkEntry entry : REGISTRY) {\n"
                    + "      if (entry.matches(uri)) {\n"
                    + "        return entry;\n"
                    + "      }\n"
                    + "    }\n"
                    + "    return null;\n"
                    + "  }\n"
                    + "}"));
  }

  @Test public void testNonStaticMethodCompileFail() {
    JavaFileObject sampleActivity = JavaFileObjects
        .forSourceString("SampleActivity", "package com.example;"
            + "import com.airbnb.deeplinkdispatch.DeepLink; "
            + "public class SampleActivity {"
            + "  @DeepLink(\"airbnb://host/{arbitraryNumber}\")"
            + "  public Intent intentFromNoStatic(Context context){"
            + "    return new Intent();"
            + "  }"
            + "}"
        );

    assertAbout(javaSource())
        .that(sampleActivity)
        .processedWith(new DeepLinkProcessor())
        .failsToCompile()
        .withErrorContaining("Only static methods can be annotated");
  }
}
