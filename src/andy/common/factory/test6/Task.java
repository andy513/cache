package andy.common.factory.test6;

import java.util.Map;

/**
 * @author Andy<andy_513@163.com>
 */
public class Task {
	
	private int taskId;
	private Map<Integer,Integer> conditions;
	
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public Map<Integer, Integer> getConditions() {
		return conditions;
	}
	public void setConditions(Map<Integer, Integer> conditions) {
		this.conditions = conditions;
	}

}
