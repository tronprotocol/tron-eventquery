package org.tron.trongeventquery.response;

import com.alibaba.fastjson.JSONObject;


public class Response {
  public Response(boolean result, String msg) {
    jsonObject = new JSONObject();
    jsonObject.put("success", result);
    jsonObject.put("error", msg);
  }

  public JSONObject toJSONObject() {
    return jsonObject;
  }

  private JSONObject jsonObject;

}