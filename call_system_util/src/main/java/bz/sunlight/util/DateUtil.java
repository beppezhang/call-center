package bz.sunlight.util;






import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	//日期转字符串
	public static String dateToString(Date date,String format){
		if(date!=null){
			
			SimpleDateFormat sdf=new SimpleDateFormat(format);
			String str = sdf.format(date);
			return str;
		}
		return "";
		
	}
	
	//字符串转日期格式
	public static Date stringToDate(String str,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		Date date=null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("日期转化出错");
			e.printStackTrace();
		}
		return date;
		
	}
	
	
	
    /**
     * 判断两个日期是否是同一天
     * 
     * @param Date
     *            date1
     * @param Date
     *            date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                        .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }
    
    /**
     * 当天开始时间
     * @param Date date
     * @return Date
     */
    public static Date startOfCurrentDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 当天的结束时间
     * @param Date date
     * @return Date
     */
    public static Date endOfCurrentDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
    }

	
	
}
