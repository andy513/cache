package andy.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSONObject;

import andy.entity.User;
import andy.memcache.XMemcacheUtil;
import net.rubyeye.xmemcached.exception.MemcachedException;

/**
 * @author Andy<andy_513@163.com>
 */
public class MemcacheTest {

	public static void main(String[] args) throws Exception {
		// XMemcacheUtil.flushAll();
		List<User> string = new ArrayList<User>();
		for (int i = 0; i < 4; i++) {
			string.add(new User(i, i + ""));
		}
		String key = "a";
		XMemcacheUtil.addList(key, string);
		System.out.println(JSONObject.toJSONString(string));
		string = new ArrayList<User>();
		for (int i = 0; i < 2; i++) {
			string.add(new User(i, i + "a"));
		}
		XMemcacheUtil.setList(key, string);
		System.out.println(JSONObject.toJSONString(XMemcacheUtil.get(key)));
		string = new ArrayList<User>();
		for (int i = 0; i < 2; i++) {
			string.add(new User(i, i + "a"));
		}
		XMemcacheUtil.delList(key, string);
		System.out.println(JSONObject.toJSONString(XMemcacheUtil.get(key)));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					XMemcacheUtil.set(key, "a");
				} catch (TimeoutException | InterruptedException | MemcachedException e) {
					e.printStackTrace();
				}
			}
		});
		XMemcacheUtil.shutdown();
	}

}
