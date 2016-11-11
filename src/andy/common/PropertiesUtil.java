package andy.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Andy<andy_513@163.com>
 */
public final class PropertiesUtil {

	private static final Properties p = new Properties();

	static {
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream("src/cache.properties"));
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final String getString(String key) {
		return p.getProperty(key, "").trim();
	}

	public static final int getInteger(String key) {
		return Integer.parseInt(getString(key));
	}
	
	public static final String[] getStringArray(String key) {
		return getStringArray(key, ",");
	}
	
	public static final String[] getStringArray(String key, String split) {
		String[] strs = null;
		if (split == null) split = ","; 
		strs = getString(key).split(split);
		return strs;
	}

	public static final int[] getIntArray(String key) {
		return getIntArray(key,",");
	}
	
	public static final int[] getIntArray(String key, String split) {
		String[] strs = getStringArray(key, split);
		int[] is = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			is[i] = Integer.parseInt(strs[i]);
		}
		return is;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getString("memcached.host"));
		System.out.println(getInteger("memcached.port"));
	}

}
