package com.openai.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtils {
	Logger logger = LoggerFactory.getLogger(TimeUtils.class);

	public static Date addSevenDays() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DATE, 7);
		return calendar.getTime();
	}

	public static Date getNextDateWithOutTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static String getUnixTimestamp(String input) {
		int hours = 2;
		try {
			hours = Integer.valueOf(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		double currentTimeMillies = (double) (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(hours));
		currentTimeMillies = (currentTimeMillies / 1000);
		return String.valueOf(String.format("%.6f", currentTimeMillies));
	}
	
	public static Date getLastDateOfPreviousMonth() {
		Calendar aCalendar = Calendar.getInstance();
		// add -1 month to current month
		aCalendar.add(Calendar.MONTH, -1);
		// set actual maximum date of previous month
		aCalendar.set(Calendar.DATE,     aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		//read it
		return aCalendar.getTime();
	}
}