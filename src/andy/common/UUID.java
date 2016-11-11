package andy.common;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Andy<andy_513@163.com>
 */
public final class UUID {

	private static final char[] cs = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
			'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	private static final char[] crs = new char[93];

	static {
		int i = 33;// '!'-'~',33-126
		for (int k = 0; k < crs.length; k++) {
			crs[k] = (char) i;
			i++;
		}
	}

	public static final String getUUID() {
		return getChar(cs, 8);
	}

	public static final String getUUID(int len) {
		return getChar(crs, len);
	}

	private static final String getChar(final char[] crs, final int len) {
		char[] chars = new char[len];
		int num = crs.length;
		for (int i = 0; i < len; i++) {
			char c = crs[ThreadLocalRandom.current().nextInt(num)];
			chars[i] = c;
		}
		return new String(chars);
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		int size = 50000000;
		for (int i = 0; i < size; i++) {
			UUID.getUUID(8);
//			System.out.println(UUID.getUUID());
		}
		time = System.currentTimeMillis() - time;
		System.out.println(time + "\t" + (size / (time / 1000)));
		time = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			UUID.getUUID();
//			System.out.println(UUID.getUUID(10));
		}
		time = System.currentTimeMillis() - time;
		System.out.println(time + "\t" + (size / (time / 1000)));
		// System.out.println((size / ((System.currentTimeMillis() - time) /
		// 1000)));
	}

}
