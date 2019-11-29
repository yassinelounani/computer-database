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

import fr.excilys.cdb.persistence.models.ComputerEntity;

@Repository
public interface ComputerRepository extends JpaRepository<ComputerEntity, Long> {

	@Query("SELECT computer, comp "
		 + "FROM ComputerEntity computer LEFT JOIN computer.company comp")
	List<ComputerEntity> selectComputers();


	@Query(value = "SELECT computer, comp "
		 + "FROM ComputerEntity computer LEFT JOIN FETCH computer.company comp",
		   countQuery = "SELECT COUNT(computer.id) FROM ComputerEntity computer" )
	Page<ComputerEntity> selectComputersWithPage(Pageable pageable);
	
	
	@Query(value = "SELECT computer, comp "
		 + "FROM ComputerEntity computer LEFT JOIN FETCH computer.company comp "
		 + "Where computer.name LIKE :name OR comp.name LIKE :name",
		 	countQuery = "SELECT COUNT(computer.id) FROM ComputerEntity computer LEFT JOIN computer.company comp "
		 	+ "Where computer.name LIKE :name OR comp.name LIKE :name" )
	Page<ComputerEntity> selectSearchComputersWithPage(@Param("name") String name, Pageable pageable);

	@Query(value = "SELECT computer, comp "
		+ "FROM ComputerEntity computer LEFT JOIN FETCH computer.company comp "
		+ "Where computer.id = :id",
			 countQuery = "SELECT COUNT(computer.id) FROM ComputerEntity computer")
	Optional<ComputerEntity> selectComputerById(@Param("id") long id);

	@Query("SELECT MAX(computer.id) "
			+ "FROM ComputerEntity computer")
	long getMaxIdComputer();

	@Transactional
	@Modifying
	@Query(value = "DELETE "
		+ "FROM computer "
		+ "Where company_id = :id", nativeQuery = true)
	int deleteComputerByCompanyId(@Param("id") long id);
	

	@Transactional
	@Modifying
	@Query("UPDATE ComputerEntity computer "
		+ "SET computer.name = :#{#computer.name}, computer.introduced = :#{#computer.introduced}, computer.discontinued = :#{#computer.discontinued}, computer.company = :#{#computer.company} "
		+ "where computer.id = :#{#computer.id}")
	int updateComputer(@Param("computer") ComputerEntity computer);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO computer(id, name, introduced, discontinued, company_id) " 
			+ "VALUES(:#{#computer.id}, :#{#computer.name}, :#{#computer.introduced}, :#{#computer.discontinued}, :#{#computer.company.id})",
				 nativeQuery = true)
	int saveComputer(@Param("computer") ComputerEntity computer);
	
}
