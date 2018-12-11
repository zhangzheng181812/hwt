package com.zhx.hwtcommon.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;

public class TripleDES {
	public static final String CIPHERMODE = "DESede/ECB/PKCS5Padding";
	public static final String ENCODE = "utf-8";

	/**
	 * 初始化密钥
	 * @return
	 * @throws Exception
	 */
	public static String getKeyStr() throws Exception{
		return Base64.encodeBase64String(getKey().getEncoded());
	}

	/**
	 * 获取密钥
	 * @return
	 * @throws Exception
	 */
	public static Key getKey() throws Exception{
		//实例化
		KeyGenerator kg = KeyGenerator.getInstance("DESede");
		kg.init(168);
		//生成密钥
		SecretKey secretKey = kg.generateKey();
		return secretKey;
	}



	public static String encrypt(String key, String src) {
		try {
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(ENCODE));
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CIPHERMODE);
			cipher.init(Cipher.ENCRYPT_MODE, securekey);
			byte[] b = cipher.doFinal(src.getBytes());
			return Base64.encodeBase64String(b).replaceAll("\r", "").replaceAll("\n", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String key, String src) {
		try {
			byte[] bytesrc = Base64.decodeBase64(src);
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes(ENCODE));
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			SecretKey securekey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(CIPHERMODE);
			cipher.init(Cipher.DECRYPT_MODE, securekey);
			byte[] retByte = cipher.doFinal(bytesrc);
			return new String(retByte, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		String key = getKeyStr();
        System.out.println(key);
		String test = TripleDES.encrypt(key, "1232135sdsadsasdshgjhkjhkadsadsdgdfhgdjhkbvnbcnbhgfjhgjhgkhghfdsgfdsgadsadadsad");
		System.out.println(test);
		test = test.replace("=","");
		System.out.println(test);
//		System.out.println(Base64Utils.getBase64(test));
		System.out.println(TripleDES.decrypt(key,test));
	}


}