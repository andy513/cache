package andy.common.factory.test7;

/**
 * @author Andy<andy_513@163.com>
 */
public class DeptHandler extends ConsumeHandler {

	public DeptHandler() {
		super.setNextHandler(new GeneralHandler());
	}

	@Override
	public void doHandler(String user, double free) {
		if (free < 1000) {
			if (user.equals("zy")) {
				System.out.println("给予报销:" + free);
			} else {
				System.out.println("报销不通过");
			}
		} else {
			ConsumeHandler consumeHandler = getNextHandler();
			if (consumeHandler != null) {
				consumeHandler.doHandler(user, free);
			}
		}
	}

}
