package andy.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author Andy<andy_513@163.com>
 */
public class RedisClient {

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static final int MAX_TOTAL = 1024;
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static final int MAX_IDLE = 200;
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static final int MAX_WAIT = 1000;
	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static final boolean TEST_ON_BORROW = true;

	private Jedis jedis;
	private JedisPool jedisPool;
	private ShardedJedis shardedJedis;
	private ShardedJedisPool shardedJedisPool;

	private static final class Instance {
		private static final RedisClient redisClient = new RedisClient();
	}

	public static final ShardedJedis getInstance() {
		return Instance.redisClient.shardedJedis;
	}

	public static final Jedis getSynInstance() {
		return Instance.redisClient.jedis;
	}

	private RedisClient() {
		initialPool();
		initialShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();
	}

	/**
	 * 初始化非切片池
	 */
	private void initialPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(MAX_TOTAL);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);
		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}

	/**
	 * 初始化切片池
	 */
	private void initialShardedPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(MAX_TOTAL);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT);
		config.setTestOnBorrow(TEST_ON_BORROW);
		// slave链接
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo("127.0.0.1", 6380));
		shards.add(new JedisShardInfo("127.0.0.1", 6379));
		// 构造池
		shardedJedisPool = new ShardedJedisPool(config, shards);
	}

	private static final ExecutorService es = Executors.newFixedThreadPool(5);

	public static void main(String[] args) throws Exception {
		CountDownLatch cld = new CountDownLatch(5);
		// final List<String> string = Collections.synchronizedList(new
		// ArrayList<String>());
		long time = System.currentTimeMillis();
		List<String> users = new ArrayList<String>();
		Map<String,String> map = new HashMap<String,String>();
		for (int i = 0; i < 10; i++) {
			users.add("123");
			map.put("789" + i, "546");
		}
//		RedisByteCache.addAllMap("a", map);
		RedisByteCache.set("a", map);
		// for (int i = 1; i <= 10000 * 100; i++) {
		// es.execute(() -> {
		String key = "a12";
		RedisByteCache.set(key, users);
//		RedisByteCache.setList(key, users);
		System.out.println(JSONObject.toJSONString(RedisByteCache.getList(key,String.class)));
		System.out.println(JSONObject.toJSONString(RedisByteCache.getAllMap("a",String.class,String.class)));
		// string.add();
		// cld.countDown();
		// });
		// if (i % 5 == 0)
		// cld.await();
		// }
		System.out.println(System.currentTimeMillis() - time);
		// System.out.println(JSONObject.toJSONString(string));
	}

	public void show() {
		// jedis.set(key, value);
		jedis.bgsave();
		KeyOperate();
		StringOperate();
		ListOperate();
		SetOperate();
		SortedSetOperate();
		HashOperate();
		jedis.resetState();
		shardedJedis.resetState();
	}

	private void KeyOperate() {
	}

	private void StringOperate() {
	}

	private void ListOperate() {
	}

	private void SetOperate() {
	}

	private void SortedSetOperate() {
	}

	private void HashOperate() {
	}

}
