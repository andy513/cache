package andy.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSONObject;

import andy.memcache.XMemcacheUtil;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * @author Andy<andy_513@163.com>
 */
public class RedisTest {

	private static final ExecutorService es = Executors.newFixedThreadPool(1);
	private static final ExecutorService es1 = Executors.newFixedThreadPool(10);

	public static void main(String[] args) throws Exception {
//		XMemcacheUtil.flushAll();
		int size = 100;
		// CountDownLatch count = new CountDownLatch(size * 2);
		XMemcachedThread1 t1 = new XMemcachedThread1();
		XMemcachedThread2 t2 = new XMemcachedThread2();
		/*
		 * Thread t1 = new Thread(new XMemcachedThread1()); Thread t2 = new
		 * Thread(new XMemcachedThread2());
		 */
		for (int i = 0; i < size; i++) {
			es.execute(t1);
			es.execute(t2);
		}
		// count.await();
		TimeUnit.MILLISECONDS.sleep(1000 * 3);
		CountDownLatch cdl = new CountDownLatch(size);
		// List<Integer> lists = new ArrayList<Integer>();
		Map<String, Integer> map = new ConcurrentHashMap<>();
		for (int i = 0; i < size; i++) {
			final int a = i;
			es1.execute(new Runnable() {
				@Override
				public void run() {
					Integer num = null;
					String key = null;
					try {
						key = "a" + a;
						num = RedisByteCache.get(key, Integer.class);
						if (num != null) {
							map.put(key, num);
						}
						key = "b" + a;
						num = RedisByteCache.get(key, Integer.class);
						if (num != null) {
							map.put(key, num);
						}
					} finally {
						cdl.countDown();
					}
				}
			});
		}
		cdl.await();
		// TimeUnit.MILLISECONDS.sleep(1000 * 1);
		System.out.println(map.size() + "\t" + JSONObject.toJSONString(map));
	}

}

class XMemcachedThread1 implements Runnable {

	private static final Lock lock = new ReentrantLock();
	/*
	 * CountDownLatch cdl;
	 * 
	 * public XMemcachedThread1(CountDownLatch cdl) { this.cdl = cdl; }
	 */
	static ThreadLocal<AtomicInteger> local = new ThreadLocal<AtomicInteger>();

	@Override
	public void run() {
		Lock l = lock;
		try {
			l.lock();
			if (local.get() == null) {
				// AtomicInteger ai = local.get();
				System.out.println(Thread.currentThread().getName() + "\ttest");
				local.set(new AtomicInteger(0));
			}
			AtomicInteger ai = local.get();
			int i = ai.getAndIncrement();
			String key = "a" + i;
			// System.out.println(key + "\t" + i);
			RedisByteCache.set(key, i);
			// cdl.countDown();
		} finally {
			l.unlock();
		}
	}

}

class XMemcachedThread2 implements Runnable {

	private static final Lock lock = new ReentrantLock();
	/*
	 * CountDownLatch cdl;
	 * 
	 * public XMemcachedThread2(CountDownLatch cdl) { this.cdl = cdl; }
	 */

	ThreadLocal<AtomicInteger> local = new ThreadLocal<AtomicInteger>();

	@Override
	public void run() {
		Lock l = lock;
		try {
			l.lock();
			if (local.get() == null) {
				// AtomicInteger ai = local.get();
				System.out.println(Thread.currentThread().getName() + "\ttest");
				local.set(new AtomicInteger(0));
			}
			AtomicInteger ai = local.get();
			int i = ai.getAndIncrement();
			String key = "b" + i;
			// System.out.println(key + "\t" + i);
			RedisByteCache.set(key, i);
			// cdl.countDown();
		} finally {
			l.unlock();
		}
	}

}