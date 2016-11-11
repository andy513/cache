package andy.common.factory.test2;

/**
 * @author Andy<andy_513@163.com>
 */
public class ManagerB extends Manager {

	public ManagerB() {
		super(new Employe());
	}

	public ManagerB(Project project) {
		super(project);
	}

	/**
	 * 项目经理自己的事情：做早期工作
	 */
	public void doEarlyWork() {
		System.out.println("项目经理B 在做需求分析");
		System.out.println("项目经理B 在做详细设计");
	}

	/**
	 * 项目经理做收尾工作
	 */
	public void doEndWork() {
		System.out.println("项目经理B 在做收尾工作");
	}
}
