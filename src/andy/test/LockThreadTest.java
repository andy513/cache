package andy.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import andy.common.ExceptionUtil;

/**
 * @author Andy<andy_513@163.com>
 */
public class LockThreadTest {

	public static void main(String[] args) {
		Lock l1 = new ReentrantLock();
		Lock l2 = new ReentrantLock();
		Object o1 = new Object();
		Object o2 = new Object();
		CountDownLatch count = new CountDownLatch(2);
		try {
			new Thread(() -> {
				count.countDown();
				l1.lock();
				for (int i = 0; i < 10; i++) {
					System.out.println(Thread.currentThread().getName() + "\t" + o1.toString());
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				l2.lock();
				for (int i = 0; i < 10; i++) {
					System.out.println(Thread.currentThread().getName() + "\t" + o2.toString());
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName() + "完成");
				l1.unlock();
				l2.unlock();
			}).start();
			new Thread(() -> {
				count.countDown();
				l1.lock();
				for (int i = 0; i < 10; i++) {
					System.out.println(Thread.currentThread().getName() + "\t" + o2.toString());
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				l2.lock();
				for (int i = 0; i < 10; i++) {
					System.out.println(Thread.currentThread().getName() + "\t" + o1.toString());
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName() + "完成");
				l1.unlock();
				l2.unlock();
			}).start();
			count.await();
		} catch (Exception e) {
			ExceptionUtil.printStackTrace(e);
		} finally {
			System.out.println("Game Over!");
			/*l1.unlock();
			l2.unlock();*/
		}
	}

}
