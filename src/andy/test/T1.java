package andy.test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class T1 {
	public static void main(String[] args) throws IOException {
		String servers = "127.0.0.1:11211";
		XMemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(servers));
		builder.setCommandFactory(new BinaryCommandFactory());
		final MemcachedClient client = builder.build();
		final String key = "ghost";
		try {
			client.add(key, 0, "Ghost wind blows!");
			System.out.println("add & get: " + client.get(key));
			client.append(key, " It's a lie.");
			System.out.println("append & get: " + client.get(key));
			client.prepend(key, "Who's said?! ");
			System.out.println("prepend & get: " + client.get(key));
			client.replace(key, 0, "Everything is nothing!");
			System.out.println("replace & get: " + client.get(key));
			client.delete(key);
			System.out.println("delete & get: " + client.get(key));
			List<String> keys = Arrays.asList(new String[] { "key1", "key2", "key3" });
			for (String k : keys) {
				client.set(k, 0, "v:" + System.nanoTime());
			}
			Map<String, GetsResponse<Object>> values = client.gets(keys);
			for (Map.Entry<String, GetsResponse<Object>> entry : values.entrySet()) {
				System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue().getValue());
			}
			final AtomicLong seq = new AtomicLong(0);
			ExecutorService pool = Executors.newCachedThreadPool();
			for (int i = 0; i < 50; i++) {
				pool.execute(new Runnable() {
					@Override
					public void run() {
						while (true) {
							CacheResult o = new CacheResult();
							o.file = new File("/opt/status/servers.lst");
							o.lastmodified = seq.incrementAndGet();
							System.out.println("#" + Thread.currentThread().getId() + "=>o: " + o);
							try {
								client.set(key, 0, o);
								TimeUnit.MILLISECONDS.sleep(20);
							} catch (TimeoutException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (MemcachedException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
			TimeUnit.MILLISECONDS.sleep(1000);
			for (int i = 0; i < 1000; i++) {
				client.cas(key, new CASOperation<CacheResult>() {
					@Override
					public int getMaxTries() {
						return 10;
					}
					@Override
					public CacheResult getNewValue(long arg0, CacheResult result) {
						CacheResult old = result;
						CacheResult nu = new CacheResult();
						nu.file = old.file;
						nu.lastmodified = seq.incrementAndGet();
						System.out.println("cas: old=" + old + ", new=" + nu);
						return result;
					}
				});
			}
			pool.shutdown();
			client.flushAll();
			List<InetSocketAddress> addresses = AddrUtil.getAddresses(servers);
			for (InetSocketAddress addr : addresses) {
				Map<String, String> stats = client.stats(addr);
				System.out.println(stats);
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MemcachedException e) {
			e.printStackTrace();
		}
		synchronized (client) {
			try {
				client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static class CacheResult implements Serializable {
		private static final long serialVersionUID = 7L;
		private File file;
		private long lastmodified;

		@Override
		public String toString() {
			return "file=[" + file + ", lastmodified=" + lastmodified + "]";
		}
	}
}