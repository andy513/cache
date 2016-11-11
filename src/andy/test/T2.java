package andy.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import andy.memcache.XMemcacheUtil;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * @author Andy<andy_513@163.com>
 */
public class T2 {

	private static final ExecutorService es = Executors.newFixedThreadPool(20);
	private static final Lock lock = new ReentrantLock();

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			XMemcacheUtil.flushAll();
			extracted();
		}
		System.out.println(System.currentTimeMillis() - time);
		System.out.println("T2:\tGame Over!");
	}

	private static void extracted() throws TimeoutException, InterruptedException, MemcachedException {
		String key = "a";
		XMemcacheUtil.set(key, 0);
		int count = 200;
		CountDownLatch cdl = new CountDownLatch(count * 2);
		for (int i = 0; i < count; i++) {
			es.execute(() -> {
				Lock l = lock;
				Integer currentValue;
				l.lock();
				try {
					currentValue = XMemcacheUtil.get(key);
					currentValue++;
					XMemcacheUtil.set(key, currentValue);
					cdl.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					l.unlock();
				}
			});
			es.execute(() -> {
				Lock l = lock;
				int currentValue;
				l.lock();
				try {
					currentValue = XMemcacheUtil.get(key);
					currentValue -= 2;
					XMemcacheUtil.set(key, currentValue);
					cdl.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					l.unlock();
				}
			});
		}
		cdl.await();
		int value = XMemcacheUtil.get(key);
		if (value != -200) {
			System.out.println(value);
		}
	}

}
