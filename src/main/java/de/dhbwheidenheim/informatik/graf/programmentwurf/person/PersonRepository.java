package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> { 
	@Query("SELECT p FROM Person p WHERE p.email = ?1")
	Optional<Person> findByEmail(String email);
}
