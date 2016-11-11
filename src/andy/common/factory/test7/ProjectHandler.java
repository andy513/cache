package andy.common.factory.test7;

/**
 * @author Andy<andy_513@163.com>
 */
public class ProjectHandler extends ConsumeHandler {
	
	public ProjectHandler() {
		super.setNextHandler(new DeptHandler());
	}

	@Override
	public void doHandler(String user, double free) {
		if (free < 500) {
			if (user.equals("lwx")) {
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
