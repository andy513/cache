package andy.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.ShardedJedis;

/**
 * @author andy<andy_513@163.com>
 */
public class RedisClientTemplate<T> {

	private static final Logger log = LogManager.getLogger(RedisClientTemplate.class);
	private static final ShardedJedis shardedJedis = JedisDataSourceImpl.getRedisClient();

	/**
	 * 设置单个值
	 * 
	 * @param key
	 * @param object
	 * @return
	 */
	public static final <T> long set(String key, T object) {
		if (shardedJedis == null || key == null || object == null) {
			return 0;
		}
		if (object instanceof List) {
			List<?> lists = (List<?>) object;
			long result = 0;
			for (int i = 0; i < lists.size(); i++)
				result += shardedJedis.lpush(key, JSONObject.toJSONString(lists.get(i)));
			return result;
		} else if (object instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) object;
			long result = 0;
			for (Entry<?, ?> entry : map.entrySet())
				result += shardedJedis.hset(key, String.valueOf(entry.getKey()), JSONObject.toJSONString(entry.getValue()));
			return result;
		}
		String result = shardedJedis.set(key, JSON.toJSONString(object));
		return "OK".equals(result) ? 1 : 0;
	}

	/**
	 * 获取单个值
	 * 
	 * @param key
	 * @return
	 */
	public static final <T> T get(String key, Class<T> cls) {
		if (shardedJedis == null || key == null) {
			return null;
		}
		String result = shardedJedis.get(key);
		if (result == null)
			return null;
		return JSON.parseObject(result, cls);
	}
	
	public static final <T> T getMapValue(String key,Object mkey,Class<T> cls){
		String string = shardedJedis.hget(key, String.valueOf(mkey));
		return JSONObject.parseObject(string, cls);
	}
	
	public static final <T> Set<T> getMapKeys(String key,Class<T> cls){
		Set<String> sets = shardedJedis.hkeys(key);
		Set<T> ts = new HashSet<T>();
		for(Iterator<String> iterator = sets.iterator();iterator.hasNext();){
			String string = iterator.next();
			ts.add(JSONObject.parseObject(string, cls));
		}
		return ts;
	}

	public static final <T> List<T> getMapValues(String key, Class<T> cls) {
		List<String> lists = shardedJedis.hvals(key);
		List<T> ts = new ArrayList<>();
		for (int i = 0; i < lists.size(); i++) {
			String string = lists.get(i);
			T t = JSONObject.parseObject(string, cls);
			ts.add(t);
		}
		return ts;
	}

	public static final Boolean exists(String key) {
		Boolean result = false;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.exists(key);
		return result;
	}

	public static final String type(String key) {
		String result = null;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.type(key);
		return result;
	}

	/**
	 * 在某段时间后失效
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static final Long expire(String key, int seconds) {
		Long result = null;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.expire(key, seconds);
		return result;
	}

	/**
	 * 在某个时间点失效
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public static final Long expireAt(String key, long unixTime) {
		Long result = null;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.expireAt(key, unixTime);
		return result;
	}

	public static final Long ttl(String key) {
		Long result = null;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.ttl(key);
		return result;
	}

	public static final boolean setbit(String key, long offset, boolean value) {
		boolean result = false;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.setbit(key, offset, value);
		return result;
	}

	public static final boolean getbit(String key, long offset) {
		boolean result = false;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.getbit(key, offset);
		return result;
	}

	public static final long setRange(String key, long offset, String value) {
		long result = 0;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.setrange(key, offset, value);
		return result;
	}

	public static final String getRange(String key, long startOffset, long endOffset) {
		String result = null;
		if (shardedJedis == null) {
			return result;
		}
		result = shardedJedis.getrange(key, startOffset, endOffset);
		return result;
	}
}
