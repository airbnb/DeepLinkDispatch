package com.airbnb.deeplinkdispatch;

import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;

class BaseDeepLinkProcessorTest {

  protected final JavaFileObject fakeBaseDeeplinkDelegate = JavaFileObjects
    .forSourceString("BaseDeepLinkDelegate", "package com.airbnb.deeplinkdispatch;\n" +
      "\n" +
      "import java.util.List;\n" +
      "import java.util.Map;\n" +
      "\n" +
      "public class BaseDeepLinkDelegate {" +
      "  public BaseDeepLinkDelegate(List<? extends BaseRegistry> registries) {\n" +
      "  }" +
      "\n" +
      "  public BaseDeepLinkDelegate(\n" +
      "    List<? extends BaseRegistry> registries,\n" +
      "    Map<String, String> configurablePathSegmentReplacements\n" +
      "  ) {}\n" +
      "}");

}
