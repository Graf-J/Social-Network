package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.Relation;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationRepository;

@Service
public class PersonService {
	private final PersonRepository personRepository;
	private final RelationRepository relationRepository;
	
	@Autowired
	public PersonService(PersonRepository personRepository, RelationRepository relationRepository) {
		this.personRepository = personRepository;
		this.relationRepository = relationRepository;
	}
	
	public Long getPersonCount() {
		return personRepository.count();
	}
	
	public Optional<Person> getPerson(Long id) {
		return personRepository.findById(id);
	}
	
	public Optional<Person> getPerson(String email) {
		return personRepository.findByEmail(email);
	}
	
	public List<Person> getPersons() {
		return personRepository.findAll();
	}
	
	public List<Person> getPersons(PageRequest pageRequest) {
		return personRepository.findAll(pageRequest).getContent();
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
	
	public void addPerson(Person person) {
		// Check if E-Mail is valid
		String regexPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (!Pattern.compile(regexPattern).matcher(person.getEmail()).matches()) {
			throw new IllegalArgumentException("Email isn't valid");
		}
		// Check if E-Mail alreay exists
		if (personRepository.findByEmail(person.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already taken");
		}
		// Check if Birthday is valid
		if (person.getBirthday().isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Birthday can't be in the future");
		}
		
		personRepository.save(person);
	}
	
	public void addPersons(List<Person> persons) {
		personRepository.saveAll(persons);
	}
	
	public void deletePersons() {
		personRepository.deleteAll();
	}
}
