package models;

public class Company {

	private long id;
	private String name;
	
	public Company(CompanyBuilder builder) {
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
	
}
