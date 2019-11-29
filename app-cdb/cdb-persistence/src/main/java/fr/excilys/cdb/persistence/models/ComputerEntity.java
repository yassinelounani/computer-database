package fr.excilys.cdb.persistence.models;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "Computer")
public class ComputerEntity {
	@Id
	private long id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private CompanyEntity company;
	
	public ComputerEntity() {
		super();
	}

	private ComputerEntity(ComputerBuilder builder){
		super();
		this.id = builder.id;
		this.name = builder.name;
		this.introduced = builder.introduced;
		this.discontinued = builder.discontinued;
		this.company = builder.company;
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

	public CompanyEntity getCompany() {
		return company;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
				+ discontinued + ", company=" + company + "]";
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null) return false;
	    if (getClass() != o.getClass()) return false;
	    ComputerEntity computer = (ComputerEntity) o;
	    return Objects.equals(id, computer.id)
	            && Objects.equals(name, computer.name)
	            && Objects.equals(introduced, computer.introduced)
	            && Objects.equals(discontinued, computer.discontinued)
	            && Objects.equals(company, computer.company);
	}

	public static class ComputerBuilder {
		private long id;
		private String name;
		private LocalDate introduced;
		private LocalDate discontinued;
		private CompanyEntity company;

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

		public ComputerBuilder setCompany(CompanyEntity company) {
			this.company = company;
			return this;
		}

		public ComputerEntity build() {
			return new ComputerEntity(this);
		}
	}
}
