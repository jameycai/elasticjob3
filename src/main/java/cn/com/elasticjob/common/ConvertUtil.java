package cn.com.elasticjob.common;

import io.netty.buffer.ByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Title:         java对象转换的帮助工具类
 * @description:
 *
 * @company        XXXXX股份有限公司
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2021年01月05日 14:54:59
 **/
public class ConvertUtil {
	//定义LOG的输出
	private static Logger logger = LoggerFactory.getLogger(ConvertUtil.class);



	/*** =================================================================
	 * 将object转换为相应字符型、整型
	 *
	 * ================================================================== **/
	/**
	 * 将通用对象s转换为long类型，如果字符串为空或null，返回r；
	 * @author caijinpeng
	 * @param s
	 * @param r
	 * @return
	 */
	public static long Obj2long(Object s, long r) {
		long i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				///i = Long.parseLong(str);
				BigDecimal bg = new BigDecimal(str);
				if(bg.doubleValue()==bg.longValue()){
					i = bg.longValue();
				}
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}


	/**
	 * 将通用对象s转换为long类型，如果字符串为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng
	 */
	public static long Obj2long(Object s) {
		long i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				///i = Long.parseLong(str);
				BigDecimal bg = new BigDecimal(str);
				if(bg.doubleValue()==bg.longValue()){
					i = bg.longValue();
				}
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}


	/**
	 * 将通用对象s转换为long类型，如果字符串为空或null，返回r；
	 * @param s
	 * @return
	 * @author caijinpeng
	 */
	public static double Obj2Double(Object s, double r) {
		double i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				//i = Double.parseDouble(str);
				BigDecimal bg = new BigDecimal(str);
				i = bg.doubleValue();
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}

	/**
	 * 将通用对象s转换为long类型，如果字符串为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng
	 */
	public static double Obj2Double(Object s) {
		double i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				//i = Double.parseDouble(str);
				BigDecimal bg = new BigDecimal(str);
				i = bg.doubleValue();
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}


	/**
	 * 将通用对象s转换为int类型，如果字符串为空或null，返回r；
	 * @author caijinpeng
	 * @param s
	 * @param r
	 * @return
	 */
	public static int Obj2int(Object s, int r) {
		int i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				//i = Integer.parseInt(str);
				BigDecimal bg = new BigDecimal(str);
				if(bg.doubleValue()==bg.intValue()){
					i = bg.intValue();
				}
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}

	/**
	 * 将通用对象s转换为int类型，如果字符串为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng
	 */
	public static int Obj2int(Object s) {
		int i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				//i = Integer.parseInt(str);
				BigDecimal bg = new BigDecimal(str);
				if(bg.doubleValue()==bg.intValue()){
					i = bg.intValue();
				}
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}


	/**
	 * 将通用对象s转换为float类型，如果字符串为空或null，返回r；
	 * @author caijinpeng
	 * @param s
	 * @param r
	 * @return
	 */
	public static float Obj2Float(Object s, float r) {
		float i = r;
		try {
			if(null!=s){
				String str = s.toString().trim();
				//i = Float.parseFloat(str);
				BigDecimal bg = new BigDecimal(str);
				i = bg.floatValue();
			}
		} catch (Exception e) {
			i = r;
		}
		return i;
	}

	/**
	 * 将通用对象s转换为float类型，如果字符串为空或null，返回0；
	 * @param s
	 * @return
	 * @author caijinpeng
	 */
	public static float Obj2Float(Object s) {
		float i = 0;
		try {
			if(null!=s){
				String str = s.toString().trim();
				//i = Float.parseFloat(str);
				BigDecimal bg = new BigDecimal(str);
				i = bg.floatValue();
			}
		} catch (Exception e) {
			i = 0;
		}
		return i;
	}

