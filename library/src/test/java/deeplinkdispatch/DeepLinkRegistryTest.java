package deeplinkdispatch;

import com.airbnb.deeplinkdispatch.DeepLinkRegistry;
import com.airbnb.deeplinkdispatch.internal.DeepLinkEntry;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class DeepLinkRegistryTest {

  @Test public void testSingleParameter() {
    String uriWithParams = "airbnb://test/{param1}";

    DeepLinkRegistry registry = new DeepLinkRegistry(null);
    registry.registerDeepLink(uriWithParams, DeepLinkEntry.Type.CLASS, "TestActivity", null);

    String uri = "airbnb://test/12345";
    DeepLinkEntry entry = registry.parseUri(uri);
    Assert.assertNotNull(entry);

    Map<String, String> parameters = entry.getParameters(uri);
    Assert.assertEquals(parameters.get("param1"), "12345");
  }

  @Test public void testTwoParameters() {
    String uriWithParams = "airbnb://test/{param1}/{param2}";

    DeepLinkRegistry registry = new DeepLinkRegistry(null);
    registry.registerDeepLink(uriWithParams, DeepLinkEntry.Type.CLASS, "TestActivity", null);

    String uri = "airbnb://test/12345/alice";
    DeepLinkEntry entry = registry.parseUri(uri);
    Assert.assertNotNull(entry);

    Map<String, String> parameters = entry.getParameters(uri);
    Assert.assertEquals(parameters.get("param1"), "12345");
    Assert.assertEquals(parameters.get("param2"), "alice");
  }
}
