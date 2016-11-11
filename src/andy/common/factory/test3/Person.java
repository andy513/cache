package andy.common.factory.test3;

/**
 * @author Andy<andy_513@163.com>
 */
public class Person implements Human {

	@Override
	public void wearClothes() {
		System.out.println("穿什么呢。。");
	}

	@Override
	public void walkToWhere() {
		System.out.println("去哪里呢。。");
	}

}
