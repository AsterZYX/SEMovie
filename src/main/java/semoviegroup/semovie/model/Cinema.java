package semoviegroup.semovie.model;

public class Cinema {
	String name;
	String location;

	public Cinema(String name, String location) {
		super();
		this.name = name;
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"location\":\"" + location + "\"}";
	}

}
