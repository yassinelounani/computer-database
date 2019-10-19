package fr.excilys.cdb.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.dao.ComputerDao;
import fr.excilys.cdb.persistence.mappers.Mapper;
import fr.excilys.cdb.persistence.models.CompanyBuilder;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;

public class ComputerServiceExporterTest {

	private static final LocalDate DISCONTINUED_HP = LocalDate.of(2030, 3, 10);
	private static final LocalDate INTRODUCED_HP = LocalDate.of(2020, 10, 12);
	private static final String HP_LITE = "HP Lite 1234";
	private static final LocalDate DISCONTINUED_MAC = LocalDate.of(2022, 3, 21);
	private static final LocalDate INTRODUCED_MAC = LocalDate.of(2020, 1, 10);
	private static final String MAC_BOOK_PRO = "MacBook Pro 2020";
	private static final String APPLE = "Apple";
	private static final int ID_2 = 2;
	private static final int ID_1 = 1;
	private static final int UPDATE_OK = 1;

	private static CompanyEntity COMPANY = CompanyBuilder.newInstance()
														 .setId(ID_1)
														 .setName(APPLE)
														 .build();
	private static ComputerEntity COMPUTER_MAC = ComputerBuilder.newInstance()
																.setId(ID_1)
																.setName(MAC_BOOK_PRO)
																.setIntroduced(INTRODUCED_MAC)
																.setDicontinued(DISCONTINUED_MAC)
																.setCompany(COMPANY)
																.build();

	private static ComputerEntity COMPUTER_HP = ComputerBuilder.newInstance()
			.setId(ID_2)
			.setName(HP_LITE)
			.setIntroduced(INTRODUCED_HP)
			.setDicontinued(DISCONTINUED_HP)
			.setCompany(COMPANY)
			.build();
	

	private ComputerServiceExporter computerService;

	@BeforeEach
	public void before() {
		ComputerServiceExporter.instance = null;
	}

	
	public void test_getComputers_expect_success() {
		//prepare
		ComputerDao mockDao = mock(ComputerDao.class);
		List<ComputerEntity> computerEntities = Arrays.asList(COMPUTER_HP, COMPUTER_MAC);
		List<Computer> computer = Mapper.mapAll(computerEntities, Computer.class);
		when(mockDao.getComputers()).thenReturn(computerEntities);
		computerService = ComputerServiceExporter.getInstance();
		//execute
		List<Computer> getComputers = computerService.getComputers();
		//verify
		assertThat(getComputers).containsExactlyElementsOf(computer);
	}

	
	public void test_getComputerById_expect_success() {
		//prepare
		ComputerDao mockDao = mock(ComputerDao.class);
		Mockito.reset(mockDao);
		doReturn(Optional.of(COMPUTER_MAC)).when(mockDao).getComputerById(ID_1);
		computerService = ComputerServiceExporter.getInstance();
		System.out.println(computerService);
		//execute
		Computer computer = computerService.getComputerById(ID_1).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(computer);
	}

	
	public void test_addComputer_expect_exception() {
		//prepare
		CompanyDao mockCompany = mock(CompanyDao.class);
		Computer computer = Mapper.map(COMPUTER_HP, Computer.class);
		doReturn(Optional.empty()).when(mockCompany).getCompanyById(ID_2);
		computerService = ComputerServiceExporter.getInstance();
		//execute
		assertThatThrownBy(() -> { computerService.addComputer(computer);})
				.isInstanceOf(NotFoundCompanyException.class)
				.hasMessageContaining("Company with id :"+ ID_2 + " not Exist referenced by company_id");
	}

	
	public void test_addComputer_expect_success() {
		//prepare
		CompanyDao mockCompany = mock(CompanyDao.class);
		Mockito.reset(mockCompany);
		ComputerDao mockComputer = mock(ComputerDao.class);
		Computer computer = Mapper.map(COMPUTER_MAC, Computer.class);
		doReturn(Optional.of(COMPANY)).when(mockCompany).getCompanyById(ID_1);
		doReturn(UPDATE_OK).when(mockComputer).addComputer(COMPUTER_MAC);
		computerService = ComputerServiceExporter.getInstance();
		//execute && verify 
		assertThatCode(() -> { 
			int addValue = computerService.addComputer(computer);
			assertThat(addValue).isEqualTo(UPDATE_OK);
		}).doesNotThrowAnyException();
	}
}
