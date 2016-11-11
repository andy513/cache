package andy.entity;

import java.io.Serializable;

/**
 * @author Andy<andy_513@163.com>
 */
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int type;
	private String name;
	private String password;
	
	public User() {
	}
	
	public User(int id, String name) {
		setId(id);
		setName(name);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void main(String[] args) {
		User u1 = new User();
		u1.setId(1);
		User u2 = new User();
		u2.setId(2);
		System.out.println(u1.hashCode() + "\t" + u2.hashCode());
		System.out.println(System.identityHashCode(u1) + "\t" + System.identityHashCode(u2));
		System.out.println((System.identityHashCode(u1) == 1704856573) + "\t" + (System.identityHashCode(u2) == 705927765));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
