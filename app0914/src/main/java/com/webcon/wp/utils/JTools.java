package com.webcon.wp.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * JAVA数据类型和二进制转换的工具类
 * 
 * @author Vieboo
 * 
 */
public class JTools {
	private static boolean NEEDTRANS = false; // Language character convert
	private static boolean DEBUG = false;// Debug info output
	private static boolean EDITFORBIDDEN = false;// Form read only

	public static short Bytes2ToShort(byte mybytes[], int nOff) {
		return (short) ((0xff & mybytes[nOff]) << 8 | 0xff & mybytes[nOff + 1]);
	}

	public static int Bytes4ToInt(byte mybytes[], int nOff) {
		try {
			return (0xff & mybytes[nOff]) << 24 | (0xff & mybytes[nOff + 1]) << 16
					| (0xff & mybytes[nOff + 2]) << 8 | 0xff & mybytes[nOff + 3];
		} catch (Exception e) {
			return 0;    // return 0--> 处理失败
		}
	}

	public static void ShortToBytes2(short i, byte mybytes[], int nOff) {
		mybytes[nOff + 1] = (byte) (0xff & i);
		mybytes[nOff] = (byte) ((0xff00 & i) >> 8);
	}

	/*
	 * 32 Integer to byte array every 8 bits
	 */
	public static void IntToBytes4(int i, byte mybytes[], int nOff) {

		mybytes[nOff + 3] = (byte) (0xff & i);
		mybytes[nOff + 2] = (byte) ((0xff00 & i) >> 8);
		mybytes[nOff + 1] = (byte) ((0xff0000 & i) >> 16);
		mybytes[nOff] = (byte) (int) (((long) 0xff000000 & (long) i) >> 24);
	}

	/**
	 * Integer >> to get ip address
	 * 
	 * @param a
	 *            int
	 * @return String
	 */
	public static String int4ToIP(int a) {
		return ((a & 0xff000000) >>> 24) + "." + ((a & 0x00ff0000) >>> 16)
				+ "." + ((a & 0x0000ff00) >>> 8) + "." + (a & 0x000000ff);
	}

	/**
	 * Integer radio flag
	 * 
	 * @param a
	 *            int
	 * @param nLen
	 *            int
	 * @return String
	 */
	public static String int2Bitmap(int a, int nLen) {
		if (nLen <= 0 || a <= 0)
			return "0";

		String str = Integer.toBinaryString(a);
		int str_len = str.length();

		if (str_len > nLen)
			return "0";

		while (str_len < nLen) {
			str = "0" + str;
			str_len++;
		}

		if (str_len > nLen) {
			str = str.substring(str_len - nLen, str_len);
		}

		return (str);
	}

	public static String tranStr(String str) throws Exception {
		if (NEEDTRANS)
			return (new String(str.getBytes("iso8859-1"), "GBK"));
		else
			return str;
	}

	public static String now2str() {
		return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new java.util.Date());
	}

	public static boolean isNeedTrans() {
		return NEEDTRANS;
	}

	public static boolean isDebug() {
		return DEBUG;
	}

	public static boolean isEditForbidden() {
		return EDITFORBIDDEN;
	}

	public static void log(String str) {
		System.out.println(str);
	}

	public static void logInOneLine(String str) {
		System.out.print(str);
	}

	public static void printbyte(byte[] bt, int len)

	{
		String a = "";
		String b = "";
		if (len > bt.length || len < 1)
			len = bt.length;
		for (int i = 0; i < len; i++) {
			int hex = (int) bt[i] & 0xff;
			b = Integer.toHexString(hex);
			if (b.length() == 1)
				b = "0" + b;
			a += b + " ";
			if (i == 0)
				continue;
			if ((i + 1) % 8 == 0)
				a += "     ";
			if (((i + 1)) % 16 == 0)
				a += "\n";
		}
		log(a);
	}

	public static String seprateFormat(String a, int pernum)

	{
		int i = 0;
		String b = "";
		// System.out.println(a.length());
		// System.out.println(a.substring(24,8));
		while (i < a.length()) {
			if (i < 0)
				break;
			// System.out.println(i);
			b = b + " " + a.substring(i, i + pernum);
			i += pernum;
		}
		return b;

	}

	public static void main(String[] args) {
		if (args[0].equals("help"))
			System.out
					.println("usage: java -cp ./ com.webcon.util.JTools.int2Bitmap YourInt bitlength");
		else
			System.out.println(int2Bitmap(Integer.parseInt(args[0]),
					Integer.parseInt(args[1])));
	}

	/**
	 * byte array =>String ends with '\0'
	 * 
	 * @param aySource
	 *            byte[]
	 * @param nOff
	 *            int
	 * @return String
	 */
	public static String toString(byte[] aySource, int nOff) throws Exception {
		int i = nOff;
		while (i < aySource.length && aySource[i] != 0)
			i++;

		if (ParseConfig.getEncodingCode() != null
				&& (!ParseConfig.getEncodingCode().equals(""))) {
			return new String(aySource, nOff, i - nOff,
					ParseConfig.getEncodingCode());
		} else {
			return new String(aySource, nOff, i - nOff);
		}

	}

	/**
	 * get(0)-->String get(1)-->offset
	 * 
	 * @param aySource
	 * @param nOff
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static List toStringList(byte[] aySource, int nOff, String code)
			throws Exception {
		List list = new ArrayList();
		int i = nOff;
		while (i < aySource.length && aySource[i] != 0)
			i++;
		if (ParseConfig.getEncodingCode() != null
				&& (!ParseConfig.getEncodingCode().equals(""))) {
			list.add(new String(aySource, nOff, i - nOff, ParseConfig
					.getEncodingCode()));
			list.add(i + 1);
			return list;
		} else {
			list.add(new String(aySource, nOff, i - nOff, code));
			list.add(i + 1);
			return list;
		}

	}
}
