package bz.sunlight.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;




public class HttpUtil {

	/**
	 * get请求
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
//		httpGet.addHeader("X-HTTP-SEQID", "123456");
		JSONObject jsonObject = null;
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param outStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpost.setEntity(new StringEntity(outStr,"UTF-8"));
		HttpResponse response = client.execute(httpost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.fromObject(result);
		return jsonObject;
	}
	
	public static String doGet(String url, String queryString) { 
        String response = null; 
        HttpClient client = new HttpClient(); 
        HttpMethod method = new GetMethod(url); 
        method.addRequestHeader("X-HTTP-SEQID", "123456");
        try { 
                if (StringUtils.isNotBlank(queryString)) 
                        method.setQueryString(URIUtil.encodeQuery(queryString)); 
                client.executeMethod(method); 
                if (method.getStatusCode() == HttpStatus.SC_OK) { 
                        response = method.getResponseBodyAsString(); 
                } 
        } catch (URIException e) { 
//                log.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e); 
        	e.printStackTrace();
        } catch (IOException e) { 
//                log.error("执行HTTP Get请求" + url + "时，发生异常！", e); 
        	e.printStackTrace();
        } finally { 
                method.releaseConnection(); 
        } 
        return response; 
} 
	
	 /** 
     * 执行一个HTTP POST请求，返回请求响应的HTML 
     * 
     * @param url        请求的URL地址 
     * @param params 请求的查询参数,可以为null 
     * @return 返回请求响应的HTML 
     */ 
    public static String doPost(String url, Map<String, String> params) { 
            String response = null; 
            HttpClient client = new HttpClient(); 
            HttpMethod method = new PostMethod(url); 
            method.addRequestHeader("X-HTTP-SEQID", "123456");
            for (Iterator it = params.entrySet().iterator(); it.hasNext();) { 

            } 
            //设置Http Post数据 
            if (params != null) { 
                    HttpMethodParams p = new HttpMethodParams(); 
                    for (Map.Entry<String, String> entry : params.entrySet()) { 
                            p.setParameter(entry.getKey(), entry.getValue()); 
                    } 
                    method.setParams(p); 
            } 
            try { 
                    client.executeMethod(method); 
                    if (method.getStatusCode() == HttpStatus.SC_OK) { 
                            response = method.getResponseBodyAsString(); 
                    } 
            } catch (IOException e) { 
//                    log.error("执行HTTP Post请求" + url + "时，发生异常！", e); 
            	e.printStackTrace();
            } finally { 
                    method.releaseConnection(); 
            } 

            return response; 
    } 

    public static String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        String localIP = "127.0.0.1";

        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if ((ip == null) || (ip.length() == 0) || (ip.equalsIgnoreCase(localIP)) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    } 
    
    public static void main(String[] args) throws ParseException, IOException {
//    	JSONObject doGetStr = doGetStr("http://localhost:8080/data/data");
    	String doGet = doGet("http://localhost:8080/data/data", null);
    	System.out.println(doGet);
	}
}
