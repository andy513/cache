package andy.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import andy.entity.User;

/**
 * @author Andy<andy_513@163.com>
 */
public class Test4 {
	static AtomicReference<User> atomicUser = new AtomicReference<User>();

	private static final Lock l = new ReentrantLock();

	public static void main(String[] args) {
		l.lock();
		final Integer num = 1;
		try {
			new Thread(()->{
				try {
					int a = num;
					TimeUnit.MILLISECONDS.sleep(1000);
					System.out.println(++a + "\ta");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
			System.out.println(num + "\ta");
		} finally {
			l.unlock();
		}

	}

}
