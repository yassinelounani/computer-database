package wrappers;


import java.sql.Date;
import java.time.LocalDate;

public class HelperDate {
	
	public static Date dateToSql(LocalDate date) {
		if(date == null ) return null;
		return Date.valueOf(date);
	}
}
