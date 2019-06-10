package semoviegroup.semovie.model;

import java.text.Collator;
import java.util.Locale;

public class Cinema implements Comparable {
	private String name;
	private String location;

	public Cinema(String name, String location) {
		super();
		this.name = name;
		this.location = location;
	}

	public Cinema() {

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

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Collator instance = Collator.getInstance(Locale.CHINA);
		return instance.compare(this.toString(), o.toString());
	}

}
