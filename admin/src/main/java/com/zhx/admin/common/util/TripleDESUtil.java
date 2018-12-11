package com.zhx.admin.common.util;

public class TripleDESUtil {

	//	public static String key = PropertiesUtil.getValue("THREEDESKEY");
	public static String key = "asdsaffjhdfgdbv^&%^**hhjad";
	/**
	 * 3DES加密
	 *
	 * @param value
	 * @return
	 */
	public static String getEncryptString(String value,String key) {
		return  TripleDES.encrypt(key, value).replace("=","");
	}
	/**
	 * 3DES解密
	 *
	 * @param value
	 * @return
	 */
	public static String decryptString(String value,String key) {
		return  TripleDES.decrypt(key, value);
	}

	/**
	 * 3DES加密 带Base64
	 *
	 * @param value
	 * @return
	 */
	public static String getEncryptStringIncludeBase64(String value,String key) {
		value = Base64Utils.getBase64(value);
		value = TripleDES.encrypt(key, value);
//		System.out.println("生成密文:" + value);
		return value.replace("=","");
	}

	/**
	 * 3DES解密 带Base64
	 *
	 * @param str
	 * @return
	 */
	public static String decryptDataIncludeBase64(String str,String key) {
		if (str == null) {
			str = "";
		}
		str = str.replaceAll(" ", "+");
		return Base64Utils.getFromBase64(TripleDES.decrypt(key, str));
	}

	public static void main(String[] args) {
		String key = "hsfsjdaftertkjerlkgjdfkg";
		System.out.println("*******************BASE64****************************");
		System.out.println(Base64Utils.getBase64("我的12312314"));
		System.out.println(Base64Utils.getFromBase64(Base64Utils.getBase64("我的12312314")));
		System.out.println("*******************3des****************************");
		System.out.println(TripleDESUtil.getEncryptString("我的12312314",key));
		System.out.println(TripleDESUtil.decryptString(TripleDESUtil.getEncryptString("我的12312314",key), key));
		System.out.println("*******************3des+Base64****************************");
		System.out.println(TripleDESUtil.getEncryptStringIncludeBase64("我的12312314",key));
		System.out.println(TripleDESUtil.decryptDataIncludeBase64(TripleDESUtil.getEncryptStringIncludeBase64("我的12312314",key),key));
	}
}
