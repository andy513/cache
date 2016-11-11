package andy.test;

import java.util.List;
import java.util.concurrent.RecursiveTask;

import com.alibaba.fastjson.JSONObject;

import andy.entity.User;

/**
 * @author Andy<andy_513@163.com>
 */
public class ListTask extends RecursiveTask<User> {

	private static final long serialVersionUID = 1L;

	private static final int THRESHOLD = 100;
	private int start;
	private int end;
	private User u;
	private List<User> users;

	public ListTask(List<User> users,int start, int end,User u) {
		this.start = start;
		this.end = end;
		this.users = users;
		this.u = u;
	}

	@Override
	protected User compute() {
		User user = null;
		if ((start - end) < THRESHOLD) {
			for (int i = start; i < end; i++) {
				user = users.get(i);
				System.out.println(JSONObject.toJSONString(user));
				if(user.equals(u)){
					return user;
				}
			}
		} else {
			int middle = (start + end) / 2;
			ListTask left = new ListTask(users,start, middle,u);
			ListTask right = new ListTask(users, middle + 1, end, u);
			left.fork();
			right.fork();
			user = left.join();
			if (user != null) {
				return user;
			}
			user = right.join();
			if (user != null) {
				return user;
			}
		}
		return user;
	}

}
