package fr.excilys.cdb.api.dto;

import javax.validation.constraints.NotBlank;

public final class SortDto {
	@NotBlank
	private String property;
	@NotBlank
	private String order;
	
	public SortDto(@NotBlank String property, @NotBlank String order) {
		super();
		this.property = property;
		this.order = order;
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
		return "Sort [property=" + property + ", order=" + order + "]";
	}
}
