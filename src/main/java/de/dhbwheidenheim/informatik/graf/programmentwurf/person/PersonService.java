package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.InvalidFormInputException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.Pagination;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.Relation;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationRepository;

/**
 * Service class responsible for handling operations related to persons.
 */
@Service
public class PersonService {
	private final PersonRepository personRepository;
	private final RelationRepository relationRepository;
	
	/**
     * Constructs a new instance of the PersonService class.
     *
     * @param personRepository The repository for managing persons.
     * @param relationRepository The repository for managing relations.
     */
	public PersonService(PersonRepository personRepository, RelationRepository relationRepository) {
		this.personRepository = personRepository;
		this.relationRepository = relationRepository;
	}
	
	/**
     * Count the total number of persons in the repository.
     *
     * @return The count of persons.
     */
	public Long countPersons() {
		return personRepository.count();
	}
	
	/**
     * Retrieve a person by their ID.
     *
     * @param id The ID of the person to retrieve.
     * @return An Optional containing the person if found, or empty if not.
     */
	public Optional<Person> getPerson(Long id) {
		return personRepository.findById(id);
	}
	
	/**
     * Retrieve a person by their email.
     *
     * @param email The email of the person to retrieve.
     * @return An Optional containing the person if found, or empty if not.
     */
	public Optional<Person> getPerson(String email) {
		return personRepository.findByEmail(email);
	}
	
	/**
     * Retrieve a paginated list of persons.
     *
     * @param pagination The pagination details.
     * @return A list of persons for the given page.
     */
	public List<Person> getPersons(Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findAll(pageRequest).getContent();
	}
	
	/**
	 * Retrieve a paginated list of persons except the specified person.
	 *
	 * @param person The person to exclude from the results.
	 * @param pageRequest The pagination details.
	 * @return A list of persons for the given page excluding the specified person.
	 */
	public List<Person> getPersonsExcept(Person person, PageRequest pageRequest) {
		return personRepository.findAllExcept(person, pageRequest);
	}
	
	/**
	 * Retrieve the spouse of the given person.
	 *
	 * @param person The person for whom to find the spouse.
	 * @return An optional containing the spouse of the person if found, otherwise empty.
	 */
	public Optional<Person> getSpouse(Person person) {
		Optional<Relation> relation = relationRepository.findMarriage(person);
		
		if (relation.isEmpty()) {
			return Optional.empty();
		}
		
		if (relation.get().getCreator().getId() == person.getId()) {
			return Optional.of(relation.get().getReceiver());
		} else {
			return Optional.of(relation.get().getCreator());
		}
	}
	
	/**
	 * Count the number of single persons excluding the specified person.
	 *
	 * @param person The person to exclude from the count.
	 * @return The count of single persons excluding the specified person.
	 */
	public Long countSinglesExcept(Person person) {
		return personRepository.countSinglesExcept(person);
	}
	
	/**
	 * Retrieve a paginated list of single persons excluding the specified person.
	 *
	 * @param person The person to exclude from the list.
	 * @param pagination The pagination parameters specifying the page and page size.
	 * @return A paginated list of single persons excluding the specified person.
	 */
	public List<Person> getSinglesExcept(Person person, Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findSinglesExcept(person, pageRequest);
	}
	
	/**
	 * Count the number of family members associated with the given person.
	 *
	 * @param person The person for whom family members are being counted.
	 * @return The count of family members associated with the given person.
	 */
	public Long countFamilyMembers(Person person) {
		return personRepository.countFamilyMembers(person);
	}
	
