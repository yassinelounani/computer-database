package models;

import java.time.LocalDate;

public class Computer {

	private long id;
	private String name;
	private LocalDate dateIntroduced;
	private LocalDate dateDiscontinued;
	private Company company;
	
	public Computer(String name) {
		super();
		this.name = name;
	}
	
	public Computer(long id, String name, LocalDate dateIntroduced, LocalDate dateDiscontinued, Company company) {
		super();
		this.id = id;
		this.name = name;
		this.dateIntroduced = dateIntroduced;
		this.dateDiscontinued = dateDiscontinued;
		this.company = company;
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

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", dateIntroduce=" + dateIntroduced + ", dateDisconnected="
				+ dateDiscontinued + ", company=" + company + "]";
	}

	
}
