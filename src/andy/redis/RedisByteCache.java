package andy.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Message;

import andy.entity.User;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * @author andy<andy_513@163.com>
 */
public final class RedisByteCache {

	public static void main(String[] args) {
		List<String> lists = extracted();
		List<User> users = new ArrayList<User>();
		users.add(new User(1, "a"));
		users.add(new User(2, "b"));
		users.add(new User(3, "c"));
		RedisByteCache.set("a", users);
		RedisByteCache.set("a", users);
		System.out.println(JSONObject.toJSONString(RedisByteCache.getList("a", User.class)));
	}

	private static List<String> extracted() {
		List<String> lists = new ArrayList<String>();
		lists.add(1 + "");
		lists.add(2 + "");
		lists.add(3 + "");
		return lists;
	}

	public static final Jedis shardJedis = RedisClient.getSynInstance();
	private static final Lock lock = new ReentrantLock();

	public static final String set(String key, Message message) {
		Lock l = lock;
		l.lock();
		try {
			return shardJedis.set(getKey(key), message.toByteArray());
		} finally {
			l.unlock();
		}
	}

	public static final long del(long key) {
		return del(key + "");
	}

	public static final long del(String key) {
		Lock l = lock;
		l.lock();
		try {
			return shardJedis.del(getKey(key));
		} finally {
			l.unlock();
		}
	}

	public static final byte[] get(String key) {
		Lock l = lock;
		l.lock();
		try {
			return shardJedis.get(getKey(key));
		} finally {
			l.unlock();
		}
	}

	public static final void set(long key, Object value) {
		set(key + "", value);
	}

	public static final void set(String key, Object value) {
		Lock l = lock;
		l.lock();
		try {
			if (value instanceof List) {
				List<Object> lists = (List<Object>) value;
				setList(key, lists);
			} else if (value instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) value;
				addAllMap(key, map);
			} else if (value instanceof String || value instanceof Integer) {
				shardJedis.set(key, value.toString());
			} else {
				shardJedis.set(getKey(key), decoder(value));
			}
		} finally {
			l.unlock();
		}
	}

	public static final <T> T get(long key, Class<T> t) {
		return get(key + "", t);
	}

	public static final <T> T get(String key, Class<T> t) {
		Lock l = lock;
		l.lock();
		try {
			if (t.equals(String.class)) {
				return (T) shardJedis.get(key);
			} else if (t.equals(Integer.class)) {
				String value = shardJedis.get(key);
				if(value == null) return null;
				Integer a = Integer.parseInt(value);
				return (T) a;
			}
			byte[] b = shardJedis.get(getKey(key));
			return encoder(b, t);
		} finally {
			l.unlock();
		}
	}

	public static final <T> long addMap(String key, String field, T value) {
		return shardJedis.hset(getKey(key), getKey(field), decoder(value));
	}

	private static final <T> long addMap(byte[] key, String field, T value) {
		return shardJedis.hset(key, getKey(field), decoder(value));
	}

	public static final void delMap(String key, String... fields) {
		for (String field : fields) {
			shardJedis.hdel(getKey(key), getKey(field));
		}
	}

	private static final <T> void addAllMap(String key, Map<String, T> map) {
		byte[] keys = getKey(key);
		map.forEach((field, value) -> {
			addMap(keys, field, value);
		});
	}

	public static final <K, V> Map<K, V> getAllMap(String key, Class<K> cls_k, Class<V> cls_v) {
		byte[] keys = getKey(key);
		Map<byte[], byte[]> map = shardJedis.hgetAll(keys);
		Map<K, V> result = new ConcurrentHashMap<K, V>();
		for (Entry<byte[], byte[]> entry : map.entrySet()) {
			byte[] field = entry.getKey();
			byte[] value = entry.getValue();
			result.put(encoder(field, cls_k), encoder(value, cls_v));
		}
		return result;
	}

	public static final <V> V getMapValue(String key, String field, Class<V> v) {
		byte[] values = shardJedis.hget(getKey(key), decoder(field));
		return encoder(values, v);
	}

	private static final <T> void setList(String key, List<T> value) {
		int size = value.size();
		byte[] keys = getKey(key);
		for (int i = 0; i < size; i++) {
			shardJedis.lpush(keys, decoder(value.get(i)));
		}
	}

	public static final <T> List<T> getList(String key, Class<T> t) {
		List<T> result = new ArrayList<T>();
		List<byte[]> lists = shardJedis.lrange(getKey(key), 0, -1);
		for (int i = 0; i < lists.size(); i++) {
			result.add(encoder(lists.get(i), t));
		}
		return result;
	}

	private static final byte[] getKey(String key) {
		Schema<String> STR_KEY = RuntimeSchema.getSchema(String.class);
		LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.MIN_BUFFER_SIZE);
		return ProtostuffIOUtil.toByteArray(key, STR_KEY, BUFFER);
	}

	private static final <T> byte[] decoder(T t) {
		Schema<T> schema = RuntimeSchema.getSchema((Class<T>) t.getClass());
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		byte[] b = ProtostuffIOUtil.toByteArray(t, schema, buffer);
		return b;
	}

	private static final <T> T encoder(byte[] bytesList, Class<T> t) {
		if (bytesList == null)
			return null;
		Schema<T> schema = RuntimeSchema.getSchema(t);
		T result = null;
		try {
			result = t.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		ProtostuffIOUtil.mergeFrom(bytesList, result, schema);
		return result;
	}

}