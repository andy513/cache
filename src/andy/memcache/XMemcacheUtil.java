package andy.memcache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.code.yanf4j.core.impl.StandardSocketOption;

import andy.common.PropertiesUtil;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * @author Andy<andy_513@163.com>
 */
public class XMemcacheUtil {

	private static final Logger log = LogManager.getLogger(XMemcacheUtil.class);
	private static final Lock lock = new ReentrantLock();
	private static final String host = PropertiesUtil.getString("memcached.host");
	private static final int[] weight = PropertiesUtil.getIntArray("memcached.weight");
	private static final MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(host), weight);
	private static MemcachedClient client = null;

	static {
		try {
			// 使用二进制文件
			builder.setCommandFactory(new BinaryCommandFactory());
			// 使用一致性哈希算法（Consistent Hash Strategy）
			builder.setSessionLocator(new KetamaMemcachedSessionLocator());
			// 使用序列化传输编码
			builder.setTranscoder(new SerializingTranscoder());
			// 设置接收缓存区为32K，默认16K
			builder.setSocketOption(StandardSocketOption.SO_RCVBUF, 32 * 1024);
			// 设置发送缓冲区为16K，默认为8K
			builder.setSocketOption(StandardSocketOption.SO_SNDBUF, 16 * 1024);
			// 启用nagle算法，提高吞吐量，默认关闭
			builder.setSocketOption(StandardSocketOption.TCP_NODELAY, false);
			// 进行数据压缩，大于1KB时进行压缩
			builder.getTranscoder().setCompressionThreshold(32 * 1024);
			builder.setFailureMode(true);
			// 连接池通常不建议设置太大，我推荐在0-30之间为好，太大则浪费系统资源，太小无法达到分担负载的目的。
			builder.setConnectionPoolSize(20);
			// 默认如果连接超过5秒没有任何IO操作发生即认为空闲并发起心跳检测，你可以调长这个时间：
			builder.setConnectTimeout(10000);
			// 这是由于xmemcached的通讯层是基于非阻塞IO的，那么在请求发送给memcached之后，需要等待应答的到来，这个等待时间默认是1秒，如果
			// 超过1秒就抛出java.util.TimeoutExpcetion给用户。如果你频繁抛出此异常，可以尝试将全局的等待时间设置长一些
			builder.setOpTimeout(3000);
			client = builder.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final void shutdown() {
		try {
			client.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static final <T> boolean add(String key, T value) throws TimeoutException, InterruptedException, MemcachedException {
		return add(key, 0, value);
	}

	public static final <T> boolean add(String key, int exp, T value) throws TimeoutException, InterruptedException, MemcachedException {
		return client.add(key, exp, value);
	}

	public static final <T> boolean set(String key, T value) throws TimeoutException, InterruptedException, MemcachedException {
		return set(key, value, 0);
	}

	public static final <T> boolean set(String key, T value, int exp) throws TimeoutException, InterruptedException, MemcachedException {
		if (value == null)
			return false;
		if (client != null && !client.isShutdown()) {
			/*
			 * if (client.get(key) == null) { client.add(key, exp, value); }
			 * else {
			 */
			/*
			 * client.cas(key, new CASOperation<T>() {
			 * 
			 * @Override public int getMaxTries() { return 3; }
			 * 
			 * @Override public T getNewValue(long currentCAS, T currentValue) {
			 * log.error(currentCAS + "\t" +
			 * JSONObject.toJSONString(currentValue)); return value; } });
			 */
			return client.set(key, exp, value);
			// }
		}
		return false;
	}

	public static final boolean del(String key) throws TimeoutException, InterruptedException, MemcachedException {
		Lock l = lock;
		l.lock();
		try {
			if (client != null && !client.isShutdown()) {
				return client.delete(key);
			}
		} finally {
			l.unlock();
		}
		return false;
	}

	public static final <T> T get(String key) throws TimeoutException, InterruptedException, MemcachedException {
		Lock l = lock;
		l.lock();
		try {
			return (client != null && !client.isShutdown()) ? client.get(key) : null;
		} finally {
			l.unlock();
		}
	}

	private static final <T> boolean addT(T t, String key, boolean bool) throws TimeoutException, InterruptedException, MemcachedException {
		List<T> lists = null;
		if (t instanceof List) {
			lists = (List<T>) t;
		} else {
			lists = new ArrayList<T>();
			lists.add(t);
		}
		return add(lists, key, bool);
	}

	public static final void flushAll() {
		Lock l = lock;
		l.lock();
		try {
			try {
				if (client != null && !client.isShutdown()) {
					client.flushAll();
				}
			} catch (TimeoutException | InterruptedException | MemcachedException e) {
				e.printStackTrace();
			}
		} finally {
			l.unlock();
		}
	}

	private static final <T> boolean add(List<T> lists, String key, boolean bool) throws TimeoutException, InterruptedException, MemcachedException {
		Lock l = lock;
		l.lock();
		try {
			List<T> ts = get(key);
			if (ts == null) {
				if (client != null && !client.isShutdown()) {
					return client.set(key, 0, lists);
				} else {
					return false;
				}
			} else {
				if (bool) {
					ts.addAll(lists);
				} else {
					List<T> old_lists = new ArrayList<T>();
					old_lists.addAll(lists);
					int max_size = ts.size();
					for (int i = 0; i < max_size; i++) {
						T ti = ts.get(i);
						int j = 0;
						while (j < old_lists.size()) {
							T tj = old_lists.get(j);
							if (ti.equals(tj)) {
								ts.set(i, tj);
								old_lists.remove(j);
								break;
							}
							j++;
						}
					}
				}
				if (client != null && !client.isShutdown()) {
					return client.set(key, 0, ts);
				} else {
					return false;
				}
			}
		} finally {
			l.unlock();
		}
	}

	public static final <T> boolean addList(String key, T t) throws TimeoutException, InterruptedException, MemcachedException {
		return addT(t, key, true);
	}

	/*
	 * public static final User get(String key, User user) throws Exception { if
	 * (client != null && !client.isShutdown()) { return client.get(key + "_" +
	 * user.hashCode()); } else { return null; } }
	 */

	public static final <T> boolean setList(String key, T t) throws TimeoutException, InterruptedException, MemcachedException {
		return addT(t, key, false);
	}

	public static final <T> void delList(String key, List<T> lists) throws TimeoutException, InterruptedException, MemcachedException {
		List<T> ts = get(key);
		if (ts != null) {
			ts.removeAll(lists);
			set(key, ts);
		}
	}

	public static final <T> void delList(String key, T t) throws TimeoutException, InterruptedException, MemcachedException {
		List<T> ts = get(key);
		if (ts != null) {
			ts.remove(t);
			set(key, ts);
		}
	}

}
