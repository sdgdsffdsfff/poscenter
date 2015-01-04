package mmboa.util;

import java.security.MessageDigest;

/**
 * @author zhoujun
 * 对密码进行加密等
 */
public final class Secure {
	
	// 将用户密码加密后用于保存
	public static String encryptPwd(String pwd) {
		return md5x("MMB_POS_ADMIN_5t4r7uX-[';/v3" + pwd);
	}
	
	public static MessageDigest digest = null;
	static {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch(Exception e) {e.printStackTrace();}
	}
	
	// md5摘要后用base64x做成字符串
	public static synchronized String md5x(String src) {
		try {
			digest.update(src.getBytes("UTF-8"));
		} catch (java.io.UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		byte[] rawData = digest.digest();
		byte[] encoded = Base64x.encode(rawData);
		String retValue = new String(encoded);
		return retValue;
	}
}
