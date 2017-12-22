package com.eastsoft.esgjyj.util;

/**
 * Title: 公司常用组件 Description: 加密处理工具
 * @author fan zeng liang
 * @version 1.0
 */
public class PassWd
{

	/**
	 * 功能说明: 用给定的密钥加密字符串 <br>
	 * 加密算法： <br>
	 * 1.首先生成一个4位的随机数，作为加密因子1，不足4位者前补0 <br>
	 * 2.将以上的随机数各位求和，结果作为加密因子2 <br>
	 * 3.取原文的每个字节,求其ASCII值,该值与加密因子2相加, <br>
	 * 再与加密因子1中的某位数值相加（加密因子1的各位数字循环使用）, <br>
	 * 4.将以上相加的结果的15进制,转换成ls_hex数组中相应位置的两个字符组成的字符串 <br>
	 * 其他说明：如果需要支持中文字符,至少需将ls_hex[]数组的长度增加到19， <br>
	 * 汉字占两个字节,每个字节的ASCII码值在128-255之间,加密后的值在177-348之间, <br>
	 * 所以汉字的每个字节加密后如果保存成2个字符,最少需要19进制 <br>
	 * @param ps_text
	 *            待加密字串(不支持中文字符)
	 * @param ps_key
	 *            密钥(长度固定,全部由数字组成)
	 * @return 加密后的字符串
	 */
	public String f_encrypt(String ps_text, String ps_key)
	{
		String ls_textencrypt = "" , ls_pass = "";
		int li_textlen = 0 , li_keylen = 0 , li_key2 = 0 , li_key1 = 0;
		int li_loop = 0 , i = 0 , li_keyloop = 0;
		String ls_hex[] =
		{
		        "3", "6", "2", "A", "1", "P", "8", "4", "S", "E", "Z", "9",
		        "7", "5", "C"
		};

		if ( Tools.isEmpty(ps_key) || Tools.isEmpty(ps_text) )
			return "";

		li_keylen = ps_key.length();
		li_textlen = ps_text.length();
		if ( li_textlen < 8 )
		{
			for (int j = 0; j < 8 - li_textlen; j++ )
			{
				ps_text = ps_text + " ";
			}
		}
		li_textlen = ps_text.length();
		li_key2 = 0;
		for (i = 0; i < li_keylen; i++ )
		{
			li_key2 = li_key2 + Integer.parseInt(ps_key.substring(i, i + 1));
		}

		li_keyloop = 0;

		for (li_loop = 0; li_loop < li_textlen; li_loop++ )
		{
			ls_pass = String.valueOf(ps_text.substring(li_loop, li_loop + 1));
			if ( li_keyloop > li_keylen - 1 )
				li_keyloop = 0;

			li_key1 = (char) ( ps_key.charAt(li_keyloop) );
			li_key1 = li_key1 + li_key2 + ls_pass.charAt(0);
			li_keyloop++;

			// 将li_key1(当前字符加密后的值, >96,<221 )转成15进制的两个字符
			i = ( li_key1 % 15 );
			ls_pass = ls_hex[i];
			i = li_key1 / 15;
			ls_pass = ls_hex[i] + ls_pass;

			ls_textencrypt = ls_textencrypt + ls_pass;
		}

		ls_textencrypt = ls_textencrypt + ps_key;
		return ls_textencrypt;
	}

	/**
	 * 功能说明：将字符型转化为整型
	 * @param chTemp
	 *            源字符
	 * @return 数字
	 */
	public int charToInteger(char chTemp)
	{
		char chTemp1 = chTemp;
		int temp = chTemp1;
		return temp;
		// return Integer.parseInt(String.valueOf(chTemp));
	}

	/**
	 * 功能说明：返回四位随机数字的字符串
	 * @return 四位字符串
	 */
	public static String getRandom()
	{
		String strRandom = "";
		int iRandom = (int) ( 10000 * ( Math.random() ) );
		strRandom = String.valueOf(iRandom);
		while (strRandom.length() != 4)
		{
			iRandom = (int) ( 10000 * ( Math.random() ) );
			strRandom = String.valueOf(iRandom);
		}
		return strRandom;
	}

	/**
	 * 功能说明: 获得加密字符串的原文
	 * @param ps_text
	 *            源加密的字符串(该字符串是用函数f_encrypt加密的)
	 * @return <加密前的字串>
	 */
	public String f_decrypt(String ps_text)
	{
		String ls_key = "" , ls_text = "" , ls_char = "" , ls_1 = "" , ls_2 = "" , ls_value = "";
		final int li_keylen = 4; // 密钥的固定长度
		int li_textlen = 0 , i = 0 , j = 0 , li_loop = 0 , li_keyloop = 0;
		int li_key1 = 0 , li_key2 = 0;
		String ls_hex[] =
		{
		        "3", "6", "2", "A", "1", "P", "8", "4", "S", "E", "Z", "9",
		        "7", "5", "C"
		};

		if ( Tools.isEmpty(ps_text) || Tools.isEmpty(ps_text) )
			return "";
		// if(ps_text.length() != 12) return ""; //!!!判断长度是否正确

		ls_key = ps_text.substring(ps_text.length() - li_keylen); // 密钥
		ls_text = ps_text.substring(0, ( ps_text.length() - li_keylen ));
		li_textlen = ( ls_text.length() ) / 2;
		li_key2 = 0;
		for (i = 0; i < li_keylen; i++ )
		{
			// 检查ls_key是否全是数字
			li_key2 = li_key2 + Integer.parseInt(ls_key.substring(i, i + 1));

		}

		li_keyloop = 0;
		for (li_loop = 0; li_loop < li_textlen; li_loop++ )
		{

			i = li_loop * 2;
			if ( i <= 0 )
				i = 0;
			ls_char = ls_text.substring(i, i + 2); // 加密串中每两个字符是原文的一个字符
			ls_1 = ls_char.substring(0, 1);
			ls_2 = ls_char.substring(ls_char.length() - 1);
			for (i = 0; i < 15; i++ )
			{
				if ( ls_1.equals(ls_hex[i]) )
					break;
			}
			// !!! if i > 15 then ps_text参数错误

			for (j = 0; j < 15; j++ )
			{
				if ( ls_2.equals(ls_hex[j]) )
					break;
			}
			// !!! if j > 15 then ps_text参数错误

			i = ( i ) * 15 + j; // 2个加密字符的十进制数值

			// 还原
			if ( li_keyloop > li_keylen - 1 )
				li_keyloop = 0;
			li_key1 = (char) ( ls_key.charAt(li_keyloop) );
			i = i - li_key1 - li_key2;
			ls_value = ls_value + String.valueOf((char) ( i ));
			li_keyloop++;

		}

		return ls_value.trim();

	}

	public static void main(String[] args) {
		PassWd passWd = new PassWd();
		System.out.println(passWd.f_decrypt("S3S6SAS1S3S689872356"));
	}

}
