package fr.excilys.cdb.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Page;
import fr.excilys.cdb.persistence.dao.CompanyDao;
import fr.excilys.cdb.persistence.dao.ComputerDao;
import fr.excilys.cdb.persistence.dao.ConnectionToDb;
import fr.excilys.cdb.persistence.mappers.Mapper;
import fr.excilys.cdb.persistence.models.CompanyBuilder;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.ComputerBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.Pageable;

@RunWith(MockitoJUnitRunner.class)
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
	private static CompanyEntity COMPANY = CompanyBuilder.newInstance()
														 .setId(ID_1)
														 .setName(APPLE)
														 .build();
	private static ComputerEntity COMPUTER_MAC = ComputerBuilder.newInstance()
																.setId(1)
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
	
	
	private ComputerDao computerDao;
	private CompanyDao companyDao;
	
	@BeforeEach
	public void before() {
		ConnectionToDb connectionTodb = new ConnectionToDb();
		companyDao = CompanyDao.getInstance(connectionTodb);
		computerDao = ComputerDao.getInstance(connectionTodb);
	}

	@Test
	public void test_getComputers_expect_success() {
		//prepare
		ComputerDao mockDao = mock(computerDao.getClass());
		List<ComputerEntity> computerEntities = Arrays.asList(COMPUTER_HP, COMPUTER_MAC);
		List<Computer> computer = Mapper.mapAll(computerEntities, Computer.class);
		doReturn(computerEntities).when(mockDao).getComputers();
		ComputerServiceExporter computerService = ComputerServiceExporter.getInstance(mockDao, companyDao);
		//execute
		List<Computer> getComputers = computerService.getComputers();
		//verify
		assertThat(getComputers).containsExactlyElementsOf(computer);
	}

	@Test
	public void test_getComputers_with_Page_expect_success() {
		//prepare
		ComputerDao mock = mock(computerDao.getClass());
		Page page = new Page(1, 2);
		Pageable pageable = Mapper.map(page, Pageable.class);
		List<ComputerEntity> computerEntities = Arrays.asList(COMPUTER_HP, COMPUTER_MAC);
		List<Computer> computer = Mapper.mapAll(computerEntities, Computer.class);
		doReturn(computerEntities).when(mock).getComputersWithPage(pageable);
		ComputerServiceExporter computerService = ComputerServiceExporter.getInstance(mock, companyDao);
		//execute
		List<Computer> getComputers = computerService.getComputersWithPage(page);
		//verify
		assertThat(getComputers).containsExactlyElementsOf(computer);
	}

	@Test
	public void test_getComputerById_with_Page_expect_success() {
		//prepare
		ComputerDao mock = mock(computerDao.getClass());
		doReturn(Optional.of(COMPUTER_HP)).when(mock).getComputerById(ID_1);
		ComputerServiceExporter computerService = ComputerServiceExporter.getInstance(mock, companyDao);
		//execute
		Computer computer = computerService.getComputerById(ID_1).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(computer);
	}
	

	

}
