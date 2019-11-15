package fr.excilys.cdb.persistence.models;

public class SortDao {
	private String property;
	private String order;
	
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
		return "SortDao [property=" + property + ", order=" + order + "]";
	}
}
