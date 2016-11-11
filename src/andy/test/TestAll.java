package andy.test;

import com.alibaba.fastjson.JSONObject;

/**
 * @author Andy<andy_513@163.com>
 */
public class TestAll {

	private static final int[] is = new int[] { 1, 2 };

	public static void main(String[] args) {
		int len = 8;
		int num = 0;// 第几次遍历
		int start = 0;
		for (int m = 0; m < 100; m++) {
			int[] i_s = new int[len];
			for (int i = 0; i < len; i++) {
				if (num == len - i - 1) {
					System.out.println(num + "\t" + i);
					i_s[i] = is[start + 1 >= is.length ? start - 1 : start + 1];
				} else {
					i_s[i] = is[start];
				}
			}
			System.out.println(JSONObject.toJSONString(i_s));
			num++;
			if (num == len) {
				num = 0;
				start++;
				continue;
			}
		}
	}

}
