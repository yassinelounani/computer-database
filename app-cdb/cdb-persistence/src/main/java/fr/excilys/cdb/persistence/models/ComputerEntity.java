package fr.excilys.cdb.persistence.models;

import java.time.LocalDate;

public class ComputerEntity {

	private long id;
	private String name;
	private LocalDate dateIntroduced;
	private LocalDate dateDiscontinued;
	private CompanyEntity company;
	
	public ComputerEntity() {
		super();
	}
	
	public ComputerEntity(ComputerBuilder builder){
		super();
		this.id = builder.getId();
		this.name = builder.getName();
		this.dateIntroduced = builder.getIntroduced();
		this.dateDiscontinued = builder.getDiscontinued();
		this.company = builder.getCompany();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public LocalDate getDateIntroduced() {
		return dateIntroduced;
	}

	public void setDateIntroduced(LocalDate dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
	}

	public LocalDate getDateDiscontinued() {
		return dateDiscontinued;
	}

	public void setDateDiscontinued(LocalDate dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
	}

	public CompanyEntity getCompany() {
		return company;
	}

	public void setCompany(CompanyEntity company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", dateIntroduce=" + dateIntroduced + ", dateDisconnected="
				+ dateDiscontinued + ", company=" + company + "]";
	}

	
}
