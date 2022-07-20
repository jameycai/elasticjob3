package cn.com.elasticjob.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 *
 * @ClassName:     日期时间帮助类
 * @Description:   TODO(用一句话描述该文件做什么)
 *
 * @company        XXXXX股份有限公司
 * @author         caijinpeng
 * @email          jamey_cai@163.com
 * @version        V1.0
 * @Date           2021年01月05日 14:54:59
 *
 **/
public class DateTimeUtil {
	//定义LOG的输出
	private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

	public static String FORMAT_YYMM = "yyMM";

	/**
	 * 将毫秒数转换为yyyy-MM-dd HH:mm:ss格式的时间串
	 *
	 * @author caijinpeng
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
	 * 将yyyy-MM-dd HH:mm:ss类型的字符串转换为毫秒数
	 *
	 * @author caijinpeng
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
	 * 将yyyy-MM-dd HH:mm:ss类型的字符串转换为秒数
	 *
	 * @author caijinpeng
	 * @param dateStr
	 * @return
	 */
	public static long StrLong2Seconds(String dateStr){
		if (dateStr == null || "".equals(dateStr.trim())) {
			return 0;
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime = LocalDateTime.parse(dateStr, df);
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			long lTime = instant.toEpochMilli();
			if(String.valueOf(lTime).length()>10){
				String lTimeStr = String.valueOf(lTime).substring(0,10);
				return ConvertUtil.Obj2long(lTimeStr);
			}
		}catch(Exception ex){
			logger.error("StrLong2Millis error! request dateStr:"+dateStr, ex);
		}
		return 0;
	}



