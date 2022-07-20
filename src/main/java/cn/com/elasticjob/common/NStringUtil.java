package cn.com.elasticjob.common;


import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 *
 * @ClassName:     NStringUtil
 * @Description:   String处理帮忙类
 *
 * @company        XXXXX股份有限公司
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2021年02月06日 14:54:59
 */
public class NStringUtil {
	//定义类的输出
	private static final Logger logger = LoggerFactory.getLogger(NStringUtil.class);

	private static Pattern blankPattern = Pattern.compile("\\t|\\r|\\n|\\r\\n");
	private static Pattern numericPattern = Pattern.compile("^[0-9\\-]+$");
	private static Pattern numeric2Pattern = Pattern.compile("^[0-9]+$");
	private static Pattern numericStringPattern = Pattern.compile("^[0-9\\-\\-]+$");

	//浮点数值
	private static Pattern floatNumericPattern1 = Pattern.compile("^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
	private static Pattern floatNumericPattern2 = Pattern.compile("^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$");
	//科学计算法
	private static Pattern scientificNumPattern1 = Pattern.compile("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");
	private static Pattern scientificNumPattern2 = Pattern.compile("^((-?\\d+.?\\d*)[Ee]{1}(\\+?\\d+))$");

	private static Pattern abcPattern = Pattern.compile("^[a-z|A-Z]+$");
	public static final String splitStrPattern = ",|，|;|；|、|\\.|。|-|_|\\(|\\)|\\[|\\]|\\{|\\}|\\\\|/| |　|\"";

	private static Pattern htmlPattern = Pattern.compile("\\s*<.*?>\\s*", Pattern.DOTALL
			| Pattern.MULTILINE | Pattern.CASE_INSENSITIVE); // \\s?[s|Sc|Cr|Ri|Ip|Pt|T]

	private static Pattern messyCodePattern = Pattern.compile("\\s*|\t*|\r*|\n*");


	/**
	 * 去除字符串中的回车、换行符、制表符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str){
		if(null==str || str.length()==0){
			return str;
		}

		try{
			String dest = str;
			Matcher m = blankPattern.matcher(str);
			while(m.find()){
				//将(\t|\r|\n|\r\n)替换成" "
				dest = m.replaceAll(" ");
			}
			return dest;
		}catch(Exception ex){
			//ex
		}
		return str;
	}

	/**
	 * 判断是否数字表示
	 *
	 * @param src
	 *            源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isNumeric(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numericPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}
	/**
	 * 判断是否数字表示
	 *
	 * @param src
	 *            源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isNumericString(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numericStringPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}


	/**
	 * 判断是否浮点数字（科学计数法）表示
	 *
	 * @param src
	 *            源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isFloatNumeric(String src) {
		boolean return_value = false;
		if (src != null && StringUtils.isNotBlank(src)) {
			src = src.trim();
			if(src.startsWith("+")){
				src = src.substring(1, src.length());
			}

			//暂时简单判断，后续改造
			if ( "0".equals(src) || "0.0".equals(src) ||  "0.00".equals(src) || "0.000".equals(src) || "0.0000".equals(src)) {
				return true;
			}

			// 浮点数值
			Matcher m1 = floatNumericPattern1.matcher(src);
			Matcher m2 = floatNumericPattern2.matcher(src);
			if (m1.matches() || m2.matches()) {
				return_value = true;
			} else{
				// 科学计算法
				Matcher m13 = scientificNumPattern1.matcher(src);
				Matcher m14 = scientificNumPattern2.matcher(src);
				if(m13.matches() || m14.matches()){
					return_value = true;
				}
			}
		}
		return return_value;
	}


	/**
	 * 将浮点数字字符串（科学计数法），保留小数位数，返回float
	 *
	 * @param value
	 * @param round
	 * @return
	 *     double
	 */
	public static float numberFloatRound(String value, int round) {
		if(null==value || StringUtils.isBlank(value)){
			return 0;
		}

		if(round<0 || round > 8){
			round = 2;
		}

		value = value.trim();
		try{
			if(!isFloatNumeric(value)){
				return ConvertUtil.Obj2Float(value);
			}

			// 保留小数
			BigDecimal bg = new BigDecimal(value);
			bg = bg.setScale(round, RoundingMode.HALF_UP);

			// 去字符串，再转换double
			BigDecimal bg2 = new BigDecimal(bg.toPlainString());
			return bg2.floatValue();
		}catch (Exception ex){
			return ConvertUtil.Obj2Float(value);
		}
	}

	/**
	 * 将浮点数字字符串（科学计数法），保留小数位数，返回double
	 *
	 * @param value
	 * @param round
	 * @return
	 *     double
	 */
	public static double floatNumberKeepRound(String value, int round) {
		if(null==value || StringUtils.isBlank(value)){
			return 0;
		}

		if(round<0 || round > 8){
			round = 2;
		}

		value = value.trim();
		try{
			if(!isFloatNumeric(value)){
				return ConvertUtil.Obj2Double(value);
			}

			// 保留小数
			BigDecimal bg = new BigDecimal(value);
			bg = bg.setScale(round, RoundingMode.HALF_UP);

			// 去字符串，再转换double
			BigDecimal bg2 = new BigDecimal(bg.toPlainString());
			return bg2.doubleValue();
		}catch (Exception ex){
			return ConvertUtil.Obj2Double(value);
		}
	}


	/**
	 * 将浮点数字字符串（科学计数法），保留小数位数，返回数值字符串
	 *
	 * @param value
	 * @param round
	 * @return
	 *      String
	 */
	public static String floatNumberStrKeepRound(String value, int round) {
		if(null==value || StringUtils.isBlank(value)){
			return value;
		}

		if(round<0 || round > 8){
			round = 2;
		}

		value = value.trim();
		try{
			if(!isFloatNumeric(value)){
				return value;
			}

			BigDecimal bg = new BigDecimal(value);
			bg = bg.setScale(round, RoundingMode.HALF_UP);
			return bg.toPlainString();
		}catch (Exception ex){
			return value;
		}
	}


	/**
	 * 判断是否纯字母组合
	 *
	 * @param src
	 *            源字符串
	 * @return 是否纯字母组合的标志
	 */
	public static boolean isABC(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = abcPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}


	/**
	 * 把string array or list用给定的符号symbol连接成一个字符串
	 *
	 * @param array
	 * @param symbol
	 * @return
	 */
	public static String joinString(List array, String symbol) {
		String result = "";
		if (array != null) {
			for (int i = 0; i < array.size(); i++) {
				String temp = array.get(i).toString();
				if (temp != null && temp.trim().length() > 0) {
					result += (temp + symbol);
				}
			}
			if (result.length() > 1) {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	/**
	 * 截取字符串　超出的字符用symbol代替 　　
	 *
	 * @param len
	 *            　字符串长度　长度计量单位为一个GBK汉字　　两个英文字母计算为一个单位长度
	 * @param str
	 * @param symbol
	 * @return
	 */
	public static String getLimitLengthString(String str, int len, String symbol) {
		int iLen = len * 2;
		int counterOfDoubleByte = 0;
		String strRet = "";
		try {
			if (str != null) {
				byte[] b = str.getBytes("GBK");
				if (b.length <= iLen) {
					return str;
				}
				for (int i = 0; i < iLen; i++) {
					if (b[i] < 0) {
						counterOfDoubleByte++;
					}
				}
				if (counterOfDoubleByte % 2 == 0) {
					strRet = new String(b, 0, iLen, "GBK") + symbol;
					return strRet;
				} else {
					strRet = new String(b, 0, iLen - 1, "GBK") + symbol;
					return strRet;
				}
			} else {
				return "";
			}
		} catch (Exception ex) {
			return str.substring(0, len);
		} finally {
			strRet = null;
		}
	}

	/**
	 * 检查数据串中是否包含非法字符集
	 *
	 * @param str
	 * @return [true]|[false] 包含|不包含
	 */
	public static boolean check(String str) {
		String sIllegal = "'\"";
		int len = sIllegal.length();
		if (null == str) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (str.indexOf(sIllegal.charAt(i)) != -1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 随即生成指定位数的含验证码字符串
	 *
	 * @author Peltason
	 * @date 2007-5-9
	 * @param bit
	 *            指定生成验证码位数
	 * @return String
	 */
	public static String random(int bit) {
		if (bit == 0) {
			bit = 6; // 默认6位
		}

		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		String str = "";
		str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";// 初始化种子
		return RandomStringUtils.random(bit, str);// 返回6位的字符串
	}

	/**
	 * 检查字符串是否属于手机号码
	 *
	 * @author Peltason
	 * @date 2007-5-9
	 * @param str
	 * @return boolean
	 */
	public static boolean isMobile(String str) {
		if (str == null || "".equals(str)) {
			return false;
		}
		if (str.length() != 11 || !isNumeric(str)) {
			return false;
		}
		if (!"13".equals(str.substring(0, 2))
				&& !"15".equals(str.substring(0, 2))
				&& !"18".equals(str.substring(0, 2))) {
			return false;
		}
		return true;
	}

	/**
	 * 取得字符串的实际长度（考虑了汉字的情况）
	 *
	 * @param SrcStr
	 *            源字符串
	 * @return 字符串的实际长度
	 */
	public static int getStringLen(String SrcStr) {
		int return_value = 0;
		if (SrcStr != null) {
			char[] theChars = SrcStr.toCharArray();
			for (int i = 0; i < theChars.length; i++) {
				return_value += (theChars[i] <= 255) ? 1 : 2;
			}
		}
		return return_value;
	}


	/**
	 * Ascii转为Char
	 *
	 * @author 甜瓜
	 * @param asc
	 * @return
	 */
	public static String AsciiToChar(int asc) {
		String TempStr = "A";
		char tempchar = (char) asc;
		TempStr = String.valueOf(tempchar);
		return TempStr;
	}

	/**
	 * 判断是否是空字符串 null和"" 都返回 true
	 *
	 * @author Robin Chang
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (s != null && !"".equals(s)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否是空字符串 null和"" null返回result,否则返回字符串
	 *
	 * @param s
	 * @return
	 */
	public static String isEmpty(String s, String result) {
		if (s != null && !"".equals(s)) {
			return s;
		}
		return result;
	}
	/**
	 * 移除html标签
	 *
	 * @param htmlstr
	 * @return
	 */
	public static String removeHtmlTag(String htmlstr) {
		// \\s?[s|Sc|Cr|Ri|Ip|Pt|T]
		Matcher m = htmlPattern.matcher(htmlstr);
		String rs = m.replaceAll("");
		rs = rs.replaceAll("&nbsp", " ");
		rs = rs.replaceAll("&lt;", "<");
		rs = rs.replaceAll("&gt;", ">");
		return rs;
	}

	/**
	 * 取从指定搜索项开始的字符串，返回的值不包含搜索项
	 *
	 * @param captions
	 *            例如:"www.koubei.com"
	 * @param regex
	 *            分隔符，例如"."
	 * @return 结果字符串，如：koubei.com，如未找到返回空串
	 */
	public static String getStrAfterRegex(String captions, String regex) {
		if (!isEmpty(captions) && !isEmpty(regex)) {
			int pos = captions.indexOf(regex);
			if (pos != -1 && pos < captions.length() - 1) {
				return captions.substring(pos + 1);
			}
		}
		return "";
	}

	/**
	 * 把字节码转换成16进制
	 */
	public static String byte2hex(byte bytes[]) {
		StringBuffer retString = new StringBuffer();
		for (int i = 0; i < bytes.length; ++i) {
			retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
					.substring(1).toUpperCase());
		}
		return retString.toString();
	}

	/**
	 * 把16进制转换成字节码
	 *
	 * @param hex
	 * @return
	 */
	public static byte[] hex2byte(String hex) {
		byte[] bts = new byte[hex.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
					16);
		}
		return bts;
	}

	/**
	 * 转换数字为固定长度的字符串
	 *
	 * @param length
	 *            希望返回的字符串长度
	 * @param data
	 *            传入的数值
	 * @return
	 */
	public static String getStringByInt(int length, String data) {
		String s_data = "";
		int datalength = data.length();
		if (length > 0 && length >= datalength) {
			for (int i = 0; i < length - datalength; i++) {
				s_data += "0";
			}
			s_data += data;
		}

		return s_data;
	}

	/**
	 * 判断是否位数字,并可为空
	 *
	 * @param src
	 * @return
	 */
	public static boolean isNumericAndCanNull(String src) {

		if (src == null || "".equals(src)) {
			return true;
		}
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numeric2Pattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}



	/**
	 * 将字符进行URLEncoder编码
	 * @param str
	 * @return
	 */
	public static String strURLEncoder(String str){
		if(null==str || str.trim().length()==0){
			return str;
		}

		try {
			str = URLEncoder.encode(str, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}


	/**
	 * 将字符进行URLDecoder解码
	 * @param str
	 * @return
	 */
	public static String strURLDecoder(String str){
		if (StringUtils.isBlank(str)) {
			return str;
		}

		try {
			str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
			// 字符串中加号(+)进行解码时候，导致内容丢失，所有需要将加号(+)替换成%2B，再进行解码就不会丢失
			str = str.replaceAll("\\+", "%2B");
			str = URLDecoder.decode(str, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}


	/**
	 * 将字符进行URLDecoder解码
	 * @param str
	 * @param encode
	 * @return
	 */
	public static String strURLDecoder(String str, String encode){
		if (StringUtils.isBlank(str)) {
			return str;
		}

		try {
			str = str.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
			// 字符串中加号(+)进行解码时候，导致内容丢失，所有需要将加号(+)替换成%2B，再进行解码就不会丢失
			str = str.replaceAll("\\+", "%2B");
			str = URLDecoder.decode(str, encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}


	/**
	 * 将字符串中加号(+)替换成%2B
	 * @param str
	 * @return
	 */
	public static String strReplacePlus(String str){
		if (StringUtils.isBlank(str)) {
			return str;
		}

		try {
			// 字符串中加号(+)进行解码时候，导致内容丢失，所有需要将加号(+)替换成%2B
			str = str.replaceAll("\\+", "%2B");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 将字符串中%2B替换成(+)
	 * @param str
	 * @return
	 */
	public static String plusReplaceStr(String str){
		if (StringUtils.isBlank(str)) {
			return str;
		}

		try {
			// 将字符串中%2B替换成(+)
			str = str.replaceAll("%2B","\\+");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}




	/**
	 * 获取子字符，在字符串中，第N次出现的位置
	 * @param string
	 * @param subStr
	 * @param n
	 * @return
	 */
	public static int getCharacterPosition(String string, String subStr, int n){
		int position = -1;

		if (StringUtils.isBlank(string) || StringUtils.isBlank(subStr)
				|| n<1){
			return position;
		}

		try{
			//这里是获取 subStr字符 的位置
			Matcher slashMatcher = Pattern.compile(subStr).matcher(string);
			int mIdx = 0;
			while(slashMatcher.find()) {
				mIdx++;
				//当 subStr字符 第N次出现的位置
				if(mIdx == n){
					break;
				}
			}
			position = slashMatcher.start();
		}catch(Exception ex){
			logger.error("getCharacterPosition error!", ex);
		}
		return position;
	}


	/**
	 * 在字符串中，每隔N个字符，添加一个字符。
	 * @param string
	 * @param n
	 * @param str
	 * @return
	 */
	public static String getCharacterCycleAddChar(String string, int n, String str){
		if(null==string || string.trim().length()==0 || null==str || n<0){
			return string;
		}

		try{
			char[] charArray = string.toCharArray();
			StringBuffer tempBuff = new StringBuffer();
			for(int i=0;i<charArray.length;i++){
				tempBuff.append(charArray[i]);
				if(i!=(charArray.length-1)&&(i+1)%n==0) {
					tempBuff.append(str);
				}
			}
			return tempBuff.toString();
		}catch(Exception ex){
			logger.error("getCharacterCycleAddChar error!", ex);
		}
		return string;
	}

	/**
	 * 判断字符串是否为乱码
	 * @param str 传入需要判断的字符串
	 * @return  如果为乱码则返回true，否则返回false
	 */
	public static boolean isMessyCode(String str) {
		Matcher m = messyCodePattern.matcher(str);
		String after = m.replaceAll("");
		String temp = after.replaceAll("\\p{P}", "");
		char[] ch = temp.trim().toCharArray();
		float chLength = 0;
		float count = 0;
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!Character.isLetterOrDigit(c)) {
				if (!isChinese(c)) {
					count = count + 1;
				}
				chLength++;
			}
		}
		float result = count / chLength;
		if (result > 0.4) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * 判断是否为中文char
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串是否为乱码并返回
	 * @param msg 传入乱码字符串
	 * @return 返回转换后的汉字数据
	 */
	public static String neatenChinese(String msg) {
		if (isMessyCode(msg)) {
			try {
				return new String(msg.getBytes("ISO8859-1"), "UTF-8");
			} catch (Exception e) {
			}
		}
		return msg;
	}


	/**
	 * 通过正则表达式的方式获取字符串中指定字符的个数
	 * @param text 字符串
	 * @param str 指定字符
	 * @return 指定字符的个数
	 */
	public static int getChartCount(String text, String str) {
		int count = 0;
		try{
			if(StringUtils.isBlank(text) && StringUtils.isBlank(str)){
				return count;
			}

			// 根据指定的字符构建正则
			Pattern pattern = Pattern.compile(str);
			// 构建字符串和正则的匹配
			Matcher matcher = pattern.matcher(text);

			// 循环依次往下匹配
			while (matcher.find()){ // 如果匹配,则数量+1
				count++;
			}
		}catch(Exception ex){
			logger.error("getChartCount", ex);
		}
		return  count;
	}



	/**
	 * 填充ip地址，前面补0
	 * @param ip
	 * @return
	 */
	public static String IPfill(String ip) {
		if(null==ip || ip.split("\\.").length!=4){
			return ip;
		}

		String nip = "";	//先设为空字符
		try{
			String[] temp = ip.split("\\.");	//字符串分组split()方法里是填正则表达式。所用用“\\.”
			for (int j = 0; j < temp.length; j++) {
				if (Integer.parseInt(temp[j]) / 10 == 0) {	//当该ip段为0-9时，前面补两个0
					temp[j] = "00" + temp[j];
				} else if (Integer.parseInt(temp[j]) / 100 == 0) {	//当该ip段为10-99时，前面补一个0
					temp[j] = "0" + temp[j];
				}
				nip += temp[j] + ".";	//重新赋值
			}
			nip = nip.substring(0, nip.length() - 1);	//去除最后一个多余的“.”
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return nip;
	}

	/**
	 * 回复ip地址，去除补上的0
	 * @param ip
	 * @return
	 */
	public static String IPrecovery(String ip) {
		if(null==ip || ip.split("\\.").length!=4){
			return ip;
		}

		String nip = "";
		try{
			String[] temp = ip.split("\\.");
			for (int j = 0; j < temp.length; j++) {
				if (temp[j].startsWith("00")) {	//去除0-9前面的0，一定要放前面
					temp[j] = temp[j].substring(2);
				} else if (temp[j].startsWith("0")) {	//去除10-99前面的0，一定要放后面
					temp[j] = temp[j].substring(1);
				}
				nip += temp[j] + ".";
			}
			nip = nip.substring(0, nip.length() - 1);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return nip;
	}


	public static String transforUrlParamStr(Map<String,String> urlPasm){
		if(null==urlPasm || urlPasm.size()==0){
			return null;
		}

		StringBuffer stringBuffer = new StringBuffer();
		Iterator<Map.Entry<String, String>> mapItr = urlPasm.entrySet().iterator();
		while (mapItr.hasNext()) {
			Map.Entry<String, String> entry = mapItr.next();
			//System.out.println(entry.getKey() + "=" + entry.getValue());
			stringBuffer.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}

		if(null!=stringBuffer && StringUtils.isNotBlank(stringBuffer.toString())){
			String urlParam = stringBuffer.toString();
			urlParam = urlParam.substring(1);
			return urlParam;
		}
		return null;
	}

	/**
	 * 从url中获取ip地址
	 * @param url
	 * @return
	 */
	public static String getUrlIP(String url){
		URI effectiveURI = null;
		try {
			effectiveURI = new URI(url);
		} catch (Throwable var4) {
			effectiveURI = null;
		}

		if(null!=effectiveURI){
			if(StringUtils.isBlank(effectiveURI.getHost())){
				return url;
			}else{
				return effectiveURI.getHost();
			}
		}
		return url;
	}

	/**
	 * 从url中获取端口
	 * @param url
	 * @return
	 */
	public static int getUrlPort(String url){
		URI effectiveURI = null;
		try {
			effectiveURI = new URI(url);
		} catch (Throwable var4) {
			effectiveURI = null;
		}

		if(null!=effectiveURI){
			if(effectiveURI.getPort()==-1){
				return 80;
			}else{
				return effectiveURI.getPort();
			}
		}
		return 80;
	}


	/**
	 * 把原始字符串分割成指定长度的字符串列表
	 *
	 * @param inputString
	 *            原始字符串
	 * @param length
	 *            指定长度
	 * @return
	 */
	public static List<String> getStrList(String inputString, int length) {
		int size = inputString.length() / length;
		if (inputString.length() % length != 0) {
			size += 1;
		}
		return getStrList(inputString, length, size);
	}

	/**
	 * 把原始字符串分割成指定长度的字符串列表
	 *
	 * @param inputString
	 *            原始字符串
	 * @param length
	 *            指定长度
	 * @param size
	 *            指定列表大小
	 * @return
	 */
	public static List<String> getStrList(String inputString, int length,
										  int size) {
		List<String> list = new ArrayList<String>();
		for (int index = 0; index < size; index++) {
			String childStr = substring(inputString, index * length,
					(index + 1) * length);
			list.add(childStr);
		}
		return list;
	}
	/**
	 * 分割字符串，如果开始位置大于字符串长度，返回空
	 *
	 * @param str
	 *            原始字符串
	 * @param f
	 *            开始位置
	 * @param t
	 *            结束位置
	 * @return
	 */
	public static String substring(String str, int f, int t) {
		if (f > str.length())
			return null;
		if (t > str.length()) {
			return str.substring(f, str.length());
		} else {
			return str.substring(f, t);
		}
	}


	/**
	 * 替换字符串中特殊字符
	 * @param string
	 * @param replacestr
	 * @return
	 */
	public static String strReplaceSpecialChar(String string,  String replacestr){
		if (null!=string && StringUtils.isNotBlank(string)) {
			if (null == replacestr) {
				replacestr = "_";
			}

			string = string.replaceAll("!", replacestr);
			string = string.replaceAll("@", replacestr);
			string = string.replaceAll("#", replacestr);
			string = string.replaceAll("\\$", replacestr); // 过滤掉$
			string = string.replaceAll("%", replacestr);
			string = string.replaceAll("\\^", replacestr);
			string = string.replaceAll("&", replacestr);
			string = string.replaceAll("\\*", replacestr);
			string = string.replaceAll("\\(", replacestr);
			string = string.replaceAll("\\)", replacestr);

			string = string.replaceAll("\\+", replacestr); // 替换+号
			string = string.replaceAll("\\=", replacestr);

			string = string.replaceAll("\\|", replacestr);

			string = string.replaceAll("\\[", replacestr);
			string = string.replaceAll("\\]", replacestr);

			string = string.replaceAll("\\.", replacestr);
			string = string.replaceAll("<", replacestr);
			string = string.replaceAll(">", replacestr);
			string = string.replaceAll("/", replacestr);
			string = string.replaceAll("\\?", replacestr);

		}

		return string;
	}

}
