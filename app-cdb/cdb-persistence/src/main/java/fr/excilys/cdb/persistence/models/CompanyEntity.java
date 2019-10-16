package fr.excilys.cdb.persistence.models;

import java.util.Objects;

public class CompanyEntity {

	private long id;
	private String name;
	
	public CompanyEntity(CompanyBuilder builder) {
		super();
		this.id = builder.getId();
		this.name = builder.getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	
}
