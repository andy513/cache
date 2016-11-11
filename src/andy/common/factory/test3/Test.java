package andy.common.factory.test3;

/**
 * @author Andy<andy_513@163.com>
 */
public class Test {
	public static void main(String[] args) {
		Human person = new Person();
		Decorator decorator = new Decorator_two(new Decorator_first(new Decorator_zero(person)));
		decorator.wearClothes();
		decorator.walkToWhere();
	}
}
