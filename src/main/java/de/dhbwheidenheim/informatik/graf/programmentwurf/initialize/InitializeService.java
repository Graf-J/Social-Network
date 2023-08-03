package de.dhbwheidenheim.informatik.graf.programmentwurf.initialize;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.Relation;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonRepository;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationRepository;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationType;

@Service
public class InitializeService {
	private final PersonRepository personRepository;
	private final RelationService relationService;
	private final RelationRepository relationRepository;
	
	public InitializeService(
		PersonRepository personRepository, 
		RelationService relationService,
		RelationRepository relationRepository
	) {
		this.personRepository = personRepository;
		this.relationService = relationService;
		this.relationRepository = relationRepository;
	}
	
	public List<Person> createPersons() {
		List<Person> persons = new ArrayList<>();

		persons.add(new Person("Johannes", "Graf", "johannes.graf.1908@gmail.com", LocalDate.of(2000, Month.AUGUST, 19)));
		persons.add(new Person("Stefan", "Graf", "grafstefan@gmx.de", LocalDate.of(1968, Month.DECEMBER, 1)));
		persons.add(new Person("Lioba", "Graf", "graf.lili@gmx.de", LocalDate.of(1972, Month.APRIL, 22)));
		persons.add(new Person("Sophia", "Graf", "sophia.graefin@gmx.de", LocalDate.of(2003, Month.SEPTEMBER, 7)));
		persons.add(new Person("Sebastian", "Graf", "sebi.graf@outlook.com", LocalDate.of(2006, Month.DECEMBER, 12)));
		persons.add(new Person("Rosalie", "Graf", "rosi.graefin@gmx.de", LocalDate.of(2010, Month.OCTOBER, 7)));
		persons.add(new Person("Anna", "Winkler", "anna.winkler@gmx.de", LocalDate.of(2001, Month.APRIL, 27)));
		persons.add(new Person("Thomas", "Geberle", "thommy@outlook.de", LocalDate.of(2001, Month.NOVEMBER, 12)));
		persons.add(new Person("Manuel", "Reiter", "reiter03@gmail.com", LocalDate.of(1996, Month.FEBRUARY, 28)));
		persons.add(new Person("Anja", "Kaim", "kaim.anja@outlook.de", LocalDate.of(1998, Month.DECEMBER, 3)));
		persons.add(new Person("Jochen", "Manzenrieder", "jochen.manze@gmx.de", LocalDate.of(1996, Month.AUGUST, 24)));
		persons.add(new Person("Joline", "Mayr", "joline00@gmail.de", LocalDate.of(2000, Month.SEPTEMBER, 13)));
		persons.add(new Person("Maximilian", "Baum", "baummaxi@gmail.com", LocalDate.of(2001, Month.OCTOBER, 14)));
		persons.add(new Person("Verena", "Drexler", "drexler.vere@outlook.de", LocalDate.of(2000, Month.MARCH, 5)));
		persons.add(new Person("Marcus", "Link", "marcus.link@gmx.com", LocalDate.of(2001, Month.SEPTEMBER, 8)));
		persons.add(new Person("Nikolas", "Munz", "munz.niki@gmx.de", LocalDate.of(2001, Month.DECEMBER, 14)));
		persons.add(new Person("Moritz", "Bestle", "bestleretzi@gmail.de", LocalDate.of(2002, Month.APRIL, 1)));
		persons.add(new Person("Sebastian", "Domler", "domler.basti@outlook.com", LocalDate.of(2001, Month.JANUARY, 3)));
		
		personRepository.saveAll(persons);
		
		return persons;
	}
	
	public void createRelations(List<Person> persons) {
		createMarriages(persons);
		createFamilyMembers(persons);
		createFriends(persons);
	}
	
	private void createMarriages(List<Person> persons) {
		relationService.addMarriageRelation(persons.get(1), persons.get(2));
		relationService.addMarriageRelation(persons.get(10), persons.get(11));
	}
	
	private void createFamilyMembers(List<Person> persons) {
		relationService.addFamilyRelation(persons.get(0), persons.get(1));
		relationService.addFamilyRelation(persons.get(0), persons.get(3));
		relationService.addFamilyRelation(persons.get(0), persons.get(4));
		relationService.addFamilyRelation(persons.get(0), persons.get(5));
	}
	
	private void createFriends(List<Person> persons) {
		List<Relation> relations = new ArrayList<>();
		
		relations.add(new Relation(persons.get(0), persons.get(7), RelationType.friend));
		relations.add(new Relation(persons.get(0), persons.get(8), RelationType.friend));
		relations.add(new Relation(persons.get(0), persons.get(10), RelationType.friend));
		relations.add(new Relation(persons.get(0), persons.get(12), RelationType.friend));
		relations.add(new Relation(persons.get(0), persons.get(14), RelationType.friend));
		relations.add(new Relation(persons.get(0), persons.get(15), RelationType.friend));
		relations.add(new Relation(persons.get(0), persons.get(16), RelationType.friend));
		relations.add(new Relation(persons.get(7), persons.get(8), RelationType.friend));
		relations.add(new Relation(persons.get(8), persons.get(9), RelationType.friend));
		relations.add(new Relation(persons.get(8), persons.get(10), RelationType.friend));
		relations.add(new Relation(persons.get(9), persons.get(10), RelationType.friend));
		relations.add(new Relation(persons.get(10), persons.get(11), RelationType.friend));
		relations.add(new Relation(persons.get(10), persons.get(12), RelationType.friend));
		relations.add(new Relation(persons.get(12), persons.get(13), RelationType.friend));
		relations.add(new Relation(persons.get(14), persons.get(15), RelationType.friend));
		relations.add(new Relation(persons.get(14), persons.get(16), RelationType.friend));
		relations.add(new Relation(persons.get(14), persons.get(17), RelationType.friend));
		relations.add(new Relation(persons.get(15), persons.get(17), RelationType.friend));
		relations.add(new Relation(persons.get(16), persons.get(17), RelationType.friend));
		
		relationRepository.saveAll(relations);
	}
}
