package fr.excilys.cdb.persistence.dao;

import static fr.excilys.cdb.persistence.dao.DbInit.CM_2A;
import static fr.excilys.cdb.persistence.dao.DbInit.COMPANY;
import static fr.excilys.cdb.persistence.dao.DbInit.COMPUTER;
import static fr.excilys.cdb.persistence.dao.DbInit.DISCONTINUED;
import static fr.excilys.cdb.persistence.dao.DbInit.ID_1;
import static fr.excilys.cdb.persistence.dao.DbInit.ID_100;
import static fr.excilys.cdb.persistence.dao.DbInit.ID_2;
import static fr.excilys.cdb.persistence.dao.DbInit.INTRODUCED;
import static fr.excilys.cdb.persistence.dao.DbInit.PAGE_2;
import static fr.excilys.cdb.persistence.dao.DbInit.QUNTUM_MAC;
import static fr.excilys.cdb.persistence.dao.DbInit.SIZE_5;
import static fr.excilys.cdb.persistence.dao.DbInit.UPDATE_KO;
import static fr.excilys.cdb.persistence.dao.DbInit.UPDATE_OK;
import static fr.excilys.cdb.persistence.dao.DbInit.VALUE_20;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.excilys.cdb.persistence.models.CompanyEntity;
import fr.excilys.cdb.persistence.models.CompanyEntity.CompanyBuilder;
import fr.excilys.cdb.persistence.models.ComputerEntity;
import fr.excilys.cdb.persistence.models.ComputerEntity.ComputerBuilder;
import fr.excilys.cdb.persistence.models.Pageable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadApplicationContext.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Test Computer Dao")
public class ComputerDaoTest {
	
	@Autowired
	private ComputerDao computerDao;
	
	@Test
	public void test_getComputers_expect_success(){
		//execute
		List<ComputerEntity> computers = computerDao.getComputers();
		//verify
		assertThat(computers).isNotEmpty()
							 .hasSize(VALUE_20);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_success() {
		//prepare
		Pageable pageable = new Pageable(PAGE_2, SIZE_5);
		//execute
		List<ComputerEntity> computers = computerDao.getComputersWithPage(pageable);
		//verify
		assertThat(computers).hasSize(5);
	}

	@Test
	public void test_getAllComputer_with_Page_expect_first() {
		//prepare
		Pageable pageable = new Pageable(PAGE_2, SIZE_5);
		//execute
		List<ComputerEntity> computers = computerDao.getComputersWithPage(pageable);
		//verify
		assertThat(computers).first().isEqualToComparingFieldByField(COMPUTER);
	}

	@Test
	public void test_getComputerByID_expect_computer_id_6() {
		//execute
		ComputerEntity computer = computerDao.getComputerById(ID_2).get();
		//verify
		assertThat(computer).isEqualToComparingFieldByField(COMPUTER);
	}

	@Test
	public void test_getComputerByID_expect_no_computer() {
		//execute
		Optional<ComputerEntity> computer = computerDao.getComputerById(ID_100);
		//verify
		assertThat(computer.isPresent()).isFalse();
	}

	@Test
	public void test_addComputer_expect_susses() {
		//prepare
		ComputerEntity computer = ComputerBuilder.newInstance()
												 .setId(ID_100)
												 .setIntroduced(INTRODUCED)
												 .setDicontinued(DISCONTINUED)
												 .setCompany(COMPANY)
												 .build();
		//execute
		int addValue = computerDao.addComputer(computer);
		//verify
		assertThat(addValue).isEqualTo(UPDATE_OK);
	}

	@Test
	public void test_addComputer_expect_faild() {
		//prepare
		CompanyEntity company = CompanyBuilder.newInstance()
											  .setId(ID_100)
											  .setName(CM_2A)
											  .build();
		
		ComputerEntity computer = ComputerBuilder.newInstance()
												 .setId(ID_100)
												 .setIntroduced(INTRODUCED)
												 .setDicontinued(DISCONTINUED)
												 .setCompany(company)
												 .build();
		//execute
		int addValue = computerDao.addComputer(computer);
		//verify
		assertThat(addValue).isEqualTo(UPDATE_KO);
	}

	@Test
	public void test_deleteComputer_expect_success() {
		//execute
		int deleteValue = computerDao.deleteComputerById(ID_2);
		//verify
		assertThat(deleteValue).isEqualTo(UPDATE_OK);
	}

	@Test
	public void test_deleteComputer_expect_faild() {
		//execute
		int deleteValue = computerDao.deleteComputerById(ID_100);
		//verify
		assertThat(deleteValue).isEqualTo(UPDATE_KO);
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
		int updatValue = computerDao.updateComputer(computer);
		//verify
		assertThat(updatValue).isEqualTo(UPDATE_OK);
	}

	@Test
	public void test_MaxIdComputer_expect_success() {
		//execute
		long maxVlue = computerDao.getMaxIdComputer();
		//verify
		assertThat(maxVlue).isEqualTo(20);
	}
	
}
