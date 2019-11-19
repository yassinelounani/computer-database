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
import org.junit.jupiter.api.Test;
import fr.excilys.cdb.api.ComputerService;
import fr.excilys.cdb.api.dto.Computer;
import fr.excilys.cdb.api.dto.Identifier;
import fr.excilys.cdb.api.exception.NotFoundCompanyException;
import static fr.excilys.cdb.business.Helper.mapAll;
import static fr.excilys.cdb.business.Helper.mapToComputer;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity.ComputerBuilder;
import fr.excilys.cdb.persistence.repositories.CompanyRepository;
import fr.excilys.cdb.persistence.repositories.ComputerRepository;

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

	@Test
	public void test_getComputers_expect_success() {
		//prepare
		ComputerRepository mockComputerRepository = mock(ComputerRepository.class);
		CompanyRepository mockCompanyRepository = mock(CompanyRepository.class);
		ComputerService computerService = new ComputerServiceExporter(mockComputerRepository, mockCompanyRepository);
		List<ComputerEntity> computerEntities = Arrays.asList(COMPUTER_HP, COMPUTER_MAC);
		List<Computer> computer = mapAll(computerEntities);
		when(mockComputerRepository.selectComputers()).thenReturn(computerEntities);
		//execute
		List<Computer> getComputers = computerService.getComputers();
		//verify
		assertThat(getComputers).containsExactlyElementsOf(computer);
	}

	@Test
	public void test_getComputerById_expect_success() {
		//prepare
		ComputerRepository mockComputerRepository = mock(ComputerRepository.class);
		CompanyRepository mockCompanyRepository = mock(CompanyRepository.class);
		ComputerService computerService = new ComputerServiceExporter(mockComputerRepository, mockCompanyRepository);
		doReturn(Optional.of(COMPUTER_MAC)).when(mockComputerRepository).selectComputerById(ID_1);
		//execute
		Identifier computerId = new Identifier(ID_1);
		Computer computer = computerService.getComputerById(computerId).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(computer);
	}

	@Test
	public void test_addComputer_expect_exception() {
		//prepare
		ComputerRepository mockComputerRepository = mock(ComputerRepository.class);
		CompanyRepository mockCompanyRepository = mock(CompanyRepository.class);
		ComputerService computerService = new ComputerServiceExporter(mockComputerRepository, mockCompanyRepository);
		Computer computer = mapToComputer(COMPUTER_HP);
		doReturn(Optional.empty()).when(mockCompanyRepository).selectCompanyById(ID_1);
		//execute
		assertThatThrownBy(() -> { computerService.addComputer(computer);})
				.isInstanceOf(NotFoundCompanyException.class)
				.hasMessageContaining("Company with id :"+ ID_1 + " not Exist referenced by company_id");
	}

	@Test
	public void test_addComputer_expect_success() {
		//prepare
		ComputerRepository mockComputerRepository = mock(ComputerRepository.class);
		CompanyRepository mockCompanyRepository = mock(CompanyRepository.class);
		ComputerService computerService = new ComputerServiceExporter(mockComputerRepository, mockCompanyRepository);
		Computer computer = mapToComputer(COMPUTER_MAC);
		doReturn(Optional.of(COMPANY)).when(mockCompanyRepository).selectCompanyById(ID_1);
		doReturn(UPDATE_OK).when(mockComputerRepository).saveComputer(COMPUTER_MAC);
		//execute && verify 
		assertThatCode(() -> {
			int addComputer = computerService.addComputer(computer);
			assertThat(addComputer).isEqualTo(UPDATE_OK);
		}).doesNotThrowAnyException();
	}
}
