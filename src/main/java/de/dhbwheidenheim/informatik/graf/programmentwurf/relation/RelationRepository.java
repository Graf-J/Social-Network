package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

/**
 * Repository interface for managing relation entities in the data source.
 * Extends JpaRepository to inherit common CRUD and pagination operations.
 */
@Repository
public interface RelationRepository extends JpaRepository<Relation, Long>{
	/**
	 * Finds a marriage relation involving the specified person.
	 *
	 * This method queries the relation repository to find a marriage relation involving the provided person.
	 * It searches for relations where the provided person is either the creator or the receiver of the relation,
	 * and the relation type is 'marriage'.
	 *
	 * @param person The person for whom the marriage relation is being searched.
	 * @return An Optional containing the marriage relation if found, otherwise an empty Optional.
	 */
	@Query("SELECT r FROM Relation r " +
		   "WHERE r.type = 'marriage' AND r.creator = ?1 " +
		   "OR r.type = 'marriage' AND r.receiver = ?1")
	Optional<Relation> findMarriage(Person person);
	
	/**
	 * Finds a marriage relation involving the specified creator and receiver persons.
	 *
	 * This method queries the relation repository to find a marriage relation involving the provided creator and receiver persons.
	 * It searches for relations where either the creator or the receiver matches the specified persons, and the relation type is 'marriage'.
	 *
	 * @param creator The creator person of the marriage relation.
	 * @param receiver The receiver person of the marriage relation.
	 * @return An Optional containing the marriage relation if found, otherwise an empty Optional.
	 */
	@Query("SELECT r FROM Relation r " +
	       "WHERE r.type = 'marriage' AND r.creator = ?1 " + 
	       "OR r.type = 'marriage' AND r.receiver = ?1 " + 
	       "OR r.type = 'marriage' AND r.creator = ?2 " + 
	       "OR r.type = 'marriage' AND r.receiver = ?2")
	Optional<Relation> findMarriage(Person creator, Person receiver);
	
	/**
	 * Finds a family relation involving the specified creator and receiver persons.
	 *
	 * This method queries the relation repository to find a family relation involving the provided creator and receiver persons.
	 * It searches for relations where either the creator and receiver match the specified persons or the receiver and creator match,
	 * and the relation type is 'family'.
	 *
	 * @param creator The creator person of the family relation.
	 * @param receiver The receiver person of the family relation.
	 * @return An Optional containing the family relation if found, otherwise an empty Optional.
	 */
	@Query("SELECT r FROM Relation r " +
	       "WHERE r.type = 'family' AND r.creator = ?1 AND r.receiver = ?2 " + 
	       "OR r.type = 'family' AND r.receiver = ?1 AND r.creator = ?2")
	Optional<Relation> findFamily(Person creator, Person receiver);
	
	/**
	 * Finds a friend relation involving the specified creator and receiver persons.
	 *
	 * This method queries the relation repository to find a friend relation involving the provided creator and receiver persons.
	 * It searches for relations where either the creator and receiver match the specified persons or the receiver and creator match,
	 * and the relation type is 'friend'.
	 *
	 * @param creator The creator person of the friend relation.
	 * @param receiver The receiver person of the friend relation.
	 * @return An Optional containing the friend relation if found, otherwise an empty Optional.
	 */
	@Query("SELECT r FROM Relation r " +
	       "WHERE r.type = 'friend' AND r.creator = ?1 AND r.receiver = ?2 " + 
	       "OR r.type = 'friend' AND r.receiver = ?1 AND r.creator = ?2")
	Optional<Relation> findFriend(Person creator, Person receiver);
}
