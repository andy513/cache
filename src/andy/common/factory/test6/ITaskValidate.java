package andy.common.factory.test6;

/**
 * @author Andy<andy_513@163.com>
 */
public interface ITaskValidate {

	/**
	 * @param taskId				任务ID
	 * @param conditionId			条件ID
	 * @param num					达成数量
	 */
	void validate(int taskId, int conditionId, int num);
	
}
