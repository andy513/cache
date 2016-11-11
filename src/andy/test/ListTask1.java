package andy.test;

import java.util.concurrent.RecursiveAction;

/**
 * @author Andy<andy_513@163.com>
 */
public class ListTask1<T> extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	private static final int THRESHOLD = 100;
	private int start;
	private int end;
	private T t;

	public ListTask1(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		if ((start - end) < THRESHOLD) {
			for (int i = start; i < end; i++) {
//				sum += i;
			}
		} else {
			int middle = (start + end) / 2;
			ListTask1 left = new ListTask1(start, middle);
			ListTask1 right = new ListTask1(middle + 1, end);
			left.fork();
			right.fork();
//			sum = left.join() + right.join();
		}
	}

}
