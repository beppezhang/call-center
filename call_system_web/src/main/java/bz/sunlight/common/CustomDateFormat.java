package bz.sunlight.common;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

import bz.sulight.common.BaseConstant;

//  前台日期字符串提交到后台的转化
public class CustomDateFormat implements Formatter<Date>{

	@Override
	public String print(Date object, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat(BaseConstant.DATE_FORMAT_YEAR_TO_SECOND, locale);
		return sdf.parse(text);
		
	}

}
