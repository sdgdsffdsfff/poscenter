package mmb.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class DateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM");
	private static 	DateFormat TimeFormat = new SimpleDateFormat("HH:mm");
	private static SimpleDateFormat sdf2 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static SimpleDateFormat sdfChinese = new SimpleDateFormat("M月d日");
	
	public static String[] weekdayName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	public static void main(String[] args) {
		try {
			Date d = sdf.parse("2012-06-09");
			System.out.println(calculateMillis(d, "09:00", 35160000));
			System.out.println(transMillisToTime(35160000));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 计算两个时间的毫秒数
	 * 
	 * @param str1
	 *            日期 yyyy-MM-dd
	 * @param str2
	 *            HH:mm 比较时间 基准时间是00:00
	 * @return
	 */
	public static int calculateMillis(String str1, String str2) {
		int result = 0;
		try {
			Date d1 = sdf.parse(str1 + " 00:00");
			Date d2 = sdf2.parse(str1 + " " + str2);
			result = (int) (d2.getTime() - d1.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 计算两个时间的分钟数
	 * 
	 * @param date
	 *            日期 yyyy-MM-dd
	 * @param time1
	 *            HH:mm 比较时间 基准时间是time1
	 * @param time2
	 * @return
	 */
	public static int calculateMillis(Date date, String time1, int time2) {
		int result = 0;
		try {
			Date d = sdf2.parse(sdf.format(date) + " 00:00");
			Date d1 = sdf2.parse(sdf.format(date) + " " + time1);
			int diff = (int) (d1.getTime() - d.getTime());
			result = (time2 - diff) / (1000 * 60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String transMillisToTime(int time) {
		int sumM = time / (1000 * 60);
		int h = sumM / 60;
		int m = sumM % 60;
		StringBuilder result = new StringBuilder();
		if (h < 10) {
			result.append("0");
		}
		result.append(h).append(":");
		if (m < 10) {
			result.append("0");
		}
		result.append(m);
		return result.toString();
	}

	/**
	 * 获取当前月的日期列表,yyyy-MM-dd
	 * 
	 * @return
	 */
	public static List<String> getMonthDayList(int year, int month) {
		Calendar c = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		try {
			date = dateFormat.parse(year + "-" + month);
		} catch (ParseException e) {
			date = new Date();
		}
		c.setTime(date);
		int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= days; i++) {
			c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), i);
			Date d = c.getTime();
			list.add(sdf.format(d));
		}
		return list;
	}

	/**
	 * 根据日期yyyy-MM-dd获得星期数
	 * 
	 * @param temp
	 * @return
	 */
	public static String getWeekIndex(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		int week = c.get(Calendar.DAY_OF_WEEK);
		return transIntToStr(week);
	}

	/**
	 * 根据传入的月份的index，得到整个月的星期列表
	 * 
	 * @param month
	 */
	public static String[] getWeekendList(int year, int month) {
		String[] weeks = null;
		if (month == -1) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		int days = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		weeks = new String[days];
		for (int i = 0; i < days; i++) {
			c.set(Calendar.DAY_OF_MONTH, i + 1);
			weeks[i] = transIntToStr(c.get(Calendar.DAY_OF_WEEK));
		}
		return weeks;
	}

	private static String transIntToStr(int week) {
		String index = null;
		switch (week) {
		case Calendar.MONDAY:
			index = "一";
			break;
		case Calendar.TUESDAY:
			index = "二";
			break;
		case Calendar.WEDNESDAY:
			index = "三";
			break;
		case Calendar.THURSDAY:
			index = "四";
			break;
		case Calendar.FRIDAY:
			index = "五";
			break;
		case Calendar.SATURDAY:
			index = "六";
			break;
		case Calendar.SUNDAY:
			index = "日";
			break;
		default:
			break;
		}
		return index;
	}

	/**
	 * 获得当前月份的index
	 * 
	 * @param temp
	 * @return
	 */
	public static int getMonthIndex(String temp) {
		int index = -1;
		try {
			Date d = sdf.parse(temp);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			index = c.get(Calendar.MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return index;
	}


	/**
	 * 判断是否为周六周天 true 工作日 false 周末休息日
	 * 
	 * @param datefrom
	 *            格式为"yyyy-MM-dd"
	 * @return
	 */
	public static boolean isWeekend(String date) {	// TODO: weekend是周末的意思
		boolean result = false;
		try {
			Date dateFrom = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			// 设置日期
			cal.setTime(dateFrom);
			if ((cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
					&& (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
				result = true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	// 判断是工作日
	public static boolean isWorkday(Calendar cal) {
		boolean result = false;
		int week = cal.get(Calendar.DAY_OF_WEEK);
		if ((week != Calendar.SATURDAY)
				&& (week != Calendar.SUNDAY)) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断startDate是否在endDate之前
	 * 
	 * @param startDate
	 * @param endDate
	 * @return true 在之前，false在之后
	 */
	public static boolean isBefore(String startDate, String endDate) {
		try {
			Calendar cs = Calendar.getInstance();
			cs.setTime(sdf.parse(startDate));
			Calendar ce = Calendar.getInstance();
			ce.setTime(sdf.parse(endDate));
			return cs.before(ce);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String formatDateChinese(Date date) {
		String ret;
		try {
			ret = sdfChinese.format(date);
		} catch (Exception e) {
			return "";
		}

		return ret;
	}
	
	public static String formatDate(Date date, String format){
		String ret = "";

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			ret = sdf.format(date);
		}catch(Exception e)
		{}

		return ret;
	}

	public static long parseDate(String s) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(s).getTime();
		} catch (Exception e) {
			// e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 计算date2与date1的月份差数
	 * 
	 * @param date1
	 * @param date2
	 *            时间在date1之后
	 * @return
	 */
	public static int getMonthDiff(Date date1, Date date2) {
		int diff = 0;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		diff = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		return diff;
	}

	// 计算两个时间相差多少年
	public static int calcYearDiff(long time1, long time2) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time1);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		int year2 = cal2.get(Calendar.YEAR);
		int month2 = cal2.get(Calendar.MONTH);
		int day2 = cal2.get(Calendar.DAY_OF_MONTH);

		if (month < month2 || month == month2 && day <= day2)
			return year2 - year;
		else
			return year2 - year - 1;
	}

	public static boolean Compare(String lastnight,String comparetime){
		Date a = null;
		Date b = null;
		boolean result=false;
		try {
			a = TimeFormat.parse(lastnight);
			b = TimeFormat.parse(comparetime);
			if(b.before(a)){
				result= true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断两个日期是否为同一天
	 * 
	 * @param d1
	 *            日期一
	 * @param d2
	 *            日期二
	 * @return 同一天true，不是同一天false
	 */
	public static boolean isSameDate(Calendar c1, Calendar c2) {
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.get(Calendar.DAY_OF_MONTH) == c2
						.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 判断是否是同一个月
	 * 
	 * @param Calendar
	 *            c1
	 * @param Calendar
	 *            c2
	 * @return
	 */
	public static boolean isSameMonth(Calendar c1, Calendar c2) {
		return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
	}
}
