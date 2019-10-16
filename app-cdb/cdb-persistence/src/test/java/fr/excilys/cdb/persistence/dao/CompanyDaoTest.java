package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.DbInit.COMPANY;
import static fr.excilys.cdb.persistence.dao.DbInit.ID_100;
import static fr.excilys.cdb.persistence.dao.DbInit.ID_1;
import static fr.excilys.cdb.persistence.dao.DbInit.PAGE_2;
import static fr.excilys.cdb.persistence.dao.DbInit.VALUE_10;
import static fr.excilys.cdb.persistence.dao.DbInit.PAGE_1;
import static fr.excilys.cdb.persistence.dao.DbInit.SIZE_5;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.Pageable;

public class CompanyDaoTest {

private CompanyDao companyDao;
	
	
	@BeforeEach
	public void beforeEach() {
		System.setProperty("testing", "true");
		ConnectionToDb connectionToDb = new ConnectionToDb();
		companyDao = CompanyDao.getInstance(connectionToDb);
		
	}
	
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
		Pageable pageable = new Pageable(PAGE_2, SIZE_5);
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
		assertThat(computers).first().isEqualToComparingFieldByField(COMPANY);
	}
	
	@Test
	public void test_getComputerByID_expect_computer_id_1() {
		//execute
		CompanyEntity computer = companyDao.getCompanyById(ID_1).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(COMPANY);
	}
	@Test
	public void test_getComputerByID_expect_no_computer() {
		//execute
		Optional<CompanyEntity> computer = companyDao.getCompanyById(ID_100);
		//verify
		assertThat(computer.isPresent()).isFalse();
	}
	
	
	@AfterEach
	public void afterAll() {
		System.setProperty("testing", "false");
	}
}