	/**
	 * 将毫秒数转换为yyyy-MM-dd格式的时间串
	 *
	 * @author caijinpeng
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
	 *
	 * @author caijinpeng
	 * @param dateStr
	 * @return
	 */
	public static long Str2Millis(String dateStr){
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
	 * 将13位毫秒转化为秒
	 *
	 * @author caijinpeng
	 * @param millis
	 * @return
	 */
	public static int Millis2Second(long millis){
		if (millis <= 0){
			return 0;
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Instant instant = Instant.ofEpochMilli(millis);
			long second = instant.getEpochSecond();
			return Integer.parseInt(second+"");
		}catch(Exception ex){
			logger.error("Millis2StrLong error! request millis:"+millis, ex);
		}
		return 0;
	}


	/**
	 * 将给定类型的时间字符串转换为时间
	 *
	 * @author caijinpeng
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
	 * 将给定类型的时间字符串 转换 指定时间格式输出
	 * @param dateStr     180510100000
	 * @param _inputFormat  "yyMMddHHmmss"
	 * @param _outFormat  "yyyy-MM-dd HH:mm:ss"
	 * @return
	 */
	public static String StrFormat2DateFormatStr(String dateStr , String _inputFormat, String _outFormat){
		try{
			// yyMMddHHmmss
			DateTimeFormatter _inputDf1= DateTimeFormatter.ofPattern(_inputFormat);
			LocalDateTime localDateTime = LocalDateTime.parse(dateStr, _inputDf1);
			Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

			// yyyy-MM-dd HH:mm:ss
			DateTimeFormatter df2= DateTimeFormatter.ofPattern(_outFormat);
			String s = df2.format(localDeteTime);
			return s;
		}catch (Exception ex){
			logger.error("StrFormat2DateFormatStr error! request dateStr: "+dateStr+", _inputFormat: "+_inputFormat+", _outFormat: "+_outFormat, ex);
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
	 *     日期格式4  MM-dd HH:mm:ss
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




	/**
	 * 将毫秒转换为时间Day
	 * @param millis
	 * @return
	 */
	public static String Mills2TimeDay(long millis){
		if (millis <= 0){
			return "0";
		}
		Integer ss = 1000;
		Integer mi = ss * 60;
		Integer hh = mi * 60;
		Integer dd = hh * 24;

		Long day = millis / dd;
		//Long hour = (millis - day * dd) / hh;
		//Long minute = (millis - day * dd - hour * hh) / mi;
		//Long second = (millis - day * dd - hour * hh - minute * mi) / ss;
		//Long milliSecond = millis - day * dd - hour * hh - minute * mi - second * ss;

		if(day > 0) {
			return day+"";
		} else  {
			return "0";
		}
	}


	/**
	 * 将毫秒转换为天、时分秒
	 * @param millis
	 * @return
	 */
	public static String Mills2DayHHMMSS(long millis){
		if (millis <= 0){
			return "";
		}

		try{
			Instant instant = Instant.ofEpochMilli(millis);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			int day = localDeteTime.getDayOfYear();
			int hour = localDeteTime.getHour();
			int mm = localDeteTime.getMinute();
			int ss = localDeteTime.getSecond();
			if(day>0){
				return day+"天"+hour+"小时"+mm+"分"+ss+"秒";
			}
			if(hour>0){
				return hour+"小时"+mm+"分"+ss+"秒";
			}
			if(mm>0){
				return mm+"分"+ss+"秒";
			}
			if(ss>0){
				return ss+"秒";
			}
		}catch(Exception ex){
			logger.error("Mills2DayHHMMSS error! request millis:"+millis, ex);
		}
		return "";
	}


	/**
	 * 秒转换为yyyy-MM-dd HH:mm:ss格式的时间串
	 * @param millis
	 * @return
	 */
	public static String Second2StrLong(long millis){
		if (millis <= 0){
			return "";
		}

		try{
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Instant instant = Instant.ofEpochMilli(millis * 1000);
			LocalDateTime localDeteTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
			String s = df.format(localDeteTime);
			return s;
		}catch(Exception ex){
			logger.error("Millis2StrLong error! request millis:"+millis, ex);
		}
		return "";
	}



	/**
	 * 获得当前时间， 并且指定时间字符串格式 _dtFormat
	 *
	 * @param _dtFormat
	 *     日期格式1  yyyy-MM-dd HH:mm:ss
	 *     日期格式2  yyyy-MM-dd
	 * @return 给定日期格式的字符串
	 * @author caijinpeng
	 */
	public static String getNowDateTime(String _dtFormat) {
		String currentDateTimeStr = "";
		try {
			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter f = DateTimeFormatter.ofPattern(_dtFormat);
			currentDateTimeStr = date.format(f);
		} catch (Exception ex) {
			logger.error("getNowDateTime error! request _dtFormat:"+_dtFormat, ex);
		}
		return currentDateTimeStr;
	}


	/**
	 * 获取当前时间，时间格式 yyyy-MM-dd HH:mm:ss
	 *
	 * @param
	 * @return 当前日期时间格式的字符串 yyyy-MM-dd HH:mm:ss
	 * @author caijinpeng
	 */
	public static String getNowDateTime() {
		return getNowDateTime("yyyy-MM-dd HH:mm:ss");
	}


	/**
	 * 获取当前日期时间，时间格式 yyyy-MM-dd
	 *
	 * @param
	 * @return 当前日期时间格式的字符串 yyyy-MM-dd
	 * @author caijinpeng
	 */
	public static String getNowDate(){
		return getNowDateTime("yyyy-MM-dd");
	}


	/**
	 * 比较两个日期大小，指定日期格式传递
	 *
	 * @param startTime
	 * @param endTime
	 * @param _dtFormat 日期格式
	 * @return
	 */
	public static boolean timeStrCompare(String startTime, String endTime, String _dtFormat) {
		boolean t = false;

		try {
			DateTimeFormatter df= DateTimeFormatter.ofPattern(_dtFormat);

			LocalDateTime localDateTime1 = LocalDateTime.parse(startTime, df);
			LocalDateTime localDateTime2 = LocalDateTime.parse(endTime, df);

			Duration duration = Duration.between(localDateTime1, localDateTime2);
			long diff = duration.toMillis();
			if (diff > 0){
				t = true;
			}
		} catch (Exception ex) {
			logger.error("timeCompare error! request _dtFormat:"+_dtFormat, ex);
		}
		return t;
	}


	/**
	 * 比较日期字符串，  返回相差的天数
	 * @param startTime yyyy-MM-dd
	 * @param endTime yyyy-MM-dd
	 * @return  天数
	 */
	public static int timeStrDiffToDay(String startTime, String endTime) {
		int distanceDay = 0;
		try {
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate localDateTime1 = LocalDate.parse(startTime, df);
			LocalDate localDete1 = LocalDate.of(localDateTime1.getYear(), localDateTime1.getMonth(), localDateTime1.getDayOfMonth());
			LocalDate localDateTime2 = LocalDate.parse(endTime, df);
			LocalDate localDete2 = LocalDate.of(localDateTime2.getYear(), localDateTime2.getMonth(), localDateTime2.getDayOfMonth());

			Period duration = Period.between(localDete1, localDete2);
			distanceDay = duration.getDays();
		} catch (Exception ex) {
			logger.error("timeStrDiffToDay error! request startTime:"+startTime+", endTime:"+endTime, ex);
		}
		return distanceDay;
	}


	/**
	 * 比较时间字符串，  返回相差的小时
	 *
	 * @param startTime
	 * @param endTime
	 * @return 小时
	 */
	public static long timeStrDiffToHours(String startTime, String endTime) {
		long distanceTime = 0;
		try {
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

			LocalDateTime localDateTime1 = LocalDateTime.parse(startTime, df);
			LocalDateTime localDateTime2 = LocalDateTime.parse(endTime, df);

			Duration duration = Duration.between(localDateTime1, localDateTime2);
			distanceTime = duration.toHours();
		} catch (Exception ex) {
			logger.error("timeStrDiffToHours error! request startTime:"+startTime+", endTime:"+endTime, ex);
		}
		return distanceTime;
	}

	/**
	 * 两个毫秒时间，相差多少月
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int millisDiffToMonth(long startTime, long endTime) {
		int distanceDay = 0;
		try {
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Instant instant1 = Instant.ofEpochMilli(startTime);
			LocalDateTime localDeteTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
			LocalDate localDete1 = LocalDate.of(localDeteTime1.getYear(), localDeteTime1.getMonth(), localDeteTime1.getDayOfMonth());

			Instant instant2 = Instant.ofEpochMilli(endTime);
			LocalDateTime localDeteTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
			LocalDate localDete2 = LocalDate.of(localDeteTime2.getYear(), localDeteTime2.getMonth(), localDeteTime2.getDayOfMonth());

			Period duration = Period.between(localDete1, localDete2);
			distanceDay = duration.getMonths();
		}catch(Exception ex) {
			logger.error("millisDiffToMonth error! request startTime:"+startTime+", endTime:"+endTime, ex);
		}
		return distanceDay;
	}



	/**
	 * 两个毫秒时间，相差多少天
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int millisDiffToDay(long startTime, long endTime) {
		int distanceDay = 0;
		try {
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Instant instant1 = Instant.ofEpochMilli(startTime);
			LocalDateTime localDeteTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
			LocalDate localDete1 = LocalDate.of(localDeteTime1.getYear(), localDeteTime1.getMonth(), localDeteTime1.getDayOfMonth());

			Instant instant2 = Instant.ofEpochMilli(endTime);
			LocalDateTime localDeteTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
			LocalDate localDete2 = LocalDate.of(localDeteTime2.getYear(), localDeteTime2.getMonth(), localDeteTime2.getDayOfMonth());

			Period duration = Period.between(localDete1, localDete2);
			distanceDay = duration.getDays();
		}catch(Exception ex) {
			logger.error("millisDiffToDay error! request startTime:"+startTime+", endTime:"+endTime, ex);
		}
		return distanceDay;
	}


	/**
	 * 两个毫秒时间，相差多少小时
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static long millisDiffToHours(long startTime, long endTime) {
		long distanceDay = 0;
		try {
			DateTimeFormatter df= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			Instant instant1 = Instant.ofEpochMilli(startTime);
			LocalDateTime localDeteTime1 = LocalDateTime.ofInstant(instant1, ZoneId.systemDefault());
			Instant instant2 = Instant.ofEpochMilli(endTime);
			LocalDateTime localDeteTime2 = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());

			Duration duration = Duration.between(localDeteTime1, localDeteTime2);
			distanceDay = duration.toHours();
		}catch(Exception ex) {
			logger.error("millisDiffToHours error! request startTime:"+startTime+", endTime:"+endTime, ex);
		}
		return distanceDay;
	}



	/***
	 * 将指定时间字符串和时间格式，延后几秒的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd HH:mm:ss
	 * @param sec       秒
	 * @return
	 */
	public static String getDataTimeNextSeconds(String dateStr, String _dtFormat, int sec) {
		String plusDateTimeStr = "";
		try{
			if(sec==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.plusSeconds(sec);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}
	/***
	 * 将指定时间字符串和时间格式，减少几秒的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd HH:mm:ss
	 * @param sec       秒
	 * @return
	 */
	public static String getDataTimeMinusSeconds(String dateStr, String _dtFormat, int sec) {
		String plusDateTimeStr = "";
		try{
			if(sec==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.minusSeconds(sec);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
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

	/***
	 * 将指定时间字符串和时间格式，延后几天的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd HH:mm:ss
	 * @param days      天数
	 * @return
	 */
	public static String getDataTimeNextDay(String dateStr, String _dtFormat, int days) {
		String plusDateTimeStr = "";
		try{
			if(days==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.plusDays(days);
			plusDateTimeStr = date.format(df);
		}catch(Exception e){
			return plusDateTimeStr;
		}
		return plusDateTimeStr;
	}
	/***
	 * 将指定时间字符串和时间格式，前移几天的时间
	 * @param dateStr   时间字符串
	 * @param _dtFormat 时间格式  yyyy-MM-dd HH:mm:ss
	 * @param days      天数
	 * @return
	 */
	public static String getDataTimeMinusDay(String dateStr, String _dtFormat, int days) {
		String plusDateTimeStr = "";
		try{
			if(days==0){
				return dateStr;
			}
			DateTimeFormatter df = DateTimeFormatter.ofPattern(_dtFormat);
			LocalDateTime ldt = LocalDateTime.parse(dateStr, df);
			LocalDateTime date = ldt.minusDays(days);
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
	 * 将短时间格式时间转换为字符串 yyyy-MM-dd
	 *
	 * @param dateDate
	 * @param dateDate
	 * @return
	 */
	public static String dateToStr(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}


	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToDateLong(String strDate) {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 *
	 * @param dateDate
	 * @return
	 */
	public static String dateToStrLong(Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}


	/**
	 * 前一个小时
	 */
	public static long previousHour(){
		String dateStr = getNowDataTimeNextHour(-1);
		return StrLong2Millis(dateStr);
	}

	/**
	 * 前N个小时
	 */
	public static long previousHour(int hour){
		String dateStr = getNowDataTimeNextHour(-hour);
		return StrLong2Millis(dateStr);
	}

	/**
	 * 最近12小时
	 *
	 */
	public static long prevHalfOfDay(){
		String dateStr = getNowDataTimeNextHour(-12);
		return StrLong2Millis(dateStr);
	}



	/**
	 * 最近N天
	 *
	 */
	public static long prevDay(int day){
		String dateStr = getNowDataTimeNextDay(-day);
		return StrLong2Millis(dateStr);
	}



	/**
	 * 最近一天
	 *
	 */
	public static long prevDay(){
		String dateStr = getNowDataTimeNextDay(-1);
		return StrLong2Millis(dateStr);
	}

	/**
	 * 最近三天
	 *
	 */
	public static long prevThreeDay(){
		String dateStr = getNowDataTimeNextDay(-3);
		return StrLong2Millis(dateStr);
	}


	/**
	 * 最近一周
	 *
	 */
	public static long prevWeek(){
		String dateStr = getNowDataTimeNextDay(-7);
		return StrLong2Millis(dateStr);
	}


	/**
	 * 最近一月
	 *
	 */
	public static long prevMonth(){
		String dateStr = getNowDataTimeNextMonth(-1);
		return StrLong2Millis(dateStr);
	}


	/**
	 * 获取今天的开始时间
	 * @param today
	 * @return 格式:YYYY-MM-DD HH:MM:SS
	 * @throws Exception
	 */
	public static String getTodayFromTime(Calendar today) throws Exception{
		SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormatDate.format(today.getTime())+" 00:00:00";
	}



	/**
	 * 获取今天的结束时间
	 * @param today
	 * @return 格式:YYYY-MM-DD HH:MM:SS
	 * @throws Exception
	 */
	public static String getTodayToTime(Calendar today) throws Exception{
		SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormatDate.format(today.getTime())+" 23:59:59";
	}


	/**
	 * 计算两个日期相差月份或者天数
	 * @param date1
	 * @param date2
	 * @param sdfStr
	 * @return
	 */
	public static List<String> getBetween(long date1, long date2, String sdfStr){
		SimpleDateFormat sdf = new SimpleDateFormat(sdfStr);// 格式化为年月

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTimeInMillis(date1);// 开始日期
		c2.setTimeInMillis(date2);// 结束日期

		// 定义集合存放月份
		List<String> list = new ArrayList<String>();

		Calendar curr = c1;
		long currMills = curr.getTimeInMillis();
		long c2mills = c2.getTimeInMillis();
		while (currMills <= c2mills) {
			if (sdfStr.equalsIgnoreCase("yyMM")) {
				list.add(sdf.format(curr.getTime()) + "00");
			}else {
				list.add(sdf.format(curr.getTime()));
			}
			if (sdfStr.indexOf("dd") > 0) {
				curr.add(Calendar.DAY_OF_MONTH, 1);
			}else {
				curr.add(Calendar.MONTH, 1);
			}
			currMills = curr.getTimeInMillis();
		}

		if(!list.isEmpty()){
			if (!list.get(list.size() - 1).equals(sdf.format(c2.getTime()))) {
				String nextday = "";
				if (sdfStr.equalsIgnoreCase(FORMAT_YYMM)) {
					nextday = (sdf.format(c2.getTime()) + "00");
				}else {
					nextday = sdf.format(c2.getTime());
				}
				if (!list.contains(nextday)) {
					list.add(nextday);
				}
			}
		}

		return list;
	}


	/**
	 * 获取每个月第一天的毫秒数
	 * @param amount
	 * @return
	 */
	public static long getFirstDayMillsOfMonth(int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, amount);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取索引时间范围列表
	 * 返回集合时间格式yyMMdd
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDateList(String startTime, String endTime) {

		List<String> list = new ArrayList<>();
		int days = timeStrDiffToDay(startTime, endTime);
		String dateTmp = startTime;
		for(int i = 0;i<days;i++) {
			String dataNextDay = getDataNextDay(dateTmp, "yyyy-MM-dd", 1);
			long l = StrLong2Millis(dataNextDay + " 00:00:00");
			final String yyMMdd = Millis2FormatStr(l, "yyMMdd");
			list.add(yyMMdd);
			dateTmp = dataNextDay;
		}
		return list;
	}


}