	/**
	 * Retrieve a paginated list of family members associated with the given person.
	 *
	 * @param person The person for whom family members are being retrieved.
	 * @param pagination The pagination information to control the result size and page number.
	 * @return A paginated list of family members associated with the given person.
	 */
	public List<Person> getFamilyMembers(Person person, Pagination pagination) {
		Sort familySort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), familySort);
		
		return personRepository.findFamilyMembers(person, pageRequest);
	}
	
	/**
	 * Count the number of non-family members associated with the given person.
	 *
	 * @param person The person for whom non-family members are being counted.
	 * @return The count of non-family members associated with the given person.
	 */
	public Long countNonFamilyMembers(Person person) {
		return personRepository.countNonFamilyMembers(person);
	}
	
	/**
	 * Retrieve a list of non-family members associated with the given person, paginated according to the provided pagination settings.
	 *
	 * @param person The person for whom non-family members are being retrieved.
	 * @param pagination The pagination settings to be applied to the query.
	 * @return A list of non-family members associated with the given person, paginated as per the provided settings.
	 */
	public List<Person> getNonFamilyMembers(Person person, Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findNonFamilyMembers(person, pageRequest);
	}
	
	/**
	 * Count the number of friends associated with the given person.
	 *
	 * @param person The person for whom the count of friends is to be determined.
	 * @return The count of friends associated with the given person.
	 */
	public Long countFriends(Person person) {
		return personRepository.countFriends(person);
	}
	
	/**
	 * Retrieve a paginated list of friends associated with the given person.
	 *
	 * @param person The person for whom the list of friends is to be retrieved.
	 * @param pagination The pagination parameters to determine the page and page size of the result.
	 * @return A paginated list of friends associated with the given person.
	 */
	public List<Person> getFriends(Person person, Pagination pagination) {
		Sort friendSort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), friendSort);
		
		return personRepository.findFriends(person, pageRequest);
	}
	
	/**
	 * Count the number of persons who are not friends of the given person.
	 *
	 * @param person The person for whom the count of non-friends is to be calculated.
	 * @return The count of persons who are not friends of the given person.
	 */
	public Long countNonFriends(Person person) {
		return personRepository.countNonFriends(person);
	}
	
	/**
	 * Retrieve a paginated list of persons who are not friends of the given person.
	 *
	 * @param person The person for whom the list of non-friends is to be retrieved.
	 * @param pagination The pagination settings for retrieving the list.
	 * @return A paginated list of persons who are not friends of the given person.
	 */
	public List<Person> getNonFriends(Person person, Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findNonFriends(person, pageRequest);
	}
	
	/**
	 * Adds a new person to the system while ensuring the validity of input data.
	 * The provided person object is validated for mandatory fields, valid email format,
	 * and a non-future birthday. If validation succeeds, the person is saved to the repository.
	 *
	 * @param person The person object containing information to be added.
	 * @throws InvalidFormInputException If the person's input data is not valid.
	 */
	public void addPerson(Person person) {
		// Check Form-Input Data
		if (person.getFirstName() == null || person.getFirstName().isEmpty()) {
			throw new InvalidFormInputException("/addPerson", "FirstName has to be specified");
		}
		if (person.getLastName() == null || person.getLastName().isEmpty()) {
			throw new InvalidFormInputException("/addPerson", "LastName has to be specified");
		}
		if (person.getEmail() == null || person.getEmail().isEmpty()) {
			throw new InvalidFormInputException("/addPerson", "Email has to be specified");
		}
		if (person.getBirthday() == null) {
			throw new InvalidFormInputException("/addPerson", "Birthday has to be specified");
		}
		
		// Check if E-Mail is valid
		String regexPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (!Pattern.compile(regexPattern).matcher(person.getEmail()).matches()) {
			throw new InvalidFormInputException("/addPerson", "Email isn't valid");
		}
		// Check if E-Mail alreay exists
		if (personRepository.findByEmail(person.getEmail()).isPresent()) {
			throw new InvalidFormInputException("/addPerson", "Email already taken");
		}
		// Check if Birthday is valid
		if (person.getBirthday().isAfter(LocalDate.now())) {
			throw new InvalidFormInputException("/addPerson", "Birthday can't be in the future");
		}
		
		personRepository.save(person);
	}
	
	/**
	 * Deletes all person records.
	 */
	public void deletePersons() {
		personRepository.deleteAll();
	}
}
