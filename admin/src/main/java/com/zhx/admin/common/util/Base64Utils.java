package com.zhx.admin.common.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Utils {

	/**
	 * 加密
	 *
	 * @param str
	 * @return
	 */
	public static String getBase64(String str) {
		byte[] b = null;
		String s = null;
		try {
			b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (b != null) {
			s = Base64.encodeBase64String(b);
		}
		return s;
	}

	/**
	 * 解密
	 *
	 * @param s
	 * @return
	 */
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			try {
				b = Base64.decodeBase64(s);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
