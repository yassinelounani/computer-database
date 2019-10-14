package fr.excilys.cdb.persistence.models;

public class CompanyBuilder {
	
	private long id;
	private String name;
	
	private CompanyBuilder() {}
	
	public static CompanyBuilder newInstance() {
		return new CompanyBuilder();
	}
	
	public CompanyBuilder setId(long id) {
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

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "CompanyBuilder [id=" + id + ", name=" + name + "]";
	}
	
	

}
