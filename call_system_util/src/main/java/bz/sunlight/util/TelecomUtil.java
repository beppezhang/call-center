package bz.sunlight.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TelecomUtil {

	private static final String TOKEN_URL="http://116.228.236.206:8081/DP/getToken/apiKey/projectEncrypt/sign";
	
	public static String getToken(String apiKey,String projectEncrypt,String sign){
		TOKEN_URL.replaceAll("apiKey", apiKey);
		TOKEN_URL.replaceAll("projectEncrypt", PropertiesUtil.getInstanse().getString("projectEncrypt"));
		TOKEN_URL.replaceAll("sign", sign);
		String json=null;
		json = HttpUtil.doGet(TOKEN_URL, null);
		return json;
	}
	
	public static void main(String[] args) {
//		String str=PropertiesUtil.getInstanse().getString("projectEncrypt");
//		System.out.println(str);
//		解析json字符串
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map<String, String> map=new HashMap<String, String>();
		map.put("name", "beppe");
		map.put("city","shanghai");
		list.add(map);
//		String json=JSONObject.fromObject(map).toString();
//		JSONArray arr = JSONArray.fromObject(list);
		String json = GsonUtil.toJson(list);
		List<Map> fromJson = GsonUtil.fromJson(json, List.class);
		for (Map m : fromJson) {
			System.out.println(m.get("name"));
		}
		System.out.println(fromJson);
//		JSONObject obj = JSONObject.fromObject(json);
//		System.out.println(arr);
//		List<Map<String,String>> list1=new ArrayList<Map<String,String>>();
//		for(int i=0;i<arr.size();i++){
//			Map<String, String> map1=new HashMap<String, String>();
//			JSONObject object = (JSONObject)arr.get(i);
//			 for (Iterator<?> iter = object.keys(); iter.hasNext();)
//	            {
//	                String key = (String) iter.next();
//	                String value = object.get(key).toString();
//	                map1.put(key, value);
//	            }
//			 list1.add(map1);
//			System.out.println(list1);
//		}
	}
}
