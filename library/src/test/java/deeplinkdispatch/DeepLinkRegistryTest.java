package deeplinkdispatch;

import com.airbnb.deeplinkdispatch.DeepLinkEntry;
import com.airbnb.deeplinkdispatch.DeepLinkRegistry;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DeepLinkRegistryTest {

  @Test public void testSingleParameter() {
    String uriWithParams = "airbnb://test/{param1}";

    DeepLinkRegistry registry = new DeepLinkRegistry(null);
    registry.registerDeepLink(uriWithParams, DeepLinkEntry.Type.CLASS, "TestActivity", null);

    String uri = "airbnb://test/12345";
    DeepLinkEntry entry = registry.parseUri(uri);
    Assert.assertNotNull(entry);

    Map<String, String> parameters = entry.getParameters(uri);
    assertThat(parameters.get("param1")).isEqualTo("12345");
  }

  @Test public void testTwoParameters() {
    String uriWithParams = "airbnb://test/{param1}/{param2}";

    DeepLinkRegistry registry = new DeepLinkRegistry(null);
    registry.registerDeepLink(uriWithParams, DeepLinkEntry.Type.CLASS, "TestActivity", null);

    String uri = "airbnb://test/12345/alice";
    DeepLinkEntry entry = registry.parseUri(uri);
    Assert.assertNotNull(entry);

    Map<String, String> parameters = entry.getParameters(uri);
    assertThat(parameters.get("param1")).isEqualTo("12345");
    assertThat(parameters.get("param2")).isEqualTo("alice");
  }
}
