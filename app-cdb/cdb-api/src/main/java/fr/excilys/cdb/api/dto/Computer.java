package fr.excilys.cdb.api.dto;

import java.util.Objects;

import javax.validation.constraints.NotBlank;

import fr.excilys.cdb.api.validation.IntroducedBeforeDiscontinued;
import fr.excilys.cdb.api.validation.ValidDateFormat;

@IntroducedBeforeDiscontinued
public class Computer {

	private Long id;
	@NotBlank
	private String name;
	
	@ValidDateFormat(date = "introduced")
	private String introduced;
	@ValidDateFormat(date = "discontibued")
	private String discontinued;
	private Long idCompany;
	private String nameCompany;
	
	public Computer() {
		super();
	}
	
	private Computer(Builder builder) {
		super();
		this.id = builder.id;
		this.name = builder.name;
		this.introduced = builder.introduced;
		this.discontinued = builder.discontinued;
		this.idCompany = builder.idCompany;
		this.nameCompany = builder.nameCompany;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public Long getIdCompany() {
		return idCompany;
	}	

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public void setIdCompany(Long idCompany) {
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
		return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
				+ discontinued + ", idCompany=" + idCompany + "]";
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
	            && Objects.equals(introduced, computer.introduced)
	            && Objects.equals(discontinued, computer.discontinued)
	            && Objects.equals(idCompany, computer.idCompany);
	}

	@Override
	public int hashCode() {
		 return Objects.hash(id, name, introduced, discontinued, idCompany);
	}

	public static class Builder {

		private Long id;
		private String name;
		private String introduced;
		private String discontinued;
		private Long idCompany;
		private String nameCompany;

		public static Builder newInstance() {
			return new Builder();
		}

		private Builder() {}
		
		public Builder setId(long id) {
			this.id = id;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setIntroduced(String introduced) {
			this.introduced = introduced;
			return this;
		}

		public Builder setDicontinued(String discontinued) {
			this.discontinued = discontinued;
			return this;
		}

		public Builder setIdCompany(Long idCompany) {
			this.idCompany = idCompany;
			return this;
		}

		public Computer build() {
			return new Computer(this);
		}

		public Builder setNameComapny(String nameCompany) {
			this.nameCompany = nameCompany;
			return this;
		}
	}

}