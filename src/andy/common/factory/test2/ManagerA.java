package andy.common.factory.test2;

/**
 * @author Andy<andy_513@163.com>
 */
public class ManagerA extends Manager {

	public ManagerA() {
		super(new Employe());
	}

	public ManagerA(Project project) {
		super(project);
	}

	/**
	 * 项目经理自己的事情：做早期工作
	 */
	public void doEarlyWork() {
		System.out.println("项目经理A 在做需求分析");
		System.out.println("项目经理A 在做架构设计");
		System.out.println("项目经理A 在做详细设计");
	}

	@Override
	public void doEndWork() {
		System.out.println("项目经理A 下班了");
	}
}
