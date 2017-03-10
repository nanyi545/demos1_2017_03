package com.webcon.sus.http;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * json数据格式解析
 * @author Vieboo
 *
 */
public class JsonResolve {

	/**
	 * 解析为json格式
	 * @throws JSONException 
	 */
	public JSONObject resolveJsonToJsonObject(String json, String key) throws JSONException{
		JSONObject jsonObject = new JSONObject(json);
		return jsonObject.getJSONObject(key);
	}
	
	/**
	 * 讲jsonObject解析为相应格式
	 * @throws JSONException 
	 */
	public Object resolveJsonToObject(JSONObject jsonObject, String key) throws JSONException{
		return jsonObject.get(key);
	}
	
}
