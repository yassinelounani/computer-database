package fr.excilys.cdb.api.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

public final class Company {
	
	private long id;
	@NotBlank
	private String name;

	public Company() {
		super();
	}

	public Company(Builder builder) {
		super();
		this.id = builder.id;
		this.name = builder.name;
	}

	public String getName() {
		return name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) {
	    	return true;
	    }
	    if (o == null) {
	    	return false;
	    }
	    if (getClass() != o.getClass()) {
	    	return false;
	    }
	    Company company = (Company) o;
	    return Objects.equals(id, company.id)
	            && Objects.equals(name, company.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
	
	public static class Builder {
		private long id;
		private String name;
		
		public static Builder newInstance() {
			return new Builder();
		}

		private Builder() {}

		public Builder setId(long id) {
			this.id = id;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Company build() {
			return new Company(this);
		}
	}

}
