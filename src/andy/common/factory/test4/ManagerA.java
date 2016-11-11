package andy.common.factory.test4;

/**
 * @author Andy<andy_513@163.com>
 */
public class ManagerA extends Manager {

	public ManagerA(Project project) {
		super(project);
	}

	protected void end() {
		System.out.println("ManagerA end!");
	}
	
	/*@Override
	public void doCoding() {
		System.out.println("ManagerA doCoding...");
	}
*/
	protected void start() {
		System.out.println("ManagerA start");
	}

}
