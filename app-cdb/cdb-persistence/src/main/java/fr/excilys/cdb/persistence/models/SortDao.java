package fr.excilys.cdb.persistence.models;

public class SortDao {
	private String property;
	private Order order;
	
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "SortDao [property=" + property + ", order=" + order + "]";
	}

	public enum Order {
		ASCENDING("ASC"),
		DESCENDING("DESC");

		private String sort;

		private Order(String sort) {
			this.sort = sort;
		}

		public String getSort() {
			return sort;
		}
	}
}
