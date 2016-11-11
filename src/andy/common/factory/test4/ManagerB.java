package andy.common.factory.test4;

/**
 * @author Andy<andy_513@163.com>
 */
public class ManagerB extends Manager {

	public ManagerB(Project project) {
		super(project);
	}

	@Override
	protected void end() {
		System.out.println("ManagerB end!");
	}
	
	@Override
	protected void start() {
		System.out.println("ManagerB start!");
	}

}
