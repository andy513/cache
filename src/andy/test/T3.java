package andy.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import andy.memcache.XMemcacheUtil;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * @author Andy<andy_513@163.com>
 */
public class T3 {

	private static final ExecutorService es = Executors.newFixedThreadPool(20);

	public static void main(String[] args) throws Exception {
		long time = System.currentTimeMillis();
		XMemcacheUtil.flushAll();
		for (int i = 0; i < 100; i++) {
			extracted();
		}
		System.out.println(System.currentTimeMillis() - time);
		System.out.println("T3:\tGame Over!");
	}

	private static void extracted() throws InterruptedException, TimeoutException, MemcachedException {
		String cas = "a";
		String key = "b";
		int count = 200;
		CountDownLatch cdl = new CountDownLatch(count * 2);
		XMemcacheUtil.set(key, 0);
		for (int i = 0; i < count; i++) {
			es.execute(() -> {
				try {
					// while (true) {
					while (!XMemcacheUtil.add(cas, true)) {
						TimeUnit.MILLISECONDS.sleep(5);
						continue;
					}
					Integer a = XMemcacheUtil.get(key);
					XMemcacheUtil.set(key, ++a);
					if (!XMemcacheUtil.del(cas)) {
						System.out.println("???????");
						XMemcacheUtil.del(cas);
					}
					/*
					 * break; }
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}
				cdl.countDown();
			});
			es.execute(() -> {
				try {
					// while (true) {
					while (!XMemcacheUtil.add(cas, true)) {
						TimeUnit.MILLISECONDS.sleep(5);
						continue;
					}
					Integer a = XMemcacheUtil.get(key);
					XMemcacheUtil.set(key, a -= 2);
					if (!XMemcacheUtil.del(cas)) {
						System.out.println("???????");
						XMemcacheUtil.del(cas);
					}
					// break;
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
				cdl.countDown();
			});
		}
		cdl.await();
		int num = XMemcacheUtil.get(key);
		if (num != -200) {
			System.out.println(num);
		}
	}

}
