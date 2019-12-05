package fr.excilys.cdb.persistence.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class CompanyEntity {
	@Id
	private Long id;
	private String name;
	
	public CompanyEntity() {
		super();
	}

	private CompanyEntity(CompanyBuilder builder) {
		super();
		this.id = builder.id;
		this.name = builder.name;
	}
	
	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null) return false;
	    if (getClass() != o.getClass()) return false;
	    CompanyEntity company = (CompanyEntity) o;
	    return Objects.equals(id, company.id)
	            && Objects.equals(name, company.name);
	}

	public static class CompanyBuilder {

		private Long id;
		private String name;

		private CompanyBuilder() {}

		public static CompanyBuilder newInstance() {
			return new CompanyBuilder();
		}

		public CompanyBuilder setId(Long id) {
			this.id = id;
			return this;
		}
		public CompanyBuilder setName(String name) {
			this.name = name;
			return this;
		}

		public CompanyEntity build() {
			return new CompanyEntity(this);
		}
	}
}
