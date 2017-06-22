package bz.sunlight.util;

import org.apache.commons.codec.binary.Base64;

public class EncryptUtil {

	public static String base64En(String source){
        Base64 base64 = new Base64();
        byte[] encode = base64.encode(source.getBytes());
        return new String(encode);
    }
	
	public static void main(String[] args) {
		String base64En = base64En("上海是大城市，吗");
		System.out.println(base64En.length());
	}
}
