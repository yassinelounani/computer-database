package fr.excilys.cdb.api.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;

public final class Navigation implements Serializable {
	
	private static final long serialVersionUID = 1056371805458446323L;
	@Min(0)
	private int number;
	@Min(1)
	private int size;
	
	private String property;
	
	private String filter;
	
	private String order;

	private String value;
	
	public Navigation() {
		super();
	}
	
	public Navigation(Builder builder) {
		this.number = builder.number;
		this.size = builder.size;
		this.property = builder.property;
		this.order = builder.order;
		this.value = builder.value;
		this.filter = builder.filter;
	}

	public int getNumber() {
		return number;
	}

	public int getSize() {
		return size;
	}

	public String getProperty() {
		return property;
	}

	public String getOrder() {
		return order;
	}

	public String getFilter() {
		return filter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Navigation [number=" + number + ", size=" + size + ", property=" + property + ", order=" + order
				+ ", value=" + value + "]";
	}

	public static class Builder {
		private int number;
		private int size;
		private String property;
		private String order;
		private String value;
		private String filter;

		public Builder setNumber(int number) {
			this.number = number;
			return this;
		}

		public Builder setSize(int size) {
			this.size = size;
			return this;
		}

		public Builder setProperty(String property) {
			this.property = property;
			return this;
		}

		public Builder setOrder(String order) {
			this.order = order;
			return this;
		}

		public Builder setValue(String value) {
			this.value = value;
			return this;
		}

		public Builder setFilter(String filter) {
			this.filter = filter;
			return this;
		}

		public static Builder newInstance() {
			return new Builder();
		}

		public Navigation build() {
			return new Navigation(this);
		}
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	public void setFilter(String filter) {
		this.filter = filter;
	}

	
	
}
