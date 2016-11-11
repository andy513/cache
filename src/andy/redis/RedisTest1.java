package andy.redis;

/**
 * @author Andy<andy_513@163.com>
 */
public class RedisTest1 {

	public static void main(String[] args) {
//		RedisByteCache.set("e", 1);
		Integer a = RedisByteCache.get("e", Integer.class);
		System.out.println(a);
	}

}
