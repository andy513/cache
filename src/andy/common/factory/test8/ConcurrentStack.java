package andy.common.factory.test8;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Andy<andy_513@163.com>
 */
public class ConcurrentStack<E> {
	AtomicReference<Node<E>> head = new AtomicReference<Node<E>>();

	public void push(E item) {
		Node<E> newHead = new Node<E>(item);
		Node<E> oldHead;
		do {
			oldHead = head.get();
			newHead.next = oldHead;
		} while (!head.compareAndSet(oldHead, newHead));
	}

	public E pop() {
		Node<E> oldHead;
		Node<E> newHead;
		E e;
		do {
			oldHead = head.get();
			if (oldHead == null)
				return null;
			newHead = oldHead.next;
			e = oldHead.item;
		} while (!head.compareAndSet(oldHead, newHead));
		// oldHead = null;
		return e;
	}

	static class Node<E> {
		final E item;
		Node<E> next;

		public Node(E item) {
			this.item = item;
		}
	}

	private static final ExecutorService es = Executors.newCachedThreadPool();
	private static final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

	public static void main(String[] args) throws Exception {
		/*
		 * ConcurrentStack<String> cs = new ConcurrentStack<>(); int size =
		 * 99999999; // String a = "a"; new Thread(() -> { for (int i = 0; i <
		 * size; i++) { cs.push(
		 * "abcsafdsafdabcsafdsafdabcsafdsafdabcsafdsafdabcsfdsafdabcsafdsafdabcsafdsafdi"
		 * +i); } }).start(); // TimeUnit.SECONDS.sleep(3); new Thread(() -> {
		 * for (int i = 0; i < size; i++) { cs.pop(); } }).start();
		 */
		es.execute(() -> {
			while (true) {
				try {
					queue.add(ThreadLocalRandom.current().nextInt(1000) + "");
					TimeUnit.MILLISECONDS.sleep(1000 * ThreadLocalRandom.current().nextInt(30, 100));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		for (int i = 0; i < 2; i++) {
			es.execute(new Runnable() {
				public void run() {
					while (true) {
						try {
							String k = queue.take();
							System.out.println(Thread.currentThread().getName() + ":\t" + k);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
}
