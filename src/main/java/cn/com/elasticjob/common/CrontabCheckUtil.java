package cn.com.elasticjob.common;

import cn.com.elasticjob.constants.GlobalConstant;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author caijinpeng
 * @Description OrderExceptionQuartz
 * @date 2020/4/23 17:07
 * @Version 1.0
 */
public class CrontabCheckUtil {
	private static final Logger log = LoggerFactory.getLogger(CrontabCheckUtil.class);


	/**
	 * 从crontab中获取分钟信息
	 * @author caijinpeng
	 * @date 2020/4/23 17:38
	 * @param taskInterval
	 * @return
	 */
	public static int getMinFromCrontab(String taskInterval){
		//设置采集频率
		int frequency = 3;
		String strMin = taskInterval;
		if(StringUtils.isNotEmpty(taskInterval) && taskInterval.indexOf(GlobalConstant.DH) > -1){
			//可能是Crontab表达式，目前暂时解析取分钟位置值
			String[] cron = taskInterval.split(GlobalConstant.DH);
			if(cron != null && cron.length > 1){
				strMin = cron[1];
			}
		}

		try{
			//只取分钟的位置值
			frequency = Integer.parseInt(strMin);
			if(frequency <= 0){
				frequency = 3;
			}
		} catch (Exception e){
			log.warn("TaskInterval parse to int error, taskInterval=" + taskInterval);
		}

		return frequency;
	}

	/**
	 * 判断是都是Crontab
	 */
	public static boolean isCrontab(String strInterval){
		boolean bCrontab = false;
		try {
			if(null==strInterval || StringUtils.isBlank(strInterval)){
				return false;
			}

			Integer.parseInt(strInterval.trim());
			bCrontab = false;
		} catch(Exception ex) {
			if(strInterval.contains(GlobalConstant.XH) || strInterval.contains(GlobalConstant.XG) || strInterval.contains(GlobalConstant.ZHX) || strInterval.contains(GlobalConstant.DH)){
				bCrontab = true;
			}
		}

		return bCrontab;
	}

	/**
	 * 校验在当前时间是否满足cron时间规则表达式
	 * @param cron
	 * @param currTime
	 * @return
	 * @throws ParseException
	 */
	public static Boolean filterWithCronTime(String cron, long currTime, long lastCollecTime) {
		if (StringUtils.isBlank(cron) || currTime == 0L){
			return false;
		}

		Boolean inCron = false;
		try {
			Calendar currCalendar = Calendar.getInstance();
			currCalendar.setTimeInMillis(currTime);
			currCalendar.set(Calendar.MILLISECOND, 0);

			Calendar lastCalendar = Calendar.getInstance();
			lastCalendar.setTimeInMillis(lastCollecTime);
			lastCalendar.set(Calendar.MILLISECOND, 0);
			Date lastDate = lastCalendar.getTime();

			CronExpression exp = new CronExpression(cron);
			Date nextDate = exp.getNextValidTimeAfter(lastDate);

			int minuteInterval = -1;
			String[] summary = exp.getExpressionSummary().split("\n");
			String minutesLine = summary[1].replace("minutes: ", "");
			if(minutesLine.contains(",")){
				String[] minutesArray = minutesLine.split(",");
				minuteInterval = ConvertUtil.Obj2int(minutesArray[1]) - ConvertUtil.Obj2int(minutesArray[0]);
			}
			if(minuteInterval > -1){
				lastCalendar.add(Calendar.MINUTE, minuteInterval);
				nextDate = lastCalendar.getTime();
			}

			if(nextDate != null){
				long nextTime = nextDate.getTime();
				//if(DynamicSwitchReadUtil.taskCollectPrintFlag()) {
					log.info("cron:{}, currTime:{}, lastTime:{}, nextTime:{}", cron, currCalendar.getTimeInMillis(), lastCalendar.getTimeInMillis(), nextTime);
				//}
				if( nextTime <= currCalendar.getTimeInMillis()){
					inCron = true;
				}
			}
		} catch(ParseException ex) {
			inCron = false;
			log.info("filterWithCronTime() false, cron:{}, currTime:{}, lastCollecTime:{}", cron, currTime, lastCollecTime);
			ex.printStackTrace();
		}
		return inCron;
	}


	public static void main(String[] args) throws ParseException {
		String cron = "0 */5 * * * ?";
		//true，我当前时间为15:36，
		Calendar currCalendar = Calendar.getInstance();
		currCalendar.set(Calendar.MILLISECOND, 0);
		long currentTime = currCalendar.getTimeInMillis();
		long lastTime = currentTime - 3 * 60 * 1000L;
		System.out.println("----currentTime:"+currentTime+ ", lastTime:"+lastTime);
		System.out.println(filterWithCronTime(cron, currentTime, lastTime));

	}

}