	/**
	 * 将通用对象s转换为String类型，如果字符串为空或null，返回r；
	 * @author caijinpeng
	 * @param s
	 * @param r
	 * @return
	 */
	public static String Obj2Str(Object s, String r) {
		String str = r;
		try {
			if(null!=s){
				str = String.valueOf(s).trim();
			}

			if(null==str || str.trim().length() == 0 || "null".equalsIgnoreCase(str.trim()) ){
				str = r;
			}
		} catch (Exception e) {
			str = r;
		}
		return str;
	}


	/**
	 * 将通用对象s转换为String类型，如果字符串为空或null，返回""；
	 * @author caijinpeng
	 * @param s
	 * @return
	 */
	public static String Obj2Str(Object s) {
		String str = "";
		try {
			if(null!=s){
				str = String.valueOf(s).trim();
			}

			if(null==str || str.trim().length() == 0 || "null".equalsIgnoreCase(str.trim()) ){
				str = "";
			}
		} catch (Exception e) {
			str = "";
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println(Obj2Str(null, "") == null);
		System.out.println(Obj2Str("", null) == null);
	}

	/**
	 * 将字符串 转换成 boolean 类型
	 * @param r
	 * @return
	 */
	public static boolean Obj2Boolean(Object r){
		boolean result = false;
		try {
			String str = Obj2Str(r, "");
			if(null!=str && str.trim().length()>0){
				Boolean rBoolean = Boolean.parseBoolean(str.trim());
				if(null!=rBoolean){
					result = rBoolean.booleanValue();
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 将字符串 转换成 boolean 类型
	 * @param r
	 * @return
	 */
	public static boolean Str2Boolean(String r){
		boolean result = false;
		try {
			if(null!=r && r.trim().length()>0){
				Boolean rBoolean = Boolean.parseBoolean(r.trim());
				if(null!=rBoolean){
					result = rBoolean.booleanValue();
				}
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}


	/**
	 * 去掉字符串 中前后的空格
	 * @param r
	 * @return
	 */
	public static String StringTrim(String r){
		String str = r;
		try{
			if(null!=r){
				str = r.trim();
			}
		}catch(Exception e){
			str = r;
		}
		return str;
	}


	/**
	 * 将long对象s转换为String类型，如果s为0，返回字符串r；
	 * @param s
	 * @return
	 */
	public static String Long2Str(long s, String r){
		String str = r;
		try {
			if(s!=0){
				str = String.valueOf(s);
			}
		} catch (Exception e){
			str = r;
		}
		return str;
	}


	/***
	 * 将double字符串对象d, 转换为int类型，如果d为NULL和空，返回0；
	 * @param d
	 * @return
	 */
	public static int DoubleStr2Int(String d){
		int i = 0;
		try{
			if(d!=null && d.trim().length()>0){
				Double D1=new Double(d);
				i = D1.intValue();
			}
		}catch(Exception e){
			i = 0;
		}
		return i;
	}

	/***
	 * 将double字符串对象d, 转换为long类型，如果d为NULL和空，返回0；
	 * @param d
	 * @return
	 */
	public static long DoubleStr2Long(String d){
		long i = 0;
		try{
			if(d!=null && d.trim().length()>0){
				Double D1=new Double(d);
				i = D1.longValue();
			}
		}catch(Exception e){
			i = 0;
		}
		return i;
	}



	/*** =================================================================
	 * 时间不同格式进行转换
	 *
	 * ================================================================== **/
	/**
	 * 将毫秒数转换为yyyy-MM-dd HH:mm:ss格式的时间串
	 * @param millis
	 * @return
	 */
	public static String Millis2StrLong(long millis){
		if (millis <= 0){
			return "";
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Instant instant = Instant.ofEpochMilli(millis);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			String s = df.format(localDeteTime);
			return s;
		}catch(Exception ex){
			logger.error("Millis2StrLong error! request millis:"+millis, ex);
		}
		return "";
	}


	/**
	 * 将毫秒数转换为HH:mm:ss格式的时间串
	 * @param millis
	 * @return
	 */
	public static String Millis2HmsStrLong(long millis){
		if (millis <= 0) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		Date date = calendar.getTime();
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("HH:mm:ss");
		String s = datetimeFormat.format(date);
		return s;
	}



	/**
	 * 将yyyy-MM-dd HH:mm:ss类型的字符串转换为毫秒数
	 * @param dateStr
	 * @return
	 */
	public static long StrLong2Millis(String dateStr){
		if (dateStr == null || "".equals(dateStr.trim())) {
			return 0;
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df);
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			long lTime = instant.toEpochMilli();
			return lTime;
		}catch(Exception ex){
			logger.error("StrLong2Millis error! request dateStr:"+dateStr, ex);
		}
		return 0;
	}



	/**
	 * 将毫秒数转换为yyyy-MM-dd格式的时间串
	 * @param millis
	 * @return
	 */
	public static String Millis2Str(long millis){
		if (millis <= 0){
			return "";
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd");
			Instant instant = Instant.ofEpochMilli(millis);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			String s = df.format(localDeteTime);
			return s;
		}catch(Exception ex){
			logger.error("Millis2Str error! request millis:"+millis, ex);
		}
		return "";
	}


	/**
	 * 将yyyy-MM-dd类型的字符串转换为毫秒数
	 * @param dateStr
	 * @return
	 */
	public static long Str2Millis(String dateStr){
		if (dateStr == null || "".equals(dateStr.trim())) {
			return 0;
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate localDate = LocalDate.parse(dateStr, df);
			Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
			long lTime = instant.toEpochMilli();
			return lTime;
		}catch(Exception ex){
			logger.error("Str2Millis error! request dateStr:"+dateStr, ex);
		}
		return 0;
	}


	/**
	 * 将给定类型的时间字符串转换为毫秒数
	 * @param dateStr
	 * @param _dtFormat
	 *     日期格式1  yyyy-MM-dd HH:mm:ss
	 *     日期格式2  yyyy-MM-dd
	 *     日期格式3  yyyyMMddHH
	 * @return
	 */
	public static long StrFormat2Millis(String dateStr, String _dtFormat){
		if (dateStr == null || "".equals(dateStr.trim())) {
			return 0;
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df);
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			long lTime = instant.toEpochMilli();
			return lTime;
		}catch(Exception ex){
			logger.error("StrFormat2Millis error! request dateStr:"+dateStr+", _dtFormat:"+_dtFormat, ex);
		}
		return 0;
	}




	/**
	 * 将给定类型的时间字符串转换为时间
	 * @param dateStr
	 * @param _dtFormat
	 *     日期格式1  yyyy-MM-dd HH:mm:ss
	 *     日期格式2  yyyy-MM-dd
	 *     日期格式3  yyyyMMddHH
	 * @return
	 */
	public static Date StrFormat2Date(String dateStr, String _dtFormat){
		try{
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);

			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime zdt = ldt.atZone(zoneId);
			Date date = Date.from(zdt.toInstant());
			return date;
		}catch(Exception ex){
			logger.error("StrFormat2Date error! request dateStr:"+dateStr+", _dtFormat:"+_dtFormat, ex);
		}
		return null;
	}


	/**
	 * 将13位时间毫秒数转换为 指定格式的时间字符串串
	 * @param millis
	 * @param _dtFormat
	 *     日期格式1  yyyy-MM-dd HH:mm:ss
	 *     日期格式2  yyyyMMdd
	 *     日期格式3  yyyyMMddHH
	 * @return
	 */
	public static String Millis2FormatStr(long millis, String _dtFormat){
		if (millis <= 0){
			return "";
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern(_dtFormat);
			Instant instant = Instant.ofEpochMilli(millis);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			String s = df.format(localDeteTime);
			return s;
		}catch(Exception ex){
			logger.error("Millis2FormatStr error! request millis:"+millis+", _dtFormat:"+_dtFormat, ex);
		}
		return "";
	}




	/***
	 * 将指定时间字符串和时间格式，延后几分钟的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd HH:mm:ss
	 * @param mins      分钟
	 * @return
	 */
	public static String getDataTimeNextMins(String dateStr, String _dtFormat, int mins) {
		String plusDateTimeStr = "";
		try{
			if(mins==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.plusMinutes(mins);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}
	/***
	 * 将指定时间字符串和时间格式，减少几分钟的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd HH:mm:ss
	 * @param mins      分钟
	 * @return
	 */
	public static String getDataTimeMinusMins(String dateStr, String _dtFormat, int mins) {
		String plusDateTimeStr = "";
		try{
			if(mins==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.minusMinutes(mins);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}


	/***
	 * 将指定时间字符串和时间格式，延后几小时的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式 yyyy-MM-dd HH:mm:ss
	 * @param hour      分钟
	 * @return
	 */
	public static String getDataTimeNextHour(String dateStr, String _dtFormat, int hour) {
		String plusDateTimeStr = "";
		try{
			if(hour==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.plusHours(hour);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}
	/***
	 * 将指定时间字符串和时间格式，前移几小时的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式 yyyy-MM-dd HH:mm:ss
	 * @param hour      分钟
	 * @return
	 */
	public static String getDataTimeMinusHour(String dateStr, String _dtFormat, int hour) {
		String plusDateTimeStr = "";
		try{
			if(hour==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.minusHours(hour);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}


	/***
	 * 将指定时间字符串和时间格式，延后几天的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd
	 * @param days      天数
	 * @return
	 */
	public static String getDataNextDay(String dateStr, String _dtFormat, int days) {
		String plusDateTimeStr = "";
		try{
			if(days==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDate ldt = LocalDate.parse(dateStr, df);
			LocalDate date = ldt.plusDays(days);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}
	/***
	 * 将指定时间字符串和时间格式，前移几天的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd
	 * @param days      天数
	 * @return
	 */
	public static String getDataMinusDay(String dateStr, String _dtFormat, int days) {
		String plusDateTimeStr = "";
		try{
			if(days==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDate ldt = LocalDate.parse(dateStr, df);
			LocalDate date = ldt.minusDays(days);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}



	/**
	 * 获取当前时间，延后几分钟的时间
	 * @param minutes 分钟
	 * @return
	 */
	public static String getNowDataTimeNextMins(int minutes) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().plusMinutes(minutes);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextHour error! request minutes:"+minutes, ex);
		}
		return plusDateTimeStr;
	}
	/**
	 * 获取当前时间，前移几分钟的时间
	 * @param minutes 分钟
	 * @return
	 */
	public static String getNowDataTimeMinusMins(int minutes) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().minusMinutes(minutes);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextHour error! request minutes:"+minutes, ex);
		}
		return plusDateTimeStr;
	}



	/**
	 * 获取当前时间，延后几小时的时间
	 * @param hours 天数
	 * @return
	 */
	public static String getNowDataTimeNextHour(int hours) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().plusHours(hours);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextHour error! request hours:"+hours, ex);
		}
		return plusDateTimeStr;
	}
	/**
	 * 获取当前时间，前移几小时的时间
	 * @param hours 天数
	 * @return
	 */
	public static String getNowDataTimeMinusHour(int hours) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().minusHours(hours);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextHour error! request hours:"+hours, ex);
		}
		return plusDateTimeStr;
	}


	/**
	 * 获取当前时间，延后几天的时间
	 * @param days 天数
	 * @return
	 */
	public static String getNowDataTimeNextDay(int days) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().plusDays(days);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextDay error! request days:"+days, ex);
		}
		return plusDateTimeStr;
	}
	/**
	 * 获取当前时间，前移几天的时间
	 * @param days 天数
	 * @return
	 */
	public static String getNowDataTimeMinusDay(int days) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().minusDays(days);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextDay error! request days:"+days, ex);
		}
		return plusDateTimeStr;
	}



	/**
	 * 获取当前时间，延后几个月时间
	 * @param month 月
	 * @return
	 */
	public static String getNowDataTimeNextMonth(int month) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().plusMonths(month);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextMonth error! request month:"+month, ex);
		}
		return plusDateTimeStr;
	}
	/**
	 * 获取当前时间，前移几个月时间
	 * @param month 月
	 * @return
	 */
	public static String getNowDataTimeMinuxMonth(int month) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().minusMonths(month);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextMonth error! request month:"+month, ex);
		}
		return plusDateTimeStr;
	}


