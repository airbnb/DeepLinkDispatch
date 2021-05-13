package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class DeepLinkProcessorNonIncrementalTest extends BaseDeepLinkProcessorTest {
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
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate))
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
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000mr\\u0002\\u0006"
            + "\\u0000\\u0000\\u0000\\u0000\\u0000_airbnb\\u0004\\u000b\\u0000\\u0000\\u0000\\u0000"
            + "\\u0000Lexample.com\\b\\b\\u0000<\\u0000\\u0000\\u0000\\u0000deepLink\\u0000"
            + "\\u001dairbnb://example.com/deepLink\\u0000"
            + "\\u001acom.example.SampleActivity\\u0000\";\n"
            + "  }\n"
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
        SIMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate))
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forResource("DeepLinkDelegate.java"),
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example.SampleModuleRegistry",
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
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000\\u0000\\u0002\\u008cr\\u0002\\u0006"
            + "\\u0000\\u0000\\u0000\\u0000\\u0000_airbnb\\u0004\\u000b\\u0000\\u0000\\u0000\\u0000"
            + "\\u0000Lexample.com\\b\\b\\u0000<\\u0000\\u0000\\u0000\\u0000deepLink\\u0000"
            + "\\u001dairbnb://example.com/deepLink\\u0000\\u001acom.example.SampleActivity\\u0000"
            + "\\u0002\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000«example\\u0004\\u000b\\u0000"
            + "\\u0000\\u0000\\u0000\\u0000\\u0098example.com\\b\\u0007\\u0000<\\u0000\\u0000"
            + "\\u0000\\u0000another\\u0000\\u001dexample://example.com/another\\u0000"
            + "\\u001acom.example.SampleActivity\\u0000\\b\\b\\u0000=\\u0000\\u0000\\u0000"
            + "\\u0000deepLink\\u0000\\u001eexample://example.com/deepLink\\u0000"
            + "\\u001acom.example.SampleActivity\\u0000\\u0002\\u0004\\u0000\\u0000\\u0000\\u0000"
            + "\\u0000¥http\\u0004\\u000b\\u0000\\u0000\\u0000\\u0000\\u0000\\u0092example.com\\b"
            + "\\u0007\\u00009\\u0000\\u0000\\u0000\\u0000another\\u0000\\u001a"
            + "http://example.com/another\\u0000\\u001acom.example.SampleActivity\\u0000\\b\\b"
            + "\\u0000:\\u0000\\u0000\\u0000\\u0000deepLink\\u0000\\u001b"
            + "http://example.com/deepLink\\u0000\\u001acom.example.SampleActivity\\u0000\\u0002"
            + "\\u0005\\u0000\\u0000\\u0000\\u0000\\u0000§https\\u0004\\u000b\\u0000\\u0000\\u0000"
            + "\\u0000\\u0000\\u0094example.com\\b\\u0007\\u0000:\\u0000\\u0000\\u0000\\u0000"
            + "another\\u0000\\u001bhttps://example.com/another\\u0000\\u001acom.example."
            + "SampleActivity\\u0000\\b\\b\\u0000;\\u0000\\u0000\\u0000\\u0000deepLink\\u0000"
            + "\\u001chttps://example.com/deepLink\\u0000\\u001acom.example.SampleActivity\\u0000"
            + "\";\n"
            + "  }\n"
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
      + "(UriMatch(uriTemplate=airbnb://host/path1/path2?q={q}, "
      + "annotatedClassFullyQualifiedName=com.example.SampleActivity, "
      + "annotatedMethod=intentFromTwoPathWithQuery) vs "
      + "UriMatch(uriTemplate=airbnb://host/path1/path2, "
      + "annotatedClassFullyQualifiedName=com.example"
      + ".SampleActivity, annotatedMethod=intentFromTwoPath))");
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
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE_UPPERCASE_PACKAGE,
        activityWithUppercasePackage,
        fakeBaseDeeplinkDelegate))
      .processedWith(new DeepLinkProcessor())
      .compilesWithoutError()
      .and()
      .generatesSources(
        JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.Example."
            + "SampleModuleRegistry",
          "package com.Example;\n"
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
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000mr\\u0002\\u0006"
            + "\\u0000\\u0000\\u0000\\u0000\\u0000_airbnb\\u0004\\u000b\\u0000\\u0000\\u0000"
            + "\\u0000\\u0000Lexample.com\\b\\b\\u0000<\\u0000\\u0000\\u0000\\u0000deepLink"
            + "\\u0000\\u001dairbnb://example.com/deepLink\\u0000\\u001acom.Example."
            + "SampleActivity\\u0000\";\n"
            + "  }\n"
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
      .that(Arrays.asList(SIMPLE_DEEPLINK_MODULE, sampleActivity, fakeBaseDeeplinkDelegate))
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
            + "    return \"\\u0001\\u0001\\u0000\\u0000\\u0000\\u0000\\u0002\\u0000r\\u0002\\u0006"
            + "\\u0000\\u0000\\u0000\\u0000\\u0001òairbnb\\u0004\\u0004\\u0000\\u0000\\u0000\\u0000"
            + "\\u0001æhost\\b\\u0004\\u0000B\\u0000\\u0000\\u0000\\u0000path\\u0000\\u0012airbnb:"
            + "//host/path\\u0000\\u001acom.example.SampleActivity\\u0011intentFromOnePath\\b"
            + "\\u0005\\u0000\\u0000\\u0000\\u0000\\u0000»path1\\b\\u0005\\u0000I\\u0000\\u0000"
            + "\\u0000\\u0000path2\\u0000\\u0019airbnb://host/path1/path2\\u0000\\u001acom.example."
            + "SampleActivity\\u0011intentFromTwoPath\\b\\u0005\\u0000X\\u0000\\u0000\\u0000\\"
            + "u0000path3\\u0000\\u001fairbnb://host/path1/path3?q={q}\\u0000\\u001acom.example."
            + "SampleActivity\\u001aintentFromTwoPathWithQuery\\u0018\\u0006\\u0000\\u0000\\u0000"
            + "\\u0000\\u0000f{var1}\\u0018\\u0006\\u0000X\\u0000\\u0000\\u0000\\u0000{var2}\\u0000"
            + "\\u001bairbnb://host/{var1}/{var2}\\u0000\\u001acom.example.SampleActivity\\u001e"
            + "intentFromTwoPathWithTwoParams\\u0018\\u0005\\u0000O\\u0000\\u0000\\u0000\\u0000"
            + "{var}\\u0000\\u0013airbnb://host/{var}\\u0000\\u001acom.example.SampleActivity"
            + "\\u001dintentFromOnePathWithOneParam\";\n"
            + "  }\n"
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
