package andy.test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import andy.entity.User;

/**
 * @author Andy<andy_513@163.com>
 */
public class ThreadTest {

	public static final Map<String, List<User>> map = new ConcurrentHashMap<String, List<User>>();
	private static final ExecutorService es = Executors.newFixedThreadPool(10);
	public static final BlockingQueue<User> quque = new LinkedBlockingQueue<User>();

	public static void main(String[] args) throws InterruptedException {
//		CyclicBarrier cyclic = new CyclicBarrier(10);
//		a();
		LocalDateTime ldt = LocalDateTime.now();
//		TimeUnit.SECONDS.sleep(3);
		ldt = ldt.withSecond(ldt.getSecond() - 1);
		long second = Duration.between(ldt, LocalDateTime.now()).getSeconds();
		System.out.println(second);
	}

	private static void a() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			es.execute(new Test3());
		}
		int a = 1;
		User user = null;
		for (int i = 0; i < 100; i++) {
			if (i % 50 == 0) {
				user = new User(i, "" + i);
				a++;
			}
			quque.put(user);
		}
		TimeUnit.SECONDS.sleep(30);
		List<User> users = new ArrayList<>();
		for (Entry<String, List<User>> entry : map.entrySet()) {
			users.addAll(entry.getValue());
		}
		Collections.sort(users, (User u1, User u2) -> compale(Long.parseLong(u1.getPassword()), Long.parseLong(u2.getPassword())));
		System.out.println("排序完成" + users.size());
		for (int j = 0; j < users.size(); j++) {
			User u1 = users.get(j);
			if (j == 99)
				continue;
			User u2 = users.get(j + 1);
			System.out.println(u1.getId() + "-" + u2.getId() + "\t" + (Long.parseLong(u2.getPassword()) - Long.parseLong(u1.getPassword())));
		}
	}

	private static final int compale(long a, long b) {
		return a < b ? 1 : a > b ? -1 : 0;
	}
}

class Test3 implements Runnable {

//	CountDownLatch count;
//	CyclicBarrier cyclic;
	ThreadLocal<Integer> local = new ThreadLocal<>();

	/*public Test3(CountDownLatch count) {
		this.count = count;
	}*/

	@Override
	public void run() {
		while (true) {
			User user = null;
			try {
				user = ThreadTest.quque.take();
				synchronized (user) {
					try {
						TimeUnit.MILLISECONDS.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Integer i = local.get();
					i = i == null ? 1 : ++i;
					local.set(i);
					 System.out.println(user.getId() + "\t" +System.currentTimeMillis());
					List<User> lists = ThreadTest.map.get(Thread.currentThread().getName());
					if (lists == null) {
						lists = new ArrayList<>();
						ThreadTest.map.put(Thread.currentThread().getName(), lists);
					}
					user.setPassword(System.nanoTime() + "");
					lists.add(user);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// System.out.println(Thread.currentThread().getName() + "\t" + i);
	}
}

class Test2 implements Runnable {

	AtomicInteger ai;
	int num;
	CyclicBarrier cb;
	ThreadLocal<Integer> local = new ThreadLocal<>();

	public Test2(AtomicInteger ai, int num, CyclicBarrier cb) {
		this.ai = ai;
		this.num = num;
		this.cb = cb;
	}

	public void run() {
		try {
			cb.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
		if (local.get() == null) {
			System.out.println(num);
			local.set(num);
		} else {
			num = local.get();
			local.set(++num);
		}
		System.out.println(local.get() + "\t" + ai.getAndIncrement());
		System.out.println(System.currentTimeMillis());
	}
}

class Test1 implements Runnable {

	AtomicInteger ai;
	int num;
	CountDownLatch count;
	ThreadLocal<Integer> local = new ThreadLocal<>();

	public Test1(AtomicInteger ai, int num, CountDownLatch count) {
		this.ai = ai;
		this.num = num;
		this.count = count;
	}

	@Override
	public void run() {
		if (local.get() == null) {
			local.set(num);
		} else {
			num = local.get();
			local.set(num++);
		}
		System.out.println(local.get() + "\t" + ai.getAndIncrement());
		System.out.println(System.currentTimeMillis());
		count.countDown();
	}

}