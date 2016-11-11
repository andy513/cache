package andy.test;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

/**
 * @author Andy<andy_513@163.com>
 */
public class SortTask extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	final long[] array;
	final int lo;
	final int hi;
	private int THRESHOLD = 30;

	public SortTask(long[] array) {
		this.array = array;
		this.lo = 0;
		this.hi = array.length - 1;
	}

	public SortTask(long[] array, int lo, int hi) {
		this.array = array;
		this.lo = lo;
		this.hi = hi;
	}

	protected void compute() {
		if (hi - lo < THRESHOLD)
			sequentiallySort(array, lo, hi);
		else {
			int pivot = partition(array, lo, hi);
			new SortTask(array, lo, pivot - 1);
			new SortTask(array, pivot + 1, hi);
		}
	}

	private int partition(long[] array, int lo, int hi) {
		long x = array[hi];
		int i = lo - 1;
		for (int j = lo; j < hi; j++) {
			if (array[j] <= x) {
				i++;
				swap(array, i, j);
			}
		}
		swap(array, i + 1, hi);
		return i + 1;
	}

	private void swap(long[] array, int i, int j) {
		if (i != j) {
			long temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}

	private void sequentiallySort(long[] array, int lo, int hi) {
		Arrays.sort(array, lo, hi + 1);
	}

}
