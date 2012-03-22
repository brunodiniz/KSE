package br.ucam.kuabaSubsystem.kuabaModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateParser {
	
	public static String xmlFormat = "yyyy-MM-dd'T'HH:mm:ss";
	
	private SimpleDateFormat format;

	public DateParser(String format) {
		super();
		if(!format.equals(""))
			this.format = new SimpleDateFormat(format);
		else
			this.format = new SimpleDateFormat(DateParser.xmlFormat);
	}
	
	
	
	public DateParser() {
		super();
		this.format = new SimpleDateFormat(DateParser.xmlFormat);
	}

	public GregorianCalendar toGregorianCalendar(String dateToParse)
	throws ParseException
	{
		
		Date date = this.format.parse(dateToParse);		
		GregorianCalendar gregorianDate = new GregorianCalendar();
		gregorianDate.setTime(date);
        return gregorianDate;
        
	}
	
	public String toStringFormat(GregorianCalendar dateToParse){
		
		//generating a string on format: "yyyy-MM-dd'T'HH:mm:ss"
    	String formatedDate = this.format.format( dateToParse.getTime());
    	return formatedDate;

	}

	public SimpleDateFormat getFormat() {
		return format;
	}

	public void setFormat(SimpleDateFormat format) {
		this.format = format;
	}
	
}
