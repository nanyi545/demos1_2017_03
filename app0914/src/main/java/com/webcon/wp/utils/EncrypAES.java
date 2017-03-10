package com.webcon.wp.utils;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES数据加密工具类
 * 
 * @author Vieboo
 * 
 */
public class EncrypAES {
	
	private static EncrypAES aes;
	private final static String HEX = "0123456789ABCDEF";
	private final int JELLY_BEAN_4_2 = 17;	//Android 4.2系统
	private Cipher enCipher, deCipher;
	private SecretKeySpec secKeySpec;
	
	
	private EncrypAES(){
		try {
			secKeySpec = new SecretKeySpec(getRawKey(), "AES");
			
			enCipher = Cipher.getInstance("AES");
			enCipher.init(Cipher.ENCRYPT_MODE, secKeySpec);
			
			deCipher = Cipher.getInstance("AES");
			deCipher.init(Cipher.DECRYPT_MODE, secKeySpec);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static EncrypAES getInstance(){
		if(aes == null){
			synchronized (EncrypAES.class) {
				if(aes == null){
					aes = new EncrypAES();
				}
			}
		}
		return aes;
	}

	/**
	 * 根据字符密密码生成密钥key
	 * param seed	字符密码
	 * @return	密钥
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException 
	 */
	public byte[] getRawKey() throws NoSuchAlgorithmException, NoSuchProviderException{
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		SecureRandom secRandom = null;
		//Android 4.2及以上版本加密的兼容
		//if(android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_4_2){
			secRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		//}else{
		//	secRandom = SecureRandom.getInstance("SHA1PRNG");
		//}
		secRandom.setSeed(WPConstant.AES_ENCRYPT_KEY.getBytes());
		keyGen.init(128, secRandom);	// 192 and 256 bits may not be available
		SecretKey key = keyGen.generateKey();
		return key.getEncoded();
	}
	
	
	
	/**
	 * 加密数据
	 * @param clear	需要加密的字符串
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public String encrypt(String clear) {
		 String str = "";
		try {
			byte[] result = enCipher.doFinal(clear.getBytes());
			str = toHex(result);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * 解密数据
	 * @param crypted	需要解密的字符串
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public String decrypt(String crypted){
		String str = "";
		try{
			byte[] result = deCipher.doFinal(toByte(crypted));
			str = new String(result);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}
	
	
	/**
	 * 将加密后的乱码字符转化为16进制的数据
	 * @param buf
	 * @return
	 */
	public static String toHex(byte[] buf) {   
        if (buf == null)   
            return "";   
        StringBuffer result = new StringBuffer(2*buf.length);   
        for (byte b : buf) {
            appendHex(result, b);
        }   
        return result.toString();   
    }
	
	/**
	 * 分离每个字节的前四位和后四位
	 * @param sb
	 * @param b
	 */
	private static void appendHex(StringBuffer sb, byte b) {   
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));   
    }
	
	
	/**
	 * 将转化后的16进制数据还原成加密后的乱码字符
	 * @param hexString
	 * @return
	 */
	public static byte[] toByte(String hexString) {   
        int len = hexString.length()/2;   
        byte[] result = new byte[len];   
        for (int i = 0; i < len; i++)   
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();   
        return result;   
    }
	
}
