package andy.common.factory.test1;

/**
 * @author Andy<andy_513@163.com>
 */
public class Bird extends Change {
	
	public Bird(TheGreatestSage sage) {
		super(sage);
	}

	@Override
	public void move() {
		// 代码
		System.out.println("Bird Move");
	}
}
