package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class DeepLinkProcessorNonIncrementalTest {
  private static final JavaFileObject SIMPLE_DEEPLINK_ACTIVITY = JavaFileObjects
    .forSourceString("SampleActivity", "package com.example;"
      + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
      + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
      + "import com.example.SampleModule;\n\n"
      + "@MyDeepLink({\"example.com/deepLink\",\"example.com/another\"})\n"
      + "public class SampleActivity {\n"
      + "}");

  private static final JavaFileObject SIMPLE_DEEPLINK_MODULE = JavaFileObjects.forSourceString(
    "SampleModule", "package com.example;"
      + "import com.airbnb.deeplinkdispatch.DeepLinkModule;\n\n"
      + "@DeepLinkModule\n"
      + "public class SampleModule {\n"
      + "}");

  private static final JavaFileObject SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE =
    JavaFileObjects.forSourceString(
      "SampleModule", "package com.Example;"
        + "import com.airbnb.deeplinkdispatch.DeepLinkModule;\n\n"
        + "@DeepLinkModule\n"
        + "public class SampleModule {\n"
        + "}");

  @Test
  public void testProcessor() {
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
        + "import com.example.SampleModule;\n\n"
        + "@DeepLink(\"airbnb://example.com/deepLink\")\n"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE, sampleActivity))
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forResource("DeepLinkDelegate.java"),
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example.SampleModuleRe"
            + "gistry",
          "package com.example;\n"
            + "\n"
            + "import com.airbnb.deeplinkdispatch.BaseRegistry;\n"
            + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
            + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
            + "import java.lang.String;\n"
            + "import java.util.Arrays;\n"
            + "import java.util.Collections;\n"
            + "import java.util.HashSet;\n"
            + "\n"
            + "public final class SampleModuleRegistry extends BaseRegistry {\n"
            + "  public SampleModuleRegistry() {\n"
            + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
            + "      new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry.Type.CLASS,"
            + "SampleActivity.class, null)\n"
            + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),\n"
            + "    new HashSet<String>(Arrays.<String>asList()));\n"
            + "  }\n"
            + "\n"
            + "  private static String matchIndex0() {\n"
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u00001ÿÿr\\u0002\\u0006\\u0000\\u0000\\u0"
            + "000#ÿÿairbnb\\u0004\\u000b\\u0000\\u0000\\u0000\\u0010ÿÿexample.com\\b\\b\\u0000\\u0"
            + "000\\u0000\\u0000\\u0000\\u0000deepLink\";}\n"
            + "}"
        ));
  }

  @Test
  public void testProcessorWithDefaultAndCustomAnnotations() {
    JavaFileObject customAnnotationWebLink = JavaFileObjects
      .forSourceString("WebDeepLink", "package com.example;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkSpec;\n"
        + "@DeepLinkSpec(prefix = { \"http://\", \"https://\"})\n"
        + "public @interface WebDeepLink {\n"
        + "    String[] value();\n"
        + "}");
    JavaFileObject customAnnotationAppLink = JavaFileObjects
      .forSourceString("AppDeepLink", "package com.example;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkSpec;\n"
        + "@DeepLinkSpec(prefix = { \"example://\" })\n"
        + "public @interface AppDeepLink {\n"
        + "    String[] value();\n"
        + "}");
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
        + "import com.example.SampleModule;\n\n"
        + "@DeepLink(\"airbnb://example.com/deepLink\")\n"
        + "@AppDeepLink({\"example.com/deepLink\",\"example.com/another\"})\n"
        + "@WebDeepLink({\"example.com/deepLink\",\"example.com/another\"})\n"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(customAnnotationAppLink, customAnnotationWebLink,
        SIMPLE_DEEPLINK_MODULE, sampleActivity))
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forResource("DeepLinkDelegate.java"),
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example.SampleModuleRegistry",
          "package com.example;\n"
            + "\n"
            + "import com.airbnb.deeplinkdispatch.BaseRegistry;\n"
            + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
            + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
            + "import java.lang.String;\n"
            + "import java.util.Arrays;\n"
            + "import java.util.Collections;\n"
            + "import java.util.HashSet;\n"
            + "\n"
            + "public final class SampleModuleRegistry extends BaseRegistry {\n"
            + "  public SampleModuleRegistry() {\n"
            + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
            + "      new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry.Type.CLASS,"
            + "SampleActivity.class, null),\n"
            + "      new DeepLinkEntry(\"example://example.com/another\", DeepLinkEntry.Type.CLASS,"
            + "SampleActivity.class, null),\n"
            + "      new DeepLinkEntry(\"example://example.com/deepLink\", DeepLinkEntry.Type.CLASS"
            + ", SampleActivity.class, null),\n"
            + "      new DeepLinkEntry(\"http://example.com/another\", DeepLinkEntry.Type.CLASS, Sa"
            + "mpleActivity.class, null),\n"
            + "      new DeepLinkEntry(\"http://example.com/deepLink\", DeepLinkEntry.Type.CLASS, S"
            + "ampleActivity.class, null),\n"
            + "      new DeepLinkEntry(\"https://example.com/another\", DeepLinkEntry.Type.CLASS, S"
            + "ampleActivity.class, null),\n"
            + "      new DeepLinkEntry(\"https://example.com/deepLink\", DeepLinkEntry.Type.CLASS, "
            + "SampleActivity.class, null)\n"
            + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),\n"
            + "    new HashSet<String>(Arrays.<String>asList()));\n"
            + "  }\n"
            + "\n"
            + "  private static String matchIndex0() {\n"
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000ïÿÿr\\u0002\\u0006\\u0000\\u0000\\u0"
            + "000#ÿÿairbnb\\u0004\\u000b\\u0000\\u0000\\u0000\\u0010ÿÿexample.com\\b\\b\\u0000\\u0"
            + "000\\u0000\\u0000\\u0000\\u0000deepLink\\u0002\\u0007\\u0000\\u0000\\u00002ÿÿexample"
            + "\\u0004\\u000b\\u0000\\u0000\\u0000\\u001fÿÿexample.com\\b\\u0007\\u0000\\u0000\\u00"
            + "00\\u0000\\u0000\\u0001another\\b\\b\\u0000\\u0000\\u0000\\u0000\\u0000\\u0002deepLi"
            + "nk\\u0002\\u0004\\u0000\\u0000\\u00002ÿÿhttp\\u0004\\u000b\\u0000\\u0000\\u0000\\u00"
            + "1fÿÿexample.com\\b\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003another\\b\\b\\u0"
            + "000\\u0000\\u0000\\u0000\\u0000\\u0004deepLink\\u0002\\u0005\\u0000\\u0000\\u00002ÿÿ"
            + "https\\u0004\\u000b\\u0000\\u0000\\u0000\\u001fÿÿexample.com\\b\\u0007\\u0000\\u0000"
            + "\\u0000\\u0000\\u0000\\u0005another\\b\\b\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006d"
            + "eepLink\";}\n"
            + "}"
        ));
  }

  @Test
  public void testDuplicatedUriMatch() {
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import android.content.Context;\n"
        + "import android.content.Intent;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n"
        + "import com.example.SampleModule;\n\n"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "  @DeepLink(\"airbnb://host/{var}\")"
        + "  public static Intent intentFromOnePathWithOneParam(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/path\")"
        + "  public static Intent intentFromOnePath(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/path1/path2\")"
        + "  public static Intent intentFromTwoPath(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/path1/path2?q={q}\")"
        + "  public static Intent intentFromTwoPathWithQuery(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/{var1}/{var2}\")"
        + "  public static Intent intentFromTwoPathWithTwoParams(Context context){"
        + "    return new Intent();"
        + "  }"
        + "}"
      );

    JavaFileObject module = JavaFileObjects.forSourceString(
      "SampleModule", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLinkModule;\n\n"
        + "@DeepLinkModule\n"
        + "public class SampleModule {\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(module, sampleActivity))
      .processedWith(new DeepLinkProcessor())
      .failsToCompile().withErrorContaining("Internal error during annotation "
      + "processing: java.lang.IllegalStateException: Ambiguous URI. Same match for two URIs "
      + "(UriMatch(uri=airbnb://host/path1/path2?q={q}, matchId=0, annotatedElement=com.example."
      + "SampleActivity, annotatedMethod=intentFromTwoPathWithQuery) vs UriMatch(uri="
      + "airbnb://host/path1/path2, matchId=1, annotatedElement=com.example.SampleActivity, "
      + "annotatedMethod=intentFromTwoPath))");
  }

  @Test
  public void uppercasePackage() {
    JavaFileObject activityWithUppercasePackage = JavaFileObjects
      .forSourceString("SampleActivity", "package com.Example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
        + "import com.Example.SampleModule;\n\n"
        + "@DeepLink(\"airbnb://example.com/deepLink\")"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE, activityWithUppercasePackage))
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.Example."
            + "SampleModuleRegistry",
          "package com.Example;\n"
            + "\n"
            + "import com.airbnb.deeplinkdispatch.BaseRegistry;\n"
            + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
            + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
            + "import java.lang.String;\n"
            + "import java.util.Arrays;\n"
            + "import java.util.Collections;\n"
            + "import java.util.HashSet;\n"
            + "\n"
            + "public final class SampleModuleRegistry extends BaseRegistry {\n"
            + "  public SampleModuleRegistry() {\n"
            + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
            + "      new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry.Type.CLASS,"
            + "SampleActivity.class, null)\n"
            + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),\n"
            + "    new HashSet<String>(Arrays.<String>asList()));\n"
            + "  }\n"
            + "\n"
            + "  private static String matchIndex0() {\n"
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u00001ÿÿr\\u0002\\u0006\\u0000\\u0000\\u0"
            + "000#ÿÿairbnb\\u0004\\u000b\\u0000\\u0000\\u0000\\u0010ÿÿexample.com\\b\\b\\u0000\\u0"
            + "000\\u0000\\u0000\\u0000\\u0000deepLink\";}\n"
            + "}"
        )
      );
  }

  @Test
  public void testNonStaticMethodCompileFail() {
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink; "
        + "public class SampleActivity {"
        + "  @DeepLink(\"airbnb://host/{arbitraryNumber}\")"
        + "  public Intent intentFromNoStatic(Context context) {"
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

  @Test
  public void testProcessorWithEmptyCustomPrefixFail() {
    JavaFileObject emptyPrefixLink = JavaFileObjects
      .forSourceString("MyDeepLink", "package com.example;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkSpec;\n"
        + "@DeepLinkSpec(prefix = { \"http://\", \"\" })\n"
        + "public @interface MyDeepLink {\n"
        + "    String[] value();\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(emptyPrefixLink, SIMPLE_DEEPLINK_ACTIVITY, SIMPLE_DEEPLINK_MODULE))
      .processedWith(new DeepLinkProcessor())
      .failsToCompile()
      .withErrorContaining("Prefix property cannot have null or empty strings");
  }

  @Test
  public void testProcessorWithEmptyDeepLinkSpecPrefixesFail() {
    JavaFileObject emptyPrefixArrayLink = JavaFileObjects
      .forSourceString("MyDeepLink", "package com.example;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkSpec;\n"
        + "@DeepLinkSpec(prefix = { })\n"
        + "public @interface MyDeepLink {\n"
        + "    String[] value();\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(emptyPrefixArrayLink, SIMPLE_DEEPLINK_ACTIVITY,
        SIMPLE_DEEPLINK_MODULE))
      .processedWith(new DeepLinkProcessor())
      .failsToCompile()
      .withErrorContaining("Prefix property cannot be empty");
  }

  @Test
  public void testProcessorWithDeepLinkSortRule() {
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import android.content.Context;\n"
        + "import android.content.Intent;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n"
        + "import com.example.SampleModule;\n\n"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "  @DeepLink(\"airbnb://host/{var}\")"
        + "  public static Intent intentFromOnePathWithOneParam(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/path\")"
        + "  public static Intent intentFromOnePath(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/path1/path2\")"
        + "  public static Intent intentFromTwoPath(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/path1/path3?q={q}\")"
        + "  public static Intent intentFromTwoPathWithQuery(Context context){"
        + "    return new Intent();"
        + "  }"
        + "  @DeepLink(\"airbnb://host/{var1}/{var2}\")"
        + "  public static Intent intentFromTwoPathWithTwoParams(Context context){"
        + "    return new Intent();"
        + "  }"
        + "}"
      );

    assertAbout(javaSources())
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE, sampleActivity))
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forResource("DeepLinkDelegate.java"),
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example."
            + "SampleModuleRegistry",
          "package com.example;\n"
            + "\n"
            + "import com.airbnb.deeplinkdispatch.BaseRegistry;\n"
            + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
            + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
            + "import java.lang.String;\n"
            + "import java.util.Arrays;\n"
            + "import java.util.Collections;\n"
            + "import java.util.HashSet;\n"
            + "\n"
            + "public final class SampleModuleRegistry extends BaseRegistry {\n"
            + "  public SampleModuleRegistry() {\n"
            + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
            + "      new DeepLinkEntry(\"airbnb://host/path1/path3?q={q}\", "
            + "DeepLinkEntry.Type.METHOD, SampleActivity.class, \"intentFromTwoPathWithQuery\"),\n"
            + "      new DeepLinkEntry(\"airbnb://host/path1/path2\", "
            + "DeepLinkEntry.Type.METHOD, SampleActivity.class, \"intentFromTwoPath\"),\n"
            + "      new DeepLinkEntry(\"airbnb://host/{var1}/{var2}\","
            + " DeepLinkEntry.Type.METHOD, SampleActivity.class,"
            + " \"intentFromTwoPathWithTwoParams\"),\n"
            + "      new DeepLinkEntry(\"airbnb://host/path\", "
            + "DeepLinkEntry.Type.METHOD, SampleActivity.class, \"intentFromOnePath\"),\n"
            + "      new DeepLinkEntry(\"airbnb://host/{var}\", "
            + "DeepLinkEntry.Type.METHOD, SampleActivity.class, "
            + "\"intentFromOnePathWithOneParam\")\n"
            + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }),\n"
            + "    new HashSet<String>(Arrays.<String>asList()));\n"
            + "  }\n"
            + "\n"
            + "  private static String matchIndex0() {\n"
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000vÿÿr\\u0002\\u0006\\u0000\\u0000\\"
            + "u0000hÿÿairbnb\\u0004\\u0004\\u0000\\u0000\\u0000\\\\ÿÿhost\\b\\u0004\\u0000\\"
            + "u0000\\u0000\\u0000\\u0000\\u0003path\\b\\u0005\\u0000\\u0000\\u0000\\u001aÿÿpath1\\"
            + "b\\u0005\\u0000\\u0000\\u0000\\u0000\\u0000\\u0001path2\\b\\u0005\\u0000\\u0000\\"
            + "u0000\\u0000\\u0000\\u0000path3\\u0018\\u0006\\u0000\\u0000\\u0000\\u000eÿÿ{var1}\\"
            + "u0018\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0002{var2}\\u0018\\u0005\\u0000\\"
            + "u0000\\u0000\\u0000\\u0000\\u0004{var}\";}\n"
            + "}"));
  }

  @Test
  public void testNonAppCompatTaskStackBuilderClassErrorMessage() {
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink; "
        + "import android.content.Context;\n"
        + "import android.app.TaskStackBuilder;\n"
        + "import android.content.Intent;\n"
        + "public class SampleActivity {"
        + "  @DeepLink(\"airbnb://host/{arbitraryNumber}\")"
        + "  public static TaskStackBuilder intentFromNoStatic(Context context) {"
        + "    return TaskStackBuilder.create(context);"
        + "  }"
        + "}"
      );

    assertAbout(javaSource())
      .that(sampleActivity)
      .processedWith(new DeepLinkProcessor())
      .failsToCompile()
      .withErrorContaining(
        "Only `Intent` or `androidx.core.app.TaskStackBuilder` are supported."
          + " Please double check your imports and try again.");
  }

  @Test
  public void testInvalidComponentParamInSchemeErrorMessage() {
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
        + "import com.example.SampleModule;\n\n"
        + "@DeepLink(\"airbnb://example.com/d{eepLink\")\n"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE, sampleActivity))
      .processedWith(new DeepLinkProcessor())
      .failsToCompile()
      .withErrorContaining("Invalid URI component: d{eepLink. { must come before }.");
  }

  @Test
  public void testInvalidComponentParamPathSegmentErrorMessage() {
    JavaFileObject sampleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
        + "import com.example.SampleModule;\n\n"
        + "@DeepLink(\"airbnb://example.com/de}{epLink\")\n"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE, sampleActivity))
      .processedWith(new DeepLinkProcessor())
      .failsToCompile()
      .withErrorContaining(
"Invalid URI component: de}{epLink. { must come before }."
      );
  }

  @Test
  public void malformedConfigurablePathSegmentFailsWithErrorMessage() {
    JavaFileObject simpleActivity = JavaFileObjects
      .forSourceString("SampleActivity", "package com.example;"
        + "import com.airbnb.deeplinkdispatch.DeepLink;\n"
        + "import com.airbnb.deeplinkdispatch.DeepLinkHandler;\n\n"
        + "import com.Example.SampleModule;\n\n"
        + "@DeepLink(\"airbnb://example.com/deepL<ink\")"
        + "@DeepLinkHandler({ SampleModule.class })\n"
        + "public class SampleActivity {\n"
        + "}");

    assertAbout(javaSources())
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE, simpleActivity))
      .processedWith(new DeepLinkProcessor())
      .failsToCompile()
      .withErrorContaining(
        "Malformed path segment: deepL<ink! If it contains < or >, it must start"
        + " with < and end with >."
      );
  }
}
