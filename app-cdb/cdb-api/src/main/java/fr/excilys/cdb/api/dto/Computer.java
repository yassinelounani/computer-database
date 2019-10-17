package fr.excilys.cdb.api.dto;

import java.time.LocalDate;
import java.util.Objects;

public class Computer {
	private long id;
	private String name;
	private LocalDate dateIntroduced;
	private LocalDate dateDiscontinued;
	private Company company;
	public Computer() {
		super();
	}
	public long getId() {
		return id;
	}
	public Computer setId(long id) {
		this.id = id;
		return this;
	}
	public String getName() {
		return name;
	}
	public Computer setName(String name) {
		this.name = name;
		return this;
	}
	public LocalDate getDateIntroduced() {
		return dateIntroduced;
	}
	public Computer setDateIntroduced(LocalDate dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
		return this;
	}
	public LocalDate getDateDiscontinued() {
		return dateDiscontinued;
	}
	public Computer setDateDiscontinued(LocalDate dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
		return this;
	}
	public Company getCompany() {
		return company;
	}
	public Computer setCompany(Company company) {
		this.company = company;
		return this;
	}
	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", dateIntroduce=" + dateIntroduced + ", dateDisconnected="
				+ dateDiscontinued + ", company=" + company + "]";
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null) return false;
	    if (getClass() != o.getClass()) return false;
	    Computer computer = (Computer) o;
	    return Objects.equals(id, computer.id)
	            && Objects.equals(name, computer.name)
	            && Objects.equals(dateIntroduced, computer.dateIntroduced)
	            && Objects.equals(dateDiscontinued, computer.dateDiscontinued)
	            && Objects.equals(company, computer.company);
	}
}