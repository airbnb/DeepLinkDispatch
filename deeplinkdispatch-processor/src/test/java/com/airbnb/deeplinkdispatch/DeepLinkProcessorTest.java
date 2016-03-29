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
            + "@DeepLink(\"airbnb://example.com/deepLink\") public class SampleActivity {}");

    assert_().about(javaSource())
        .that(sampleActivity)
        .processedWith(new DeepLinkProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(
            JavaFileObjects.forResource("DeepLinkDispatchActivity.java"),
            JavaFileObjects.forSourceString("DeepLinkLoader.java",
                "package com.airbnb.deeplinkdispatch;\n"
                    + "\n"
                    + "import com.example.SampleActivity;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.LinkedList;\n"
                    + "import java.util.List;\n"
                    + "\n"
                    + "public final class DeepLinkLoader {\n"
                    + "  private final List<DeepLinkEntry> registry = new LinkedList<>();\n"
                    + "\n"
                    + "  void load() {\n"
                    + "    registry.add(new DeepLinkEntry(\"airbnb://example.com/deepLink\", "
                    + "DeepLinkEntry.Type.CLASS, SampleActivity.class, null));\n"
                    + "  }\n"
                    + "\n"
                    + "  DeepLinkEntry parseUri(String uri) {\n"
                    + "    for (DeepLinkEntry entry : registry) {\n"
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
            + "import com.airbnb.deeplinkdispatch.DeepLink; "
            + "@DeepLink(\"airbnb://example.com/deepLink\") public class SampleActivity {}");

    assert_().about(javaSource())
        .that(activityWithUppercasePackage)
        .processedWith(new DeepLinkProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(
            JavaFileObjects.forResource("DeepLinkDispatchActivity.java"),
            JavaFileObjects.forSourceString("DeepLinkLoader.java",
                "package com.airbnb.deeplinkdispatch;\n"
                    + "\n"
                    + "import com.Example.SampleActivity;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.LinkedList;\n"
                    + "import java.util.List;\n"
                    + "\n"
                    + "public final class DeepLinkLoader {\n"
                    + "  private final List<DeepLinkEntry> registry = new LinkedList<>();\n"
                    + "\n"
                    + "  void load() {\n"
                    + "    registry.add(new DeepLinkEntry(\"airbnb://example.com/deepLink\", "
                    + "DeepLinkEntry.Type.CLASS, SampleActivity.class, null));\n"
                    + "  }\n"
                    + "\n"
                    + "  DeepLinkEntry parseUri(String uri) {\n"
                    + "    for (DeepLinkEntry entry : registry) {\n"
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

    assert_().about(javaSource())
        .that(sampleActivity)
        .processedWith(new DeepLinkProcessor())
        .failsToCompile()
        .withErrorContaining("Only static methods can be annotated");
  }
}
