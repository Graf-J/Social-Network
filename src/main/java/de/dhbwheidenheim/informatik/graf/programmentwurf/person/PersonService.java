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

@Service
public class PersonService {
	private final PersonRepository personRepository;
	private final RelationRepository relationRepository;
	
	public PersonService(PersonRepository personRepository, RelationRepository relationRepository) {
		this.personRepository = personRepository;
		this.relationRepository = relationRepository;
	}
	
	public Long countPersons() {
		return personRepository.count();
	}
	
	public Optional<Person> getPerson(Long id) {
		return personRepository.findById(id);
	}
	
	public Optional<Person> getPerson(String email) {
		return personRepository.findByEmail(email);
	}
	
	public List<Person> getPersons(Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findAll(pageRequest).getContent();
	}
	
	public List<Person> getPersonsExcept(Person person, PageRequest pageRequest) {
		return personRepository.findAllExcept(person, pageRequest);
	}
	
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
	
	public Long countSinglesExcept(Person person) {
		return personRepository.countSinglesExcept(person);
	}
	
	public List<Person> getSinglesExcept(Person person, Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findSinglesExcept(person, pageRequest);
	}
	
	public Long countFamilyMembers(Person person) {
		return personRepository.countFamilyMembers(person);
	}
	
	public List<Person> getFamilyMembers(Person person, Pagination pagination) {
		Sort familySort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), familySort);
		
		return personRepository.findFamilyMembers(person, pageRequest);
	}
	
	public Long countNonFamilyMembers(Person person) {
		return personRepository.countNonFamilyMembers(person);
	}
	
	public List<Person> getNonFamilyMembers(Person person, Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findNonFamilyMembers(person, pageRequest);
	}
	
	public Long countFriends(Person person) {
		return personRepository.countFriends(person);
	}
	
	public List<Person> getFriends(Person person, Pagination pagination) {
		Sort friendSort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), friendSort);
		
		return personRepository.findFriends(person, pageRequest);
	}
	
	public Long countNonFriends(Person person) {
		return personRepository.countNonFriends(person);
	}
	
	public List<Person> getNonFriends(Person person, Pagination pagination) {
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getPageSize(), sort);
		
		return personRepository.findNonFriends(person, pageRequest);
	}
	
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
	
	public void deletePersons() {
		personRepository.deleteAll();
	}
}
