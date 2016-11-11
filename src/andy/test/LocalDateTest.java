package andy.test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Andy<andy_513@163.com>
 */
public class LocalDateTest {
	
	public static void main(String[] args) {
		LocalDateTime ldt = LocalDateTime.now();
		Timestamp timestamp = Timestamp.valueOf(ldt);
		System.out.println(timestamp.getTime());
	}

}
