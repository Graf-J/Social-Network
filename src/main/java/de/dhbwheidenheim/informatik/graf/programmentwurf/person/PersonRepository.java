package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing person entities in the data source.
 * Extends JpaRepository to inherit common CRUD and pagination operations.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> { 
	/**
	 * Retrieves a person by their email using a custom JPQL query.
	 *
	 * @param email The email of the person to retrieve.
	 * @return An optional containing the person with the specified email if found.
	 */
	@Query("SELECT p FROM Person p WHERE p.email = ?1")
	Optional<Person> findByEmail(String email);
	
	/**
	 * Retrieves a list of persons excluding the specified person using a custom JPQL query.
	 *
	 * This method fetches all persons from the data source except the one provided as input.
	 *
	 * @param person The person to exclude from the result.
	 * @param pageable The pagination information for retrieving the list.
	 * @return A list of persons excluding the specified person.
	 */
	@Query("SELECT p FROM Person p WHERE p <> ?1")
	List<Person> findAllExcept(Person person, Pageable pageable);
	
	/**
	 * Counts the number of single persons which are over 17 years old excluding the specified person using a custom JPQL query.
	 *
	 * This method calculates the count of persons who are considered single and over 17 years old, excluding the provided person.
	 * It uses a JPQL query to find the count of persons who are not involved in any marriage relations
	 * or are not part of the provided person's relations.
	 *
	 * @param person The person to exclude from the count.
	 * @return The count of single persons excluding the specified person.
	 */
	@Query("SELECT COUNT(p) FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'marriage' OR p = ?1 OR TIMESTAMPDIFF(YEAR, p.birthday, CURRENT_DATE) < 18)")
	Long countSinglesExcept(Person person);
	
	/**
	 * Retrieves a paginated list of single persons which are over 17 years old excluding the specified person using a custom JPQL query.
	 *
	 * This method retrieves a paginated list of persons who are considered single and older than 17, excluding the provided person.
	 * It uses a JPQL query to find persons who are not involved in any marriage relations or are not part of
	 * the provided person's relations. The results are returned in the specified pagination order.
	 *
	 * @param person The person to exclude from the result list.
	 * @param pageable The pagination information for retrieving the result list.
	 * @return A paginated list of single persons excluding the specified person.
	 */
	@Query("SELECT p FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'marriage' OR p = ?1 OR TIMESTAMPDIFF(YEAR, p.birthday, CURRENT_DATE) < 18)")
	List <Person> findSinglesExcept(Person person, Pageable pageable);
	
	/**
	 * Counts the number of family members for the specified person excluding the specified person using a custom JPQL query.
	 *
	 * This method counts the number of family members for the provided person based on their involvement in family relations, excluding the provided person.
	 * It uses a JPQL query to count distinct persons who are connected to the specified person through family relations.
	 * Family relations are identified by the 'family' type, and the person can be either the creator or the receiver of the relation.
	 *
	 * @param person The person for whom family members are counted.
	 * @return The count of family members for the specified person, excluding the person itself.
	 */
	@Query("SELECT COUNT(DISTINCT p) FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	Long countFamilyMembers(Person person);
	
	/**
	 * Retrieves a list of distinct family members for the specified person excluding the specified person using a custom JPQL query.
	 *
	 * This method fetches a list of distinct persons who are considered family members of the provided person, excluding the provided person.
	 * Family members are identified by their involvement in family relations with the provided person.
	 * Both creators and receivers of 'family' type relations are included as family members.
	 *
	 * @param person The person for whom family members are retrieved.
	 * @return A list of distinct family members of the specified person.
	 */
	@Query("SELECT DISTINCT p FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	List<Person> findFamilyMembers(Person person);
	
	/**
	 * Retrieves a paginated view of distinct family members for the specified person using a custom JPQL query.
	 *
	 * This method fetches a paginated list of distinct persons who are considered family members of the provided person.
	 * Family members are identified by their involvement in family relations with the provided person.
	 * Both creators and receivers of 'family' type relations are included as family members.
	 * The specified person is excluded from the returned list.
	 *
	 * @param person The person for whom family members are retrieved.
	 * @param pageable The pagination parameters for the result.
	 * @return A paginated view of distinct family members of the specified person, excluding the person itself.
	 */
	@Query("SELECT DISTINCT p FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	List<Person> findFamilyMembers(Person person, Pageable pageable);
	
	/**
	 * Counts the number of non-family members for the specified person using a custom JPQL query.
	 *
	 * This method counts the number of persons who are not considered family members of the provided person.
	 * Non-family members are identified by their absence in family relations with the provided person.
	 * Both creators and receivers of 'family' type relations are considered family members.
	 *
	 * @param person The person for whom non-family members are counted.
	 * @return The count of non-family members for the specified person.
	 */
	@Query("SELECT COUNT(p) FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1))")
	Long countNonFamilyMembers(Person person);
	
	/**
	 * Retrieves a paginated view of non-family members for the specified person using a custom JPQL query.
	 *
	 * This method fetches a paginated list of persons who are not considered family members of the provided person.
	 * Non-family members are identified by their absence in family relations with the provided person.
	 * Both creators and receivers of 'family' type relations are considered family members.
	 *
	 * @param person The person for whom non-family members are retrieved.
	 * @param pageable The pagination information including page number, page size, and sorting.
	 * @return A paginated list of non-family members for the specified person.
	 */
	@Query("SELECT p FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'family' AND (r.creator = ?1 OR r.receiver = ?1))")
	List<Person> findNonFamilyMembers(Person person, Pageable pageable);
	
	/**
	 * Counts the number of friends for the specified person using a custom JPQL query.
	 *
	 * This method counts the number of friends for the provided person based on their involvement in friend relations.
	 * Friend relations are identified by the 'friend' type, and the person can be either the creator or the receiver of the relation.
	 * The count is based on distinct persons who are connected to the specified person through friend relations, excluding the person itself.
	 *
	 * @param person The person for whom friends are counted.
	 * @return The count of friends for the specified person, excluding the person itself.
	 */
	@Query("SELECT COUNT(DISTINCT p) FROM Relation r " + 
		   "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
		   "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	Long countFriends(Person person);
	
	/**
	 * Retrieves a paginated list of friends for the specified person using a custom JPQL query.
	 *
	 * This method fetches a paginated list of friends for the provided person based on their involvement in friend relations.
	 * Friend relations are identified by the 'friend' type, and the person can be either the creator or the receiver of the relation.
	 * The list includes distinct persons who are connected to the specified person through friend relations, excluding the person itself.
	 *
	 * @param person The person for whom friends are retrieved.
	 * @param pageable The pagination information specifying the page number, page size, and sorting.
	 * @return A paginated list of friends for the specified person, excluding the person itself.
	 */
	@Query("SELECT DISTINCT p FROM Relation r " + 
		   "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
		   "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1) AND p <> ?1")
	List<Person> findFriends(Person person, Pageable pageable);
	
	/**
	 * Counts the number of non-friends for the specified person using a custom JPQL query.
	 *
	 * This method counts the number of non-friends for the provided person based on their involvement in friend relations.
	 * Friend relations are identified by the 'friend' type, and the person can be either the creator or the receiver of the relation.
	 * The count includes distinct persons who are not connected to the specified person through friend relations, excluding the person itself.
	 *
	 * @param person The person for whom non-friends are counted.
	 * @return The count of non-friends for the specified person, excluding the person itself.
	 */
	@Query("SELECT COUNT(p) FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1)) " +
	       "AND p <> ?1")
	Long countNonFriends(Person person);
	
	/**
	 * Retrieves a paginated list of non-friends for the specified person using a custom JPQL query.
	 *
	 * This method fetches a paginated list of persons who are not considered friends of the provided person based on their involvement in friend relations.
	 * Friend relations are identified by the 'friend' type, and the person can be either the creator or the receiver of the relation.
	 * The list includes distinct persons who are not connected to the specified person through friend relations, excluding the person itself.
	 *
	 * @param person The person for whom non-friends are retrieved.
	 * @param pageable The pagination information specifying the page number, page size, and sorting criteria.
	 * @return A paginated list of non-friends for the specified person.
	 */
	@Query("SELECT p FROM Person p WHERE p.id NOT IN " +
	       "(SELECT DISTINCT p.id FROM Relation r " + 
	       "INNER JOIN Person p ON p.id = r.creator.id OR p.id = r.receiver.id " + 
	       "WHERE r.type = 'friend' AND (r.creator = ?1 OR r.receiver = ?1)) " +
	       "AND p <> ?1")
	List<Person> findNonFriends(Person person, Pageable pageable);
}
