package fr.excilys.cdb.persistence.repositories;

import static fr.excilys.cdb.persistence.repositories.DbInit.APPLE_COMP;
import static fr.excilys.cdb.persistence.repositories.DbInit.CM_2A;
import static fr.excilys.cdb.persistence.repositories.DbInit.COMPANY;
import static fr.excilys.cdb.persistence.repositories.DbInit.COMPUTER;
import static fr.excilys.cdb.persistence.repositories.DbInit.DISCONTINUED;
import static fr.excilys.cdb.persistence.repositories.DbInit.ID_1;
import static fr.excilys.cdb.persistence.repositories.DbInit.ID_31;
import static fr.excilys.cdb.persistence.repositories.DbInit.ID_2;
import static fr.excilys.cdb.persistence.repositories.DbInit.INTRODUCED;
import static fr.excilys.cdb.persistence.repositories.DbInit.PAGE_1;
import static fr.excilys.cdb.persistence.repositories.DbInit.QUNTUM_MAC;
import static fr.excilys.cdb.persistence.repositories.DbInit.SIZE_5;
import static fr.excilys.cdb.persistence.repositories.DbInit.UPDATE_OK;
import static fr.excilys.cdb.persistence.repositories.DbInit.VALUE_20;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import fr.excilys.cdb.persistence.configuration.LoadApplicationContext;
import fr.excilys.cdb.persistence.configuration.SpringDataJpaConfig;
import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity.ComputerBuilder;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadApplicationContext.class, SpringDataJpaConfig.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Test Computer Dao")
public class ComputerRepositoryTest {
	
	@Autowired
	private ComputerRepository computerRepository;
	
	@Test
	public void test_getComputers_expect_success(){
		//execute
		List<ComputerEntity> computers = computerRepository.selectComputers();
		//verify
		assertThat(computers).isNotEmpty()
							 .hasSize(VALUE_20);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_success() {
		//prepare
		Pageable page = PageRequest.of(PAGE_1, SIZE_5);
		//execute
		Page<ComputerEntity> computers = computerRepository.selectComputersWithPage(page);
		//verify
		assertThat(computers).hasSize(5);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_first() {
		//prepare
		Pageable page = PageRequest.of(PAGE_1, SIZE_5);
		//execute
		Page<ComputerEntity> computers = computerRepository.selectComputersWithPage(page);
		//verify
		assertThat(computers).first().isEqualToComparingFieldByField(APPLE_COMP);
	}

	@Test
	public void test_getComputerByID_expect_computer_id_2() {
		//execute
		ComputerEntity computer = computerRepository.selectComputerById(ID_2).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(COMPUTER);
	}

	@Test
	public void test_getComputerByID_expect_no_computer() {
		//execute
		Optional<ComputerEntity> computer = computerRepository.selectComputerById(ID_31);
		//verify
		assertThat(computer.isPresent()).isFalse();
	}

	@Test
	public void test_addComputer_expect_susses() {
		//prepare
		ComputerEntity computer = ComputerBuilder.newInstance()
												 .setId(ID_31)
												 .setIntroduced(INTRODUCED)
												 .setDicontinued(DISCONTINUED)
												 .setCompany(COMPANY)
												 .build();
		//execute
		int addValue = computerRepository.saveComputer(computer);
		//verify
		assertThat(addValue).isEqualTo(UPDATE_OK);
	}

	@Test
	public void test_addComputer_expect_faild() {
		//prepare
		CompanyEntity company = CompanyBuilder.newInstance()
											  .setId(ID_31)
											  .setName(CM_2A)
											  .build();
		
		ComputerEntity computer = ComputerBuilder.newInstance()
												 .setId(ID_31)
												 .setIntroduced(INTRODUCED)
												 .setDicontinued(DISCONTINUED)
												 .setCompany(company)
												 .build();
		
		//verify && execute
		assertThatThrownBy(() -> { computerRepository.saveComputer(computer);})
				.isInstanceOf(DataIntegrityViolationException.class)
				.hasMessageEndingWith("could not execute statement");
	}

	@Test
	public void test_deleteComputer_expect_success() {
		//verify && execute
		assertThatCode(() -> { computerRepository.deleteById(ID_2);})
				.doesNotThrowAnyException();
	}

	@Test
	public void test_updateComputer_expect_success() {
		//prepare
		ComputerEntity computer = ComputerBuilder.newInstance()
						.setName(QUNTUM_MAC)
						.setId(ID_1)
						.setIntroduced(INTRODUCED)
						.setDicontinued(DISCONTINUED)
						.setCompany(COMPANY)
						.build();
		//execute
		int updatValue = computerRepository.updateComputer(computer);
		//verify
		assertThat(updatValue).isEqualTo(UPDATE_OK);
	}

	@Test
	public void test_MaxIdComputer_expect_success() {
		//execute
		long maxVlue = computerRepository.getMaxIdComputer();
		//verify
		assertThat(maxVlue).isEqualTo(20);
	}
	
}
