package andy.common.factory.test4;

/**
 * @author Andy<andy_513@163.com>
 */
public class Client {
	
	public static void main(String[] args) {
		new ManagerA(new ManagerB(new Employe())).doCoding();
	}

}
