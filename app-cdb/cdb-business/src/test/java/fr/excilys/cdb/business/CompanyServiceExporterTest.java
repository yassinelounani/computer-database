package fr.excilys.cdb.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.excilys.cdb.api.dto.Company;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.mappers.Mapper;
import fr.excilys.cdb.persistence.models.CompanyBuilder;
import fr.excilys.cdb.persistence.models.CompanyEntity;

public class CompanyServiceExporterTest {
	
	private static final String APPLE = "Apple";
	private static final int ID_1 = 1;
	
	
	private static CompanyEntity COMPANY_APPLE = CompanyBuilder.newInstance()
			 .setId(ID_1)
			 .setName(APPLE)
			 .build();
	private static CompanyEntity COMPANY_HP = CompanyBuilder.newInstance()
			 .setId(ID_1)
			 .setName(APPLE)
			 .build();
	
	private CompanyServiceExporter companyService;
	
	@BeforeEach
	public void before() {
		CompanyServiceExporter.instance = null;
	}
	
	
	public void test_getComputers_expect_success() {
		//prepare
		CompanyDao mock = mock(CompanyDao.class);
		List<CompanyEntity> companyEntities = Arrays.asList(COMPANY_APPLE, COMPANY_HP);
		List<Company> companies = Mapper.mapAll(companyEntities, Company.class);
		doReturn(companyEntities).when(mock).getCompanies();
		companyService = CompanyServiceExporter.getInstance();
		System.out.println(companyService);
		//execute
		List<Company> getComputers = companyService.getCompanies();
		//verify
		assertThat(getComputers).containsExactlyElementsOf(companies);
	}
	
	


}
