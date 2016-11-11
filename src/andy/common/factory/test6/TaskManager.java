package andy.common.factory.test6;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andy<andy_513@163.com>
 */
public class TaskManager implements ITaskValidate{
	
	static final Map<Integer,Task> maps = new ConcurrentHashMap<>();
	
	static{
		maps.put(1, new Task());
		maps.put(2, new Task());
		maps.put(3, new Task());
	}

	@Override
	public void validate(int taskId, int conditionId, int num) {
		Task task = maps.get(taskId);
//		task.
	}

}