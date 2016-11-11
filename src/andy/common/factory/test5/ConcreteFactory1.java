package andy.common.factory.test5;

/**
 * @author Andy<andy_513@163.com>
 */
public class ConcreteFactory1 extends Factory1 {

	@Override
	IProductA getProductA1() {
		return new ProductA1();
	}

	@Override
	IProductB getProductB1() {
		return new ProductB1();
	}

}
