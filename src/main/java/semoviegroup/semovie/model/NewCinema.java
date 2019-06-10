package semoviegroup.semovie.model;

public class NewCinema {
	private String name;
	private String location;
	private String price;

	public NewCinema(String name, String location, String price) {
		super();
		this.name = name;
		this.location = location;
		this.price = price;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "{\"name\":\"" + name + "\",\"location\":\"" + location + "\",\"price\":\"" + price + "\"}";
	}

}
