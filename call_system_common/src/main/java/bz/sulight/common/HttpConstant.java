package bz.sulight.common;



public class HttpConstant {
	public static final int CODE_400 = 400;
	public static final int CODE_401 = 401;
	public static final int CODE_403 = 403;
	public static final int CODE_404 = 404;
	public static final int CODE_500 = 500;
	public static final String CODE_401_MSG = "请求授权失败--请关注微信在访问系统";
	public static final String CODE_403_MSG = "请求被禁止访问--未验证的销售顾问";	
	public static final String CONTENTTYPE_JSON = "application/json;charset=utf-8";
	public static final String API_USERINFO = "api_user_info_";
	public static final String API_REFRESH_TOKEN = "api_refresh_token";
	public static final String API_AUTHINFO = "api_authinfo_token";
	public static final String ACCESSTOKEN = "AccessToken";
	public static final int EXPIRESUTC = 7;// 天/单位
	public static final int REFRESHTOKENEXPIRESUTC = 2;//天/单位
	public static final int COOOKIEEXPIRESUTC = 24 * 60 * 60 * 700;
	public static final int EXPIRESUTC_SECOND = 24 * 60 * 60 * EXPIRESUTC;
	public static final int REFRESHTOKENEXPIRESUTC_SECOND = 24 * 60 * 60 * REFRESHTOKENEXPIRESUTC;
	public static final String SEG = "_";
	public static final String COOKIE_NAME = "sdt.dmc.cookie";
	public static final String APPOINTMENT_COOKIE_ADMIN = "sdt.dmc.appointment_admin";
	public static final String APPOINTMENT_COOKIE_MOBILE = "sdt.dmc.appointment_mobile";
	public static final String COOKIE_NAME_IDCARD = "sdt.idcard.cookie";
	public static final String OPEN_ID = "open_id";
	public static final String CURRENT_USER = "current_user";
	public static final String API_MSG_401 = "未被授权访问指定资源";
	public static final String API_MSG_403 = "请求的头部未包含 AccessToken，或该用户已被列入黑名单";
	
//	headers = {host:192.168.8.161:8080}
//	headers = {connection:keep-alive}
//	headers = {cache-control:max-age=0}
//	headers = {user-agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36}
//	headers = {accept:image/webp,image/*,*/*;q=0.8}
//	headers = {referer:http://192.168.8.161:8080/sdt-dmc/01.html}
//	headers = {accept-encoding:gzip, deflate, sdch}
//	headers = {accept-language:zh-CN,zh;q=0.8}
//	headers = {cookie:JSESSIONID=BB8B1D547C5F58FA95B58807E01BC167}
	public static final String HOST = "Host";
	public static final String CONNECTION = "Connection";
	public static final String CACHE_CONTROL = "Cache-Control";
	public static final String USER_AGENT = "User-Agent";
	public static final String ACCEPT = "Accept";
	public static final String REFERER = "Referer";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	public static final String COOKIE = "Cookie";
	
	
}
