package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long>{
	@Query("SELECT r FROM Relation r " +
		   "WHERE r.type = 'marriage' AND r.creator = ?1 " +
		   "OR r.type = 'marriage' AND r.receiver = ?1")
	Optional<Relation> findMarriage(Person person);
	
	@Query("SELECT r FROM Relation r " +
	       "WHERE r.type = 'marriage' AND r.creator = ?1 " + 
	       "OR r.type = 'marriage' AND r.receiver = ?1 " + 
	       "OR r.type = 'marriage' AND r.creator = ?2 " + 
	       "OR r.type = 'marriage' AND r.receiver = ?2")
	Optional<Relation> findMarriage(Person creator, Person receiver);
	
	@Query("SELECT r FROM Relation r " +
	       "WHERE r.type = 'family' AND r.creator = ?1 AND r.receiver = ?2 " + 
	       "OR r.type = 'family' AND r.receiver = ?1 AND r.creator = ?2")
	Optional<Relation> findFamily(Person creator, Person receiver);
}
