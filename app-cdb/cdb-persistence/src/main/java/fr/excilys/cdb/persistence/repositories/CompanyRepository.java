package fr.excilys.cdb.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
}
