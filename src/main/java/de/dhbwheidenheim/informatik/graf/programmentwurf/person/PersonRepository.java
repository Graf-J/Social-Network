package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> { 
	@Query("SELECT p FROM Person p WHERE p.email = ?1")
	Optional<Person> findByEmail(String email);
	
	@Query("SELECT p FROM Person p WHERE p <> ?1")
	List<Person> findAllExcept(Person person, Pageable pageable);
	
	@Query("SELECT COUNT(p) FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'marriage' OR p = ?1)")
	Long countSinglesExcept(Person person);
	
	@Query("SELECT p FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'marriage' OR p = ?1)")
	List <Person> findSinglesExcept(Person person, Pageable pageable);
	
	@Query("SELECT COUNT(DISTINCT p) FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	Long countFamilyMembers(Person person);
	
	@Query("SELECT DISTINCT p FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	List<Person> findFamilyMembers(Person person);
	
	@Query("SELECT DISTINCT p FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	List<Person> findFamilyMembers(Person person, Pageable pageable);
	
	@Query("SELECT COUNT(p) FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1))")
	Long countNonFamilyMembers(Person person);
	
	@Query("SELECT p FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1))")
	List<Person> findNonFamilyMembers(Person person, Pageable pageable);
	
	@Query("SELECT COUNT(DISTINCT p) FROM Relation r " + 
		   "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
		   "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	Long countFriends(Person person);
	
	@Query("SELECT DISTINCT p FROM Relation r " + 
		   "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
		   "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	List<Person> findFriends(Person person, Pageable pageable);
	
	@Query("SELECT COUNT(p) FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1)) " +
	       "AND p <> ?1")
	Long countNonFriends(Person person);
	
	@Query("SELECT p FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1)) " +
	       "AND p <> ?1")
	List<Person> findNonFriends(Person person, Pageable pageable);
	
	
	
	
	
	
	
	
}
