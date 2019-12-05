package fr.excilys.cdb.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import fr.excilys.cdb.persistence.models.CompanyEntity;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

	@Query("SELECT company "
			 + "FROM CompanyEntity company")
	List<CompanyEntity> selectCompanies();


	@Query("SELECT company "
		+ "FROM CompanyEntity company ")
	Page<CompanyEntity> selectCompaniesWithPage(Pageable pageable);

	@Query("SELECT company "
		 + "FROM CompanyEntity company "
		 + "Where company.id = :id")
	Optional<CompanyEntity> selectCompanyById(@Param("id") long id);
	
	@Query(value = "SELECT company "
			 + "FROM CompanyEntity company  "
			 + "Where company.name LIKE :name ",
			 	countQuery = "SELECT COUNT(company.id) FROM CompanyEntity company "
			 	+ "Where company.name LIKE :name " )
	Page<CompanyEntity> selectSearchCompaniesByPage(@Param("name") String name, Pageable pageable);

		

	@Query("SELECT MAX(company.id) "
		 + "FROM CompanyEntity company")
	long getMaxIdCompanies();
	

	@Transactional
	@Modifying
	@Query("UPDATE CompanyEntity company "
		+ "SET company.name = :#{#company.name} "
		+ "where company.id = :#{#company.id}")
	int updateCompany(@Param("company") CompanyEntity company);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO company(id, name) " 
			+ "VALUES(:#{#company.id}, :#{#company.name})",
			nativeQuery = true)
	int saveCompany(@Param("company") CompanyEntity company);
}
