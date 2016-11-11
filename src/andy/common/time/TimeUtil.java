package andy.common.time;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.Year;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * @author Andy<andy_513@163.com>
 */
public final class TimeUtil {

	public static final LocalDate testLocalDate() {
		LocalDate localDate = LocalDate.now();
		System.out.println("day of week \t\t" + localDate.getDayOfWeek());
		System.out.println("day of month \t\t" + localDate.getDayOfMonth());
		System.out.println("day of year \t\t" + localDate.getDayOfYear());
		System.out.println("length of month \t" + localDate.lengthOfMonth());
		System.out.println(localDate.getYear());
		System.out.println(localDate.getMonthValue());
		System.out.println(localDate.getMonth().maxLength());
		System.out.println(localDate.getMonth().minLength());
		System.out.println(localDate.getMonth().ordinal());
		return localDate;
	}

	public static final LocalTime testLocalTime() {
		LocalTime localTime = LocalTime.now();
		System.out.println(localTime.getHour());
		localTime = localTime.plusHours(-1);
		return localTime;
	}

	public static final LocalDateTime testLocalDateTime() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime;
	}

	public static void main(String[] args) {
		LocalDate now = LocalDate.now();
		int num = now.lengthOfMonth();
		System.out.println(num);
		LocalDate ld = LocalDate.of(2017, 5, 14);
//		long duration = Duration.between(now,ld).toDays();
		Period period = Period.between(now, ld);
		System.out.println(now + "\t" + ld + "\t" + period.getDays() +"\t"+period.getMonths()+"\t"+period.getYears());
		System.out.println(testLocalDate());
//		YearMonth ym;
		Year y = Year.now();
		System.out.println(y.getValue());
		System.out.println(testLocalTime());
	}

}
