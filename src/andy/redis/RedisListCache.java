package andy.redis;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.ShardedJedis;

/**
 * @author andy<andy_513@163.com>
 */
public final class RedisListCache {

	public static final ShardedJedis shardJedis = JedisDataSourceImpl.getRedisClient();

	/**
	 * @param key
	 * @param objects
	 * @return
	 */
	public static final long add(String key, Object... objects) {
		String string = null;
		long result = 0;
		for (int i = 0; i < objects.length; i++) {
			Object value = objects[i];
			if (value instanceof List) {
				List<?> lists = (List<?>) value;
				for (int k = 0; k < lists.size(); k++) {
					Object v = lists.get(k);
					if (v == null)
						continue;
					string = JSONObject.toJSONString(v);
					result = shardJedis.lpush(key, string);
				}
			} else {
				string = JSONObject.toJSONString(value);
				result = shardJedis.lpush(key, string);
			}
		}
		return result;
	}

	/**
	 * 删除指定集合
	 * 
	 * @param key
	 * @param objects
	 * @return
	 */
	public static final long delAll(String key, Object... objects) {
		return del(key, 1, objects);
	}

	/**
	 * 删除重复数据
	 * 
	 * @param key
	 * @param objects
	 * @return
	 */
	public static final long del(String key, Object... objects) {
		return del(key, 2, objects);
	}

	public static final long del(String key, int num, Object... objects) {
		long result = 0;
		for (Object object : objects) {
			String value = JSONObject.toJSONString(object);
			result = shardJedis.lrem(key, num, value);
		}
		return result;
	}

	/**
	 * 修改指定下标缓存集合
	 * 
	 * @param key
	 * @param index
	 *            集合下标
	 * @param object
	 *            修改的新值
	 * @return
	 */
	public static final String set(String key, long index, Object object) {
		String value = JSONObject.toJSONString(object);
		return shardJedis.lset(key, index, value);
	}

	/**
	 * 该方法需要实现hashcode
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public static final <T> String set(String key, T object) {
		long max_size = length(key);
		long start = 0;
		long end = 0;
		int num = 99;
		String result = null;
		List<String> lists = null;
		do {
			end += num;
			if (end > max_size)
				end = max_size;
			lists = shardJedis.lrange(key, start, end);
			for (int i = 0; i < lists.size(); i++) {
				T old_object = JSONObject.parseObject(lists.get(i), object.getClass());
				if (old_object.equals(object)) {
					result = shardJedis.lset(key, start + i, JSONObject.toJSONString(object));
					end = max_size;
					break;
				}
			}
			if (end == max_size)
				break;
			start = end;
		} while (result == null);
		return result;
	}

	/**
	 * 获取指定下标集合列表
	 * 
	 * @param key
	 * @param cls
	 * @param is
	 *            需要获得的下标
	 * @return 筛选过后的集合
	 */
	public static final <T> List<T> get(String key, Class<T> cls, long... is) {
		String string = null;
		List<T> list = new ArrayList<T>();
		for (long index : is) {
			string = shardJedis.lindex(key, index);
			list.add(JSONObject.parseObject(string, cls));
		}
		return list;
	}

	/**
	 * 获取指定下标范围的集合列表
	 * 
	 * @param key
	 * @param start
	 *            开始下标(从0开始)
	 * @param end
	 *            结束下标(正数表示正常下标,负数表示倒数第几位下标)
	 * @param cls
	 * @return
	 */
	public static final <T> List<T> get(String key, long start, long end, Class<T> cls) {
		List<String> lists = shardJedis.lrange(key, start, end);
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < lists.size(); i++) {
			String string = lists.get(i);
			list.add(JSONObject.parseObject(string, cls));
		}
		return list;
	}

	/**
	 * 获取指定类型的集合列表
	 * 
	 * @param key
	 * @param cls
	 * @return
	 */
	public static final <T> List<T> get(String key, Class<T> cls) {
		return get(key, 0, -1, cls);
	}

	/**
	 * 获取集合长度
	 * 
	 * @param key
	 *            缓存ID
	 * @return 缓存集合列表总长度
	 */
	public static final long length(String key) {
		return shardJedis.llen(key);
	}

}
