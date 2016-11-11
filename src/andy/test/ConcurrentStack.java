package andy.test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Andy<andy_513@163.com>
 */
public class ConcurrentStack<E> {
	
	public static void main(String[] args) {
		ConcurrentStack<Integer> cs = new ConcurrentStack<Integer>();
		for(int i = 0;i<10;i++){
			cs.push(i);
		}
		for(int i = 0;i<10;i++){
			System.out.println(cs.pop());
		}
	}
	AtomicReference<Node<E>> head = new AtomicReference<Node<E>>();
	public void push(E item) {
		Node<E> newHead = new Node<E>(item);
		Node<E> oldHead;
		do {
			oldHead = head.get();
			newHead.next = oldHead;
		} while (!head.compareAndSet(oldHead, newHead));
	}

	public AtomicReference<Node<E>> getHead() {
		return head;
	}

	public void setHead(AtomicReference<Node<E>> head) {
		this.head = head;
	}

	public E pop() {
		Node<E> oldHead;
		Node<E> newHead;
		do {
			oldHead = head.get();
			if (oldHead == null)
				return null;
			newHead = oldHead.next;
		} while (!head.compareAndSet(oldHead, newHead));
		return oldHead.item;
	}

	static class Node<E> {
		final E item;
		Node<E> next;

		public Node(E item) {
			this.item = item;
		}
	}
}
