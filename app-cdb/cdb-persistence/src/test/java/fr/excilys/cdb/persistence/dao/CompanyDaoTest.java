package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.DbInit.APPLE;
import static fr.excilys.cdb.persistence.dao.DbInit.ID_1;
import static fr.excilys.cdb.persistence.dao.DbInit.ID_100;
import static fr.excilys.cdb.persistence.dao.DbInit.PAGE_1;
import static fr.excilys.cdb.persistence.dao.DbInit.PAGE_2;
import static fr.excilys.cdb.persistence.dao.DbInit.SIZE_5;
import static fr.excilys.cdb.persistence.dao.DbInit.VALUE_10;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.Pageable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadApplicationContext.class})
public class CompanyDaoTest {

	@Autowired
	private CompanyDao companyDao;

	@Test
	public void test_getComputers_expect_success(){
		//execute
		List<CompanyEntity> companies = companyDao.getCompanies();
		//verify
		assertThat(companies).isNotEmpty()
							 .hasSize(VALUE_10);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_success() {
		//prepare
		Pageable pageable = new Pageable(PAGE_2,SIZE_5);
		//execute
		List<CompanyEntity> computers = companyDao.getCompaniesWithPage(pageable);
		//verify
		assertThat(computers).hasSize(5);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_first() {
		//prepare
		Pageable pageable = new Pageable(PAGE_1, SIZE_5);
		//execute
		List<CompanyEntity> computers = companyDao.getCompaniesWithPage(pageable);
		//verify
		assertThat(computers).first().isEqualToComparingFieldByField(APPLE);
	}

	@Test
	public void test_getComputerByID_expect_computer_id_1() {
		//execute
		CompanyEntity computer = companyDao.getCompanyById(ID_1).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(APPLE);
	}

	@Test
	public void test_getComputerByID_expect_no_computer() {
		//execute
		Optional<CompanyEntity> computer = companyDao.getCompanyById(ID_100);
		//verify
		assertThat(computer.isPresent()).isFalse();
	}

}
