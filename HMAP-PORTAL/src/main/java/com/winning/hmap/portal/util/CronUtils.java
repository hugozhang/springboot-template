package com.winning.hmap.portal.util;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class CronUtils {

	/**
	 * 返回执行Cron
	 * @param jhlx：计划类型（d：日计划      w：周计划       m：月计划）
	 * @param jh_z：周, jh_r:日， jh_s：时，jh_f：分，jh_m：秒
	 * @return Cron
	 */
	public static String getCron(String jhlx, 
								 String jh_z, 
								 String jh_r, 
								 String jh_s, 
								 String jh_f, 
								 String jh_m) {
		
		//日计划
		if ("d".equals(jhlx)) {
			return jh_m + " " + jh_f + " " + jh_s + " * * ?";
			
		//周计划
		} else if ("w".equals(jhlx)) {
			switch (jh_z) {
				case "1" : return jh_m + " " + jh_f + " " + jh_s + " ? * MON";
				case "2" : return jh_m + " " + jh_f + " " + jh_s + " ? * TUE";
				case "3" : return jh_m + " " + jh_f + " " + jh_s + " ? * WED";
				case "4" : return jh_m + " " + jh_f + " " + jh_s + " ? * THU";
				case "5" : return jh_m + " " + jh_f + " " + jh_s + " ? * FRI";
				case "6" : return jh_m + " " + jh_f + " " + jh_s + " ? * SAT";
				case "7" : return jh_m + " " + jh_f + " " + jh_s + " ? * SUN";
			}

		//月计划
		} else if ("m".equals(jhlx)) {
			return jh_m + " " + jh_f + " " + jh_s + " " + jh_r +" * ?";
		}
		
		return null;
	}


	/**
	 * 检查cron表达式的合法性
	 *
	 * @param cron cron exp
	 * @return true if valid
	 */
	public static boolean checkValid(String cron) {
		try {
			CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
			CronParser parser = new CronParser(cronDefinition);
			parser.parse(cron);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param cronExpression cron表达式
	 * @param numTimes       下一(几)次运行的时间
	 * @return
	 */
	public static List<Date> getNextExecTime(String cronExpression, Integer numTimes) throws ParseException {
		CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
		cronTriggerImpl.setCronExpression(cronExpression);
		// 这个是重点，一行代码搞定
		return TriggerUtils.computeFireTimes(cronTriggerImpl, null, numTimes);
	}
}
