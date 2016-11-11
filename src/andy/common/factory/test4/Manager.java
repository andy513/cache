package andy.common.factory.test4;

/**
 * @author Andy<andy_513@163.com>
 */
public abstract class Manager implements Project {

	private Project project;

	public Manager(Project project) {
		this.project = project;
	}

	@Override
	public void doCoding() {
		start();
		System.out.println(project);
		project.doCoding();
		end();
	}

	protected void end() {
		System.out.println("Manager end!");
	}

	protected void start() {
		System.out.println("Manager start");
	}

}
