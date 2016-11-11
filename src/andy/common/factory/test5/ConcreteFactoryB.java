package andy.common.factory.test5;

/**
 * @author Andy<andy_513@163.com>
 */
public class ConcreteFactoryB extends Factory2 {

	@Override
	IProductA getProductA2() {
		return new ProductA2();
	}

	@Override
	IProductB getProductB2() {
		return new ProductB2();
	}

}
