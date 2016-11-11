package andy.common.factory.test8;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * @author Andy<andy_513@163.com>
 */
public class ConcurrentSet {

	private AtomicInteger ai;
	private static final int LEN = 1000;

	public ConcurrentSet() {
		ai = new AtomicInteger(0);
	}

	private AtomicIntegerArray reference = new AtomicIntegerArray(LEN);
//	private AtomicReferenceArray<E> reference = new AtomicReferenceArray<E>(LEN);

	public void put(Integer e) {
		/*
		 * if(ai.get() >= reference.length()){ reference = new
		 * AtomicReferenceArray<>(LEN * 2); Arrays }
		 */
		// AtomicReferenceArray.
		reference.set(ai.getAndIncrement(), e);
	}

	public Integer get(int i) {
		return reference.get(i);
	}

	public void set(int i, Integer e) {
		reference.set(i, e);
	}

	public int size() {
		return ai.get();
	}
	
	private static final ExecutorService es = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws InterruptedException {
		long time = Instant.now().toEpochMilli();
		int size = 10000;
		CountDownLatch cdl = new CountDownLatch(size);
		for (int k = 0; k < size; k++) {
			es.execute(()->{
				extracted();
				cdl.countDown();
			});
		}
		cdl.await();
		System.out.println(Instant.now().toEpochMilli() - time);
	}

	private static void extracted1() {
		int[] is = new int[1000];
		for (int i = 0; i < 1000; i++) {
			is[i] = i;
		}
		for (int i = 0; i < 1000; i++) {
			is[i] = 100000;
		}
		for (int i = 0; i < is.length; i++) {
			int a = is[i];
		}
	}

	private static void extracted() {
		ConcurrentSet cs = new ConcurrentSet();
		for (int i = 0; i < 1000; i++) {
			cs.put(i);
		}
		for (int i = 0; i < 1000; i++) {
			cs.set(i, 100000);
		}
		for (int i = 0; i < cs.size(); i++) {
			cs.get(i);
		}
	}

}
