package semoviegroup.semovie.model;

public class Worker {
	String name;// 演职员名字
	String identity;// 身份 如：actor director writer
	String img; // url
	String role; // 如果是演员就还有 这个字段 表明饰演什么角色

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Worker(String name, String identity, String img, String role) {
		super();
		this.name = name;
		this.identity = identity;
		this.img = img;
		this.role = role;
	}

	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"identity\":\"" + identity + "\",\"img\":\"" + img + "\",\"role\":\"" + role
				+ "\"}";
	}

}
