package andy.memcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSONObject;

import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * @author Andy<andy_513@163.com>
 */
public class XMemcacheCache {

	private static final ExecutorService es = Executors.newFixedThreadPool(4);
	private static final ExecutorService es1 = Executors.newFixedThreadPool(6);

	public static void main(String[] args) throws Exception {
		XMemcacheUtil.flushAll();
		long time = System.currentTimeMillis();
		// TimeUnit.MILLISECONDS.sleep(1000);
		int size = 50000;
		XMemcachedThread1 t1 = new XMemcachedThread1();
		XMemcachedThread2 t2 = new XMemcachedThread2();
		for (int i = 0; i < size; i++) {
			es.execute(t1);
			es.execute(t2);
		}
		System.out.println(System.currentTimeMillis() - time);
		// CountDownLatch cdl = new CountDownLatch(size);
		// List<Integer> lists = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			final int a = i;
			es1.execute(new Runnable() {
				@Override
				public void run() {
					Integer num = null;
					String key = null;
					try {
						key = "a" + a;
						num = XMemcacheUtil.get(key);
						if (num != null) {
							System.out.println(key + "\t" + num);
							XMemcacheUtil.set(key, num);
						}
						key = "b" + a;
						num = XMemcacheUtil.get(key);
						if (num != null) {
							System.out.println(key + "\t" + num);
							XMemcacheUtil.set(key, num);
						}
					} catch (TimeoutException | InterruptedException | MemcachedException e) {
						e.printStackTrace();
					}
				}
			});
		}
		// cdl.await();
		// TimeUnit.MILLISECONDS.sleep(1000 * 1);
		System.out.println(System.currentTimeMillis() - time);
		// System.out.println(map.size()/* + "\t" + JSONObject.toJSONString(map)
		// */);
	}

}

class XMemcachedThread1 implements Runnable {
	private static final AtomicInteger ai = new AtomicInteger(0);

	/*
	 * CountDownLatch cdl;
	 * 
	 * public XMemcachedThread1(CountDownLatch cdl) { this.cdl = cdl; }
	 */
	// static ThreadLocal<AtomicInteger> local = new
	// ThreadLocal<AtomicInteger>();

	@Override
	public void run() {
		try {
			int i = ai.getAndIncrement();
			String key = "a" + i;
			// System.out.println(key + "\t" + i);
			XMemcacheUtil.set(key, i);
			// cdl.countDown();
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			e.printStackTrace();
		}
	}

}

class XMemcachedThread2 implements Runnable {

	private static final AtomicInteger ai = new AtomicInteger(0);

	/*
	 * CountDownLatch cdl;
	 * 
	 * public XMemcachedThread2(CountDownLatch cdl) { this.cdl = cdl; }
	 */

	// ThreadLocal<AtomicInteger> local = new ThreadLocal<AtomicInteger>();

	@Override
	public void run() {
		try {
			int i = ai.getAndIncrement();
			String key = "b" + i;
			// System.out.println(key + "\t" + i);
			XMemcacheUtil.set(key, i);
			// cdl.countDown();
		} catch (TimeoutException | InterruptedException | MemcachedException e) {
			e.printStackTrace();
		}
	}

}
