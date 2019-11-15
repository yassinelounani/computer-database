package fr.excilys.cdb.api.dto;

public class Navigation {
	private int number;
	private int size;
	private String name;
	private String property;
	private String order;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "Navigation [number=" + number + ", size=" + size + ", name=" + name + ", property=" + property
				+ ", order=" + order + "]";
	}

	
	
}
