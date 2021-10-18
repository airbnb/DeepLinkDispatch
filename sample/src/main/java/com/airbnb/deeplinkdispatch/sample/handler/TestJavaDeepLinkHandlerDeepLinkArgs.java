package com.airbnb.deeplinkdispatch.sample.handler;

import com.airbnb.deeplinkdispatch.handler.DeepLinkParamType;
import com.airbnb.deeplinkdispatch.handler.DeeplinkParam;

public class TestJavaDeepLinkHandlerDeepLinkArgs {

  private final long uuid;
  private final String name;
  private final float number;
  private final boolean flag;
  private final Boolean showTaxes;
  private final Integer queryParam;

  public TestJavaDeepLinkHandlerDeepLinkArgs(
    // path params can be non null
    @DeeplinkParam(name = "path_segment_variable_1", type = DeepLinkParamType.Path) long uuid,
    @DeeplinkParam(name = "path_segment_variable_2", type = DeepLinkParamType.Path) String name,
    @DeeplinkParam(name = "path_segment_variable_3", type = DeepLinkParamType.Path) float number,
    @DeeplinkParam(name = "path_segment_variable_4", type = DeepLinkParamType.Path) boolean flag,
    @DeeplinkParam(name = "show_taxes", type = DeepLinkParamType.Query) Boolean showTaxes,
    @DeeplinkParam(name = "queryParam", type = DeepLinkParamType.Query) Integer queryParam
  ) {
    this.uuid = uuid;
    this.name = name;
    this.number = number;
    this.flag = flag;
    this.showTaxes = showTaxes;
    this.queryParam = queryParam;
  }

  @Override
  public String toString() {
    return "TestJavaDeepLinkHandlerDeepLinkArgs{" +
      "uuid=" + uuid +
      ", name='" + name + '\'' +
      ", number=" + number +
      ", flag=" + flag +
      ", showTaxes=" + showTaxes +
      ", queryParam=" + queryParam +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TestJavaDeepLinkHandlerDeepLinkArgs that = (TestJavaDeepLinkHandlerDeepLinkArgs) o;

    if (uuid != that.uuid) return false;
    if (Float.compare(that.number, number) != 0) return false;
    if (flag != that.flag) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (showTaxes != null ? !showTaxes.equals(that.showTaxes) : that.showTaxes != null)
      return false;
    return queryParam != null ? queryParam.equals(that.queryParam) : that.queryParam == null;
  }

  @Override
  public int hashCode() {
    int result = (int) (uuid ^ (uuid >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (number != +0.0f ? Float.floatToIntBits(number) : 0);
    result = 31 * result + (flag ? 1 : 0);
    result = 31 * result + (showTaxes != null ? showTaxes.hashCode() : 0);
    result = 31 * result + (queryParam != null ? queryParam.hashCode() : 0);
    return result;
  }
}
