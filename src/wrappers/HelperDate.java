package wrappers;


import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class HelperDate {
	
	public static Date dateToSql(LocalDate date) {
		if(date == null ) return null;
		return Date.valueOf(date);
	}
	
	public static LocalDate StringDateToLocalDate(String date) {
		if(date.isEmpty()) return null;
		if(date.matches("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
			return LocalDate.parse(date, formatter);
		}
		return LocalDate.parse(date);
	}
	
	public static LocalDate sqlDateToLocalDate(Date date) {
        return Optional.ofNullable(date)
          .map(Date::toLocalDate)
          .orElse(null);
    }
}
