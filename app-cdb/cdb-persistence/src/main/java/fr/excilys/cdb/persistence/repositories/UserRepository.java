package fr.excilys.cdb.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.excilys.cdb.persistence.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUsername(String username);
	
	UserEntity findByUsernameAndPassword(String username, String password);
}