	/**
	 * 获取当前时间，延后几个年时间
	 * @param years 年
	 * @return
	 */
	public static String getNowDataTimeNextYears(int years) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().plusYears(years);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextYears error! request years:"+years, ex);
		}
		return plusDateTimeStr;
	}
	/**
	 * 获取当前时间，前移几个年时间
	 * @param years 年
	 * @return
	 */
	public static String getNowDataTimeMinusYears(int years) {
		String plusDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now().minusYears(years);
			DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			plusDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDataTimeNextYears error! request years:"+years, ex);
		}
		return plusDateTimeStr;
	}




	/**
	 * 将时间字符串yyyy-MM-dd HH:mm:ss, 转换为yyyy-MM-dd HH:mm:00
	 * @param datastr
	 * @return
	 */
	public static String DateTimeStr2YMDHM0(String datastr){
		if(null==datastr || datastr.trim().length()==0){
			return datastr;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mydate1 = "";
		try {
			Date date1 = format.parse(datastr);

			SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			mydate1 = datetimeFormat.format(date1);
			mydate1 +=":00";
		} catch (Exception e) {
		}
		return mydate1;
	}



	/**
	 * 将短时间格式字符串转换为时间 yyyy-MM-dd
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime ldt = LocalDateTime.parse(strDate, df);

			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime zdt = ldt.atZone(zoneId);
			Date date = Date.from(zdt.toInstant());
			return date;
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	/**
	 * 按照日前模式和本地化转化日期为字符串
	 * @param pattern
	 * @param locale
	 * @param date
	 *
	 */
	public static String getDateString(String pattern, Locale locale, Date date) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(pattern, locale);

			return df.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 将时间秒转换为时分秒(HH时mm分ss秒)
	 * @param time
	 * @return
	 *    1. 30秒
	 *    2. 2分40秒
	 *    3. 4小时50分5秒
	 */
	public static String secondToHMSTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0) {
			return "0分0秒";
		} else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = minute + "分" + unitFormat(second)+"秒";
			} else {
				hour = minute / 60;
				if (hour > 99999) {
					return "99999时59分59秒";
				}
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = hour + "时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";
			}
		}
		return timeStr;
	}



	private static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10) {
			retStr = "0" + Integer.toString(i);
		}else{
			retStr = "" + i;
		}
		return retStr;
	}



	/**
	 * 将毫秒值转换为时分秒(HH时mm分ss秒)
	 * @param mtime
	 * @return
	 *    1. 30秒
	 *    2. 2分40秒
	 *    3. 4小时50分5秒
	 */
	public static String Millis2HMSTime(long mtime){
		long ltime = mtime / 1000;
		int time = Integer.parseInt(ltime+"");

		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0) {
			return "0分0秒";
		} else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				if(minute==0){
					timeStr = unitFormat(second)+"秒";
				}else{
					timeStr = minute + "分" + unitFormat(second)+"秒";
				}
			} else {
				hour = minute / 60;
				if (hour > 99999)  {
					return "99999时59分59秒";
				}
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = hour + "时" + unitFormat(minute) + "分" + unitFormat(second)+"秒";
			}
		}
		return timeStr;
	}

	/*
	 * 将毫秒值转换为时分秒(D天HH小时mm分钟)
	 * 将 毫秒转化   天  小时  分钟
	 * @param ms 毫秒数
	 */
	public static String Millis2DHMTime(Long ms) {
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = ms / dd;
		Long hour = (ms - day * dd) / hh;
		Long minute = (ms - day * dd - hour * hh) / mi;
		Long second = (ms - day * dd - hour * hh - minute * mi) / ss;

		StringBuilder sb = new StringBuilder();
		if(day > 0) {
			sb.append(day+"天");
		}
		if(hour > 0) {
			sb.append(hour+"小时");
		}
		if(minute > 0) {
			sb.append(minute+"分钟");
		}
		if(second > 0) {
			sb.append(second+"秒");
		}
		return sb.toString();
	}


	/*
	 * 将秒值转换为时分秒(D天HH小时mm分钟)
	 * 将 毫秒转化   天  小时  分钟
	 * @param ms 秒数
	 */
	public static String secondMillis2DHMTime(Long s) {
		Integer ss = 1;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = s / dd;
		Long hour = (s - day * dd) / hh;
		Long minute = (s - day * dd - hour * hh) / mi;
		Long second = (s - day * dd - hour * hh - minute * mi) / ss;

		StringBuilder sb = new StringBuilder();
		if(day > 0) {
			sb.append(day+"天");
		}
		if(hour > 0) {
			sb.append(hour+"小时");
		}
		if(minute > 0) {
			sb.append(minute+"分钟");
		}
		if(second > 0) {
			sb.append(second+"秒");
		}
		return sb.toString();
	}




	/**
	 * 把list字符串，拆分成多个批次
	 * @param list 集合
	 * @param batchSize 批次大小
	 * @return Map<Integer,List<Long>>
	 */
	public static Map<Integer, List<String>> ListStrSplit2BatchList(List<String> list, int batchSize){
		Map<Integer,List<String>> itemMap = new HashMap<>();
		itemMap.put(1, new ArrayList<String>());
		for(String e : list){
			List<String> batchList= itemMap.get(itemMap.size());
			if(batchList.size() == batchSize){//当list满足批次数量，新建一个list存放后面的数据
				batchList = new ArrayList<String>();
				itemMap.put(itemMap.size()+1, batchList);
			}
			batchList.add(e);
		}
		return itemMap;
	}


	/**
	 * 把list数值，拆分成多个批次
	 * @param list 集合
	 * @param batchSize 批次大小
	 * @return Map<Integer,List<String>>
	 */
	public static Map<Integer, List<Long>> ListNumSplit2BatchList(List<Long> list, int batchSize){
		Map<Integer,List<Long>> itemMap = new HashMap<>();
		itemMap.put(1, new ArrayList<Long>());
		for(Long e : list){
			List<Long> batchList= itemMap.get(itemMap.size());
			if(batchList.size() == batchSize){//当list满足批次数量，新建一个list存放后面的数据
				batchList = new ArrayList<Long>();
				itemMap.put(itemMap.size()+1, batchList);
			}
			batchList.add(e);
		}
		return itemMap;
	}


	/**
	 * 获得当前 [给定日期时间 格式] 的字符串
	 *
	 * @param _dtFormat
	 *     日期格式1  yyyy-MM-dd HH:mm:ss
	 *     日期格式2  yyyy-MM-dd
	 * @return 给定日期格式的字符串
	 */
	public static String getNowDateTime(String _dtFormat) {
		String currentdatetime = "";
		try {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat dtFormat = new SimpleDateFormat(_dtFormat);
			currentdatetime = dtFormat.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentdatetime;
	}


	/**
	 * 获得当前日期时间格式的字符串
	 *
	 * @param
	 * @return 当前日期时间格式的字符串 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNewDateTime() {
		return getNowDateTime("yyyy-MM-dd HH:mm:ss");
	}




	/**
	 * 将普通字符串转换为16进制字符串
	 * @param str 普通字符串
	 * @param lowerCase 转换后的字母为是否为小写  可不传默认为true
	 * @param charset 编码格式  可不传默认为Charset.defaultCharset()
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String str2HexStr(String str,boolean lowerCase,String charset) throws UnsupportedEncodingException {
		return Hex.encodeHexString(str.getBytes(charset),lowerCase);
	}
	/**
	 * 将16进制字符串转换为普通字符串
	 * @param hexStr 16进制字符串
	 * @param charset 编码格式 可不传默认为Charset.defaultCharset()
	 * @return
	 * @throws DecoderException
	 * @throws UnsupportedEncodingException
	 */
	public static String hexStr2Str(String hexStr,String charset) throws DecoderException, UnsupportedEncodingException {
		byte[] bytes = Hex.decodeHex(hexStr);
		return new String(bytes,charset);
	}
	/**
	 * 将16进制字符串转换为byte数组
	 * @param hexItr 16进制字符串
	 * @return
	 */
	public static byte[] hexItr2Arr(String hexItr) throws DecoderException {
		return Hex.decodeHex(hexItr);
	}
	/**
	 * byte数组转化为16进制字符串
	 * @param arr 数组
	 * @param lowerCase 转换后的字母为是否为小写 可不传默认为true
	 * @return
	 */
	public static String arr2HexStr(byte[] arr,boolean lowerCase){
		return Hex.encodeHexString(arr, lowerCase);
	}
	/**
	 * 将普通字符串转换为指定格式的byte数组
	 * @param str 普通字符串
	 * @param charset 编码格式 可不传默认为Charset.defaultCharset()
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] str2Arr(String str,String charset) throws UnsupportedEncodingException {
		return str.getBytes(charset);
	}
	/**
	 * 将byte数组转换为指定编码格式的普通字符串
	 * @param arr byte数组
	 * @param charset 编码格式 可不传默认为Charset.defaultCharset()
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String arr2Str(byte[] arr,String charset) throws UnsupportedEncodingException {
		return new String(arr,charset);
	}


	/**
	 * commons-code方式Base64编码
	 * @param str
	 * @return
	 */
	public static String encodeByCommonsCode(String str) {
		byte[] result;
		try {
			result = org.apache.commons.codec.binary.Base64.encodeBase64(str.getBytes("UTF-8"));
			return new String(result);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * commons-code方式Base64解码
	 * @param str
	 * @return
	 */
	public static String decodeByCommonsCode(String str) {
		byte[] result = org.apache.commons.codec.binary.Base64.decodeBase64(str.getBytes());
		return new String(result);
	}



	/**
	 * 将ascii转换为普通String
	 * @param text
	 * @return
	 */
	public static String asciiToString(String text) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == '1' && i < text.length() - 2) {
				int code = Integer.parseInt(text.substring(i, i + 3));
				builder.append((char) code);
				i += 2;
			} else if (i < text.length() - 1) {
				int code = Integer.parseInt(text.substring(i, i + 2));
				builder.append((char) code);
				i += 1;
			}
		}
		return builder.toString();
	}

	/**
	 * 将Netty ByteBuf 转换 byte[]
	 * @param buf
	 * @return
	 */
	public byte[] convertByteBufToBytes(ByteBuf buf) {
		byte[] bytes = null;
		try{
			if(null!=buf){
				bytes = new byte[buf.readableBytes()];
				buf.getBytes(buf.readerIndex(), bytes);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
		if(null==bytes){
			bytes = new byte[0];
		}
		return bytes;
	}



	/**
	 * 将16进制 转换为 byte[]
	 *
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	private static String hexString = "0123456789ABCDEF";
	/**将16进制转换为字符串*/
	public  static Object hexToStr(Object hex) {
		String bytes = String.valueOf(hex).toUpperCase();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}
	/**将16进制转换为十进制*/
	public  static Object hexToDec(Object hex) {
		String tmpHex = String.valueOf(hex);
		tmpHex = tmpHex.toUpperCase();
		int val = 0;
		for (int i = 0; i < tmpHex.length(); i++){
			char c = tmpHex.charAt(i);
			int d = hexString.indexOf(c);
			val = 16 * val + d;

		}
		return val;
	}


}
