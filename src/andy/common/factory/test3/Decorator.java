package andy.common.factory.test3;

/**
 * @author Andy<andy_513@163.com>
 */
public class Decorator implements Human {

	private Human human;

	public Decorator(Human human) {
		this.human = human;
	}

	public void wearClothes() {
		human.wearClothes();
	}

	public void walkToWhere() {
		human.walkToWhere();
	}

}
