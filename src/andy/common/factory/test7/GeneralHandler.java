package andy.common.factory.test7;

/**
 * @author Andy<andy_513@163.com>
 */
public class GeneralHandler extends ConsumeHandler {

	@Override
	public void doHandler(String user, double free) {
		if (free >= 1000) {
			if (user.equals("lwxzy")) {
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

	public static void main(String[] args) {
		ProjectHandler projectHandler = new ProjectHandler();
		DeptHandler deptHandler = new DeptHandler();
		/*GeneralHandler generalHandler = new GeneralHandler();
		projectHandler.setNextHandler(deptHandler);
		deptHandler.setNextHandler(generalHandler);*/
		deptHandler.doHandler("zy", 450);
		deptHandler.doHandler("zy", 600);
		deptHandler.doHandler("zy", 600);
		deptHandler.doHandler("zy", 1500);
		deptHandler.doHandler("lwxzy", 1500);
	}

}
