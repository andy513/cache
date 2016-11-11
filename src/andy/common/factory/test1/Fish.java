package andy.common.factory.test1;

/**
 * @author Andy<andy_513@163.com>
 */
public class Fish extends Change {

	public Fish(TheGreatestSage sage) {
		super(sage);
	}

	@Override
	public void move() {
		// 代码
		System.out.println("Fish Move");
	}

}
