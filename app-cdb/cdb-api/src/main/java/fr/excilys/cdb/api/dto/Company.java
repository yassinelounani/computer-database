package fr.excilys.cdb.api.dto;

public final class Company {
	
	private long id;
	private String name;
	
	
	public Company() {
		super();
	}
	
	public String getName() {
		return name;
		
	}

	public Company setName(String name) {
		this.name = name;
		return this;
	}
	
	public long getId() {
		return id;
	}

	public Company setId(long id) {
		this.id = id;
		return this;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}
}
