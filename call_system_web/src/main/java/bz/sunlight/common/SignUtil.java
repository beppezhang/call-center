package bz.sunlight.common;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

//   电信加密算法
public class SignUtil {

	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

	public static String sign(String secretKey, String data) throws Exception {
		SecretKey specsigningKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(specsigningKey);
		byte[] rawHmac = mac.doFinal(data.getBytes());
		return org.apache.commons.codec.binary.Hex.encodeHexString(rawHmac);
	}

}
