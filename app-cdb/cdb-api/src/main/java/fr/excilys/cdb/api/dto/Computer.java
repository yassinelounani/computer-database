package fr.excilys.cdb.api.dto;

import java.util.Objects;

public class Computer {

	private long id;
	private String name;
	private String dateIntroduced;
	private String dateDiscontinued;
	private String idCompany;
	private String nameCompany;

	public Computer() {
		super();
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

	public String getDateIntroduced() {
		return dateIntroduced;
	}

	public void setDateIntroduced(String dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
	}

	public String getDateDiscontinued() {
		return dateDiscontinued;
	}

	public void setDateDiscontinued(String dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
	}

	public String getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(String idCompany) {
		this.idCompany = idCompany;
	}

	public String getNameCompany() {
		return nameCompany;
	}

	public void setNameCompany(String nameCompany) {
		this.nameCompany = nameCompany;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", dateIntroduced=" + dateIntroduced + ", dateDiscontinued="
				+ dateDiscontinued + ", idCompany=" + idCompany + ", nameCompany=" + nameCompany + "]";
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
	    Computer computer = (Computer) o;
	    return Objects.equals(id, computer.id)
	            && Objects.equals(name, computer.name)
	            && Objects.equals(dateIntroduced, computer.dateIntroduced)
	            && Objects.equals(dateDiscontinued, computer.dateDiscontinued);
	}

	@Override
	public int hashCode() {
		 return Objects.hash(id, name, dateIntroduced, dateDiscontinued);
	}
}