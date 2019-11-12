package fr.excilys.cdb.api.dto;

import javax.validation.constraints.NotBlank;

public final class Sort {
	@NotBlank
	private String Property;
	@NotBlank
	private Order order;
	
	public Sort(@NotBlank String property, @NotBlank Order order) {
		super();
		Property = property;
		this.order = order;
	}

	public String getProperty() {
		return Property;
	}

	public void setProperty(String property) {
		Property = property;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "Sort [Property=" + Property + ", order=" + order + "]";
	}

	public enum Order {
		ASCENDING,
		DESCENDING;
	}
	
}
