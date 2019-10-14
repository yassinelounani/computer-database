package models;

import java.time.LocalDate;

public class ComputerBuilder {
	
	private long id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private Company company;
	
	public static ComputerBuilder newInstance() {
		return new ComputerBuilder();
	}
	
	private ComputerBuilder() {}
	
	public ComputerBuilder setId(long id) {
		this.id = id;
		return this;
	}
	
	public ComputerBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public ComputerBuilder setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
		return this;
	}
	
	public ComputerBuilder setDicontinued(LocalDate discontinued) {
		this.discontinued = discontinued;
		return this;
	}
	
	public ComputerBuilder setCompany(Company company) {
		this.company = company;
		return this;
	}
	
	public Computer build() {
		return new Computer(this);
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDate getIntroduced() {
		return introduced;
	}

	public LocalDate getDiscontinued() {
		return discontinued;
	}

	public Company getCompany() {
		return company;
	}

	@Override
	public String toString() {
		return "ComputerBuilder [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
				+ discontinued + ", company=" + company + "]";
	}
	
	
	
	

}
