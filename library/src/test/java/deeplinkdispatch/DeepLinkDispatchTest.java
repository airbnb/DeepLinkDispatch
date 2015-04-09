package deeplinkdispatch;

import com.airbnb.deeplinkdispatch.DeepLink;

import junit.framework.TestCase;

public class DeepLinkDispatchTest extends TestCase {

  @DeepLink(uri = "Test URI here")
  private void handleUri() {}

  public void testSimple() {

  }

}
