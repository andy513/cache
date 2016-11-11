package andy.common.factory.test3;

/**
 * @author Andy<andy_513@163.com>
 */
public class Decorator_first extends Decorator {
	public Decorator_first(Human human) {
		super(human);
	}

	public void goClothespress() {
		System.out.println("去衣柜找找看。。");
	}

	public void findPlaceOnMap() {
		System.out.println("在Map上找找。。");
	}

	@Override
	public void wearClothes() {
		super.wearClothes();
		goClothespress();
	}

	@Override
	public void walkToWhere() {
		super.walkToWhere();
		findPlaceOnMap();
	}
}
