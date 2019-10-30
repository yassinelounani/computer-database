package fr.excilys.cdb.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


public class NameAndPage {
	
	@NotBlank
	private String name;
	@Valid
	private Page page;
	
	public NameAndPage(Builder builder) {
		this.name = builder.name;
		this.page = builder.page;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	@Override
	public String toString() {
		return "NameAndPage [name=" + name + ", page=" + page + "]";
	}

	public static class Builder {

		private String name;
		private Page page;

		public static Builder newInstance() {
			return new Builder();
		}

		private Builder() {}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setPage(Page page) {
			this.page = page;
			return this;
		}

		public NameAndPage build() {
			return new NameAndPage(this);
		}
	}
}
