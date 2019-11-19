package fr.excilys.cdb.persistence.repositories;

import static fr.excilys.cdb.persistence.repositories.DbInit.APPLE;
import static fr.excilys.cdb.persistence.repositories.DbInit.COMOMMODORE;
import static fr.excilys.cdb.persistence.repositories.DbInit.ID_1;
import static fr.excilys.cdb.persistence.repositories.DbInit.ID_31;
import static fr.excilys.cdb.persistence.repositories.DbInit.PAGE_1;
import static fr.excilys.cdb.persistence.repositories.DbInit.SIZE_5;
import static fr.excilys.cdb.persistence.repositories.DbInit.VALUE_10;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.excilys.cdb.persistence.configuration.LoadApplicationContext;
import fr.excilys.cdb.persistence.configuration.SpringDataJpaConfig;
import fr.excilys.cdb.persistence.models.CompanyEntity;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadApplicationContext.class, SpringDataJpaConfig.class })
public class CompanyRepositoryTest {

	@Autowired
	private CompanyRepository companyRepository;

	@Test
	public void test_getComputers_expect_success(){
		//execute
		List<CompanyEntity> companies = companyRepository.selectCompanies();
		//verify
		assertThat(companies).isNotEmpty()
							 .hasSize(VALUE_10);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_success() {
		//prepare
		Pageable page = PageRequest.of(PAGE_1, SIZE_5);
		//execute
		Page<CompanyEntity> computers = companyRepository.selectCompaniesWithPage(page);
		//verify
		assertThat(computers).hasSize(5);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_first() {
		//prepare
		Pageable page = PageRequest.of(PAGE_1, SIZE_5);
		//execute
		Page<CompanyEntity> computers = companyRepository.selectCompaniesWithPage(page);
		//verify
		assertThat(computers).first().isEqualToComparingFieldByField(COMOMMODORE);
	}

	@Test
	public void test_getComputerByID_expect_computer_id_1() {
		//execute
		CompanyEntity computer = companyRepository.selectCompanyById(ID_1).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(APPLE);
	}

	@Test
	public void test_getComputerByID_expect_no_computer() {
		//execute
		Optional<CompanyEntity> computer = companyRepository.selectCompanyById(ID_31);
		//verify
		assertThat(computer.isPresent()).isFalse();
	}

}
