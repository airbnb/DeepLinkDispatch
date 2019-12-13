package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;
import org.junit.Test;

import javax.tools.JavaFileObject;
import java.util.Arrays;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class DeepLinkProcessorTest {
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
                    + "import com.airbnb.deeplinkdispatch.BaseLoader;\n"
                    + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
                    + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.Collections;\n"
                    + "\n"
                    + "public final class SampleModuleLoader extends BaseLoader {\n"
                    + "  public SampleModuleLoader() {\n"
                    + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
                    + "      new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry."
                    + "Type.CLASS, SampleActivity.class, null)\n"
                    + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }"
                    + " ));\n }\n"
                    + "\n"
                    + "  private static String matchIndex0() {\n"
                    + "    return \"\\u0000\\u0001\\u0000\\u0000\\u00001ÿÿr\\u0001\\u0006\\u0000"
                    + "\\u0000\\u0000#ÿÿairbnb\\u0002\\u000b\\u0000\\u0000\\u0000"
                    + "\\u0010ÿÿexample.com\\u0003\\b\\u0000\\u0000\\u0000\\u0000\\u0000"
                    + "\\u0000deepLink\";}\n"
                    + "}"
            ));
  }

  @Test public void testProcessorWithDefaultAndCustomAnnotations() {
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

    JavaFileObject module = JavaFileObjects.forSourceString(
        "SampleModule", "package com.example;"
            + "import com.airbnb.deeplinkdispatch.DeepLinkModule;\n\n"
            + "@DeepLinkModule\n"
            + "public class SampleModule {\n"
            + "}");

    assertAbout(javaSources())
        .that(Arrays.asList(customAnnotationAppLink, customAnnotationWebLink,
            module, sampleActivity))
        .processedWith(new DeepLinkProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(
            JavaFileObjects.forResource("DeepLinkDelegate.java"),
            JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example.SampleModuleLoader",
                "package com.example;\n"
                    + "\n"
                    + "import com.airbnb.deeplinkdispatch.BaseLoader;\n"
                    + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
                    + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.Collections;\n"
                    + "\n"
                    + "public final class SampleModuleLoader extends BaseLoader {\n"
                    + "  public SampleModuleLoader() {\n"
                    + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
                    + "      new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry."
                    + "Type.CLASS, SampleActivity.class, null),\n"
                    + "      new DeepLinkEntry(\"example://example.com/another\", DeepLinkEntry."
                    + "Type.CLASS, SampleActivity.class, null),\n"
                    + "      new DeepLinkEntry(\"example://example.com/deepLink\", DeepLinkEntry.T"
                    + "ype.CLASS, SampleActivity.class, null),\n"
                    + "      new DeepLinkEntry(\"http://example.com/another\", DeepLinkEntry.Type."
                    + "CLASS, SampleActivity.class, null),\n"
                    + "      new DeepLinkEntry(\"http://example.com/deepLink\", DeepLinkEntry.Type."
                    + "CLASS, SampleActivity.class, null),\n"
                    + "      new DeepLinkEntry(\"https://example.com/another\", DeepLinkEntry.Type."
                    + "CLASS, SampleActivity.class, null),\n"
                    + "      new DeepLinkEntry(\"https://example.com/deepLink\", DeepLinkEntry."
                    + "Type.CLASS, SampleActivity.class, null)\n"
                    + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }"
                    + " ));\n}\n"
                    + " \n"
                    + "  private static String matchIndex0() {\n"
                    + "    return \"\\u0000\\u0001\\u0000\\u0000\\u0000ïÿÿr\\u0001\\u0006\\u0000"
                    + "\\u0000\\u0000#ÿÿairbnb\\u0002\\u000b\\u0000\\u0000\\u0000"
                    + "\\u0010ÿÿexample.com\\u0003\\b\\u0000\\u0000\\u0000\\u0000\\u0000"
                    + "\\u0000deepLink\\u0001\\u0007\\u0000\\u0000\\u00002ÿÿexample\\u0002\\u000b"
                    + "\\u0000\\u0000\\u0000\\u001fÿÿexample.com\\u0003\\u0007\\u0000\\u0000"
                    + "\\u0000\\u0000\\u0000\\u0001another\\u0003\\b\\u0000\\u0000\\u0000\\u0000"
                    + "\\u0000\\u0002deepLink\\u0001\\u0004\\u0000\\u0000\\u00002ÿÿhttp\\u0002"
                    + "\\u000b\\u0000\\u0000\\u0000\\u001fÿÿexample.com\\u0003\\u0007\\u0000"
                    + "\\u0000\\u0000\\u0000\\u0000\\u0003another\\u0003\\b\\u0000\\u0000\\u0000"
                    + "\\u0000\\u0000\\u0004deepLink\\u0001\\u0005\\u0000\\u0000\\u00002ÿÿhttps"
                    + "\\u0002\\u000b\\u0000\\u0000\\u0000\\u001fÿÿexample.com\\u0003\\u0007"
                    + "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0005another\\u0003\\b\\u0000\\u0000"
                    + "\\u0000\\u0000\\u0000\\u0006deepLink\";}\n"
                    + "}"
            ));
  }

  @Test public void testDuplicatedUriMatch() {
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
        + "(UriMatch(uri=airbnb://host/path1/path2?q={q}, matchId=0) vs "
        + "UriMatch(uri=airbnb://host/path1/path2, matchId=1))");
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
            JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.Example."
                    + "SampleModuleLoader",
                "package com.Example;\n"
                    + "\n"
                    + "import com.airbnb.deeplinkdispatch.BaseLoader;\n"
                    + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
                    + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.Collections;\n"
                    + "\n"
                    + "public final class SampleModuleLoader extends BaseLoader {\n"
                    + "  public SampleModuleLoader() {\n"
                    + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
                    + "      new DeepLinkEntry(\"airbnb://example.com/deepLink\", DeepLinkEntry."
                    + "Type.CLASS, SampleActivity.class, null)\n"
                    + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(),"
                    + " } ));\n}\n"
                    + "\n"
                    + "  private static String matchIndex0() {\n"
                    + "    return \"\\u0000\\u0001\\u0000\\u0000\\u00001ÿÿr\\u0001\\u0006\\u0000\\"
                    + "u0000\\u0000#ÿÿairbnb\\u0002\\u000b\\u0000\\u0000\\u0000\\"
                    + "u0010ÿÿexample.com\\u0003\\b\\u0000\\u0000\\u0000\\u0000\\u0000"
                    + "\\u0000deepLink\";}\n"
                    + "}"
            )
        );
  }

  @Test public void testNonStaticMethodCompileFail() {
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

  @Test public void testProcessorWithEmptyCustomPrefixFail() {
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

  @Test public void testProcessorWithEmptyDeepLinkSpecPrefixesFail() {
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

  @Test public void testProcessorWithDeepLinkSortRule() {
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
            JavaFileObjects.forSourceString("/SOURCE_OUTPUT.com.example."
                    + "SampleModuleLoader",
                "package com.example;\n"
                    + "\n"
                    + "import com.airbnb.deeplinkdispatch.BaseLoader;\n"
                    + "import com.airbnb.deeplinkdispatch.DeepLinkEntry;\n"
                    + "import com.airbnb.deeplinkdispatch.base.Utils;\n"
                    + "import java.lang.String;\n"
                    + "import java.util.Arrays;\n"
                    + "import java.util.Collections;\n"
                    + "\n"
                    + "public final class SampleModuleLoader extends BaseLoader {\n"
                    + "  public SampleModuleLoader() {\n"
                    + "    super(Collections.unmodifiableList(Arrays.<DeepLinkEntry>asList(\n"
                    + "      new DeepLinkEntry(\"airbnb://host/path1/path3?q={q}\", DeepLinkEntry."
                    + "Type.METHOD, SampleActivity.class, \"intentFromTwoPathWithQuery\"),\n"
                    + "      new DeepLinkEntry(\"airbnb://host/path1/path2\", DeepLinkEntry.Type."
                    + "METHOD, SampleActivity.class, \"intentFromTwoPath\"),\n"
                    + "      new DeepLinkEntry(\"airbnb://host/{var1}/{var2}\", DeepLinkEntry.Type."
                    + "METHOD, SampleActivity.class, \"intentFromTwoPathWithTwoParams\"),\n"
                    + "      new DeepLinkEntry(\"airbnb://host/path\", DeepLinkEntry.Type.METHOD, "
                    + "SampleActivity.class, \"intentFromOnePath\"),\n"
                    + "      new DeepLinkEntry(\"airbnb://host/{var}\", DeepLinkEntry.Type.METHOD, "
                    + "SampleActivity.class, \"intentFromOnePathWithOneParam\")\n"
                    + "    )), Utils.readMatchIndexFromStrings( new String[] {matchIndex0(), }"
                    + " ));\n}\n"
                    + "\n"
                    + "  private static String matchIndex0() {\n"
                    + "    return \"\\u0000\\u0001\\u0000\\u0000\\u0000yÿÿr\\u0001\\u0006\\u0000"
                    + "\\u0000\\u0000kÿÿairbnb\\u0002\\u0004\\u0000\\u0000\\u0000_ÿÿhost\\u0003"
                    + "\\u0005\\u0000\\u0000\\u0000\\u001aÿÿpath1\\u0003\\u0005\\u0000\\u0000"
                    + "\\u0000\\u0000\\u0000\\u0000path3\\u0003\\u0005\\u0000\\u0000\\u0000"
                    + "\\u0000\\u0000\\u0001path2\\u0003\\u0007\\u0000\\u0000\\u0000\\u000fÿÿ"
                    + "\\u001a{var1}\\u0003\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0002"
                    + "\\u001a{var2}\\u0003\\u0004\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003path"
                    + "\\u0003\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0004\\u001a{var}\";}\n"
                    + "}"));
  }

  @Test public void testNonAppCompatTaskStackBuilderClassErrorMessage() {
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
}
