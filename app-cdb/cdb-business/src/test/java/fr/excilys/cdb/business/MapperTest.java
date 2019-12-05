package fr.excilys.cdb.business;

import static fr.excilys.cdb.business.Helper.mapToComputer;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Computer.Builder;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity.ComputerBuilder;

public class MapperTest {
	
	private static final LocalDate DISCONTINUED = LocalDate.of(2020, 6, 13);
	private static final LocalDate INTRODUCED = LocalDate.of(2019,  4,  12);
	private static final String COMPUTER = "computer";
	private static final String COMPANY = "company";
	private static final long ID_1 = 1;

	@Test
	public void test_maptoComputer_expect_succes() {
		//prepare
		CompanyEntity company = CompanyBuilder.newInstance()
				.setId(ID_1)
				.setName(COMPANY)
				.build();
		
		ComputerEntity computerEntity = ComputerBuilder.newInstance()
				.setId(ID_1)
				.setName(COMPUTER)
				.setIntroduced(INTRODUCED)
				.setDicontinued(DISCONTINUED)
				.setCompany(company)
				.build();
		Computer computer = Builder.newInstance()
				.setId(ID_1)
				.setName(COMPUTER)
				.setIntroduced(INTRODUCED.toString())
				.setDicontinued(DISCONTINUED.toString())
				.setIdCompany(ID_1)
				.build();
		//execute
		Computer newComputer = mapToComputer(computerEntity);
		//verify
		assertThat(newComputer).isEqualToComparingFieldByField(computer);
	}
}
