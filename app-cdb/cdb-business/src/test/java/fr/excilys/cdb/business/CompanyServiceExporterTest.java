package fr.excilys.cdb.business;

import static fr.excilys.cdb.persistence.mappers.Mapper.mapAll;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.excilys.cdb.api.CompanyService;
import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.repositories.CompanyRepository;
import fr.excilys.cdb.persistence.repositories.ComputerRepository;

public class CompanyServiceExporterTest {
	
	private static final String APPLE = "Apple";
	private static final long ID_1 = 1;

	private static CompanyEntity COMPANY_APPLE = CompanyBuilder.newInstance()
			 .setId(ID_1)
			 .setName(APPLE)
			 .build();
	private static CompanyEntity COMPANY_HP = CompanyBuilder.newInstance()
			 .setId(ID_1)
			 .setName(APPLE)
			 .build();

	@Test
	public void test_getCompanies_expect_success() {
		//prepare
		CompanyRepository mock = mock(CompanyRepository.class);
		ComputerRepository mockComputer = mock(ComputerRepository.class);
		CompanyService companyService = new CompanyServiceExporter(mock, mockComputer);
		List<CompanyEntity> companyEntities = Arrays.asList(COMPANY_APPLE, COMPANY_HP);
		List<Company> companies = mapAll(companyEntities, Company.class);
		doReturn(companyEntities).when(mock).selectCompanies();
		//execute
		List<Company> getComputers = companyService.getCompanies();
		//verify
		assertThat(getComputers).containsExactlyElementsOf(companies);
	}

}
