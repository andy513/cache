package andy.common.factory.test7;

/**
 * @author Andy<andy_513@163.com>
 */
public abstract class ConsumeHandler {
	private ConsumeHandler nextHandler;

	public ConsumeHandler getNextHandler() {
		return nextHandler;
	}

	public void setNextHandler(ConsumeHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	/** user申请人 free报销费用 */
	public abstract void doHandler(String user, double free);

}
