package andy.common.factory.test1;

/**
 * @author Andy<andy_513@163.com>
 */
public class Change implements TheGreatestSage {
	private TheGreatestSage sage;

	public Change(TheGreatestSage sage) {
		this.sage = sage;
	}

	@Override
	public void move() {
		// 代码
		sage.move();
	}

}
