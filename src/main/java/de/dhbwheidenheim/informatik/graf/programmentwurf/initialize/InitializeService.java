package de.dhbwheidenheim.informatik.graf.programmentwurf.initialize;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.Relation;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonRepository;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.PostRepository;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.Post;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationRepository;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationType;

/**
 * Service class responsible for initializing the application with sample data.
 */
@Service
public class InitializeService {
	private final PersonRepository personRepository;
	private final RelationService relationService;
	private final RelationRepository relationRepository;
	private final PostRepository postRepository;
	
	/**
     * Constructor to initialize the service with required repositories and services.
     *
     * @param personRepository The repository for managing person data.
     * @param relationService The service for managing relation-related operations.
     * @param relationRepository The repository for managing relation data.
     * @param postRepository The repository for managing post data.
     */
	public InitializeService(
		PersonRepository personRepository, 
		RelationService relationService,
		RelationRepository relationRepository,
		PostRepository postRepository
	) {
		this.personRepository = personRepository;
		this.relationService = relationService;
		this.relationRepository = relationRepository;
		this.postRepository = postRepository;
	}
	
	/**
     * Creates and saves sample persons.
     *
     * @return A list of created persons.
     */
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
	
	/**
     * Creates sample relations among the provided persons.
     *
     * @param persons The list of persons for whom relations are to be created.
     */
	public void createRelations(List<Person> persons) {
		createMarriages(persons);
		createFamilyMembers(persons);
		createFriends(persons);
	}
	
	/**
     * Creates and saves sample posts among the provided persons.
     *
     * @param persons The list of persons for whom posts are to be created.
     */
	public void createPosts(List<Person> persons) {
		List<Post> posts = new ArrayList<>();
		
		// Programming Language Discussion
		posts.add(new Post("Was ist euere Lieblingsprogrammiersprache?", persons.get(0)));
		posts.add(new Post("Ich programmiere am liebsten Webseiten, also JavaScript", persons.get(7), posts.get(0)));
		posts.add(new Post("Versuch mal Typescript! Damit hast du mehr Typsicherheit.", persons.get(0), posts.get(1)));
		posts.add(new Post("Danke! Ich werde es im nächsten Projekt ausprobieren.", persons.get(7), posts.get(2)));
		posts.add(new Post("Mein Lieblingssprache ist C#", persons.get(13), posts.get(0)));
		posts.add(new Post("Ich programmiere am liebsten in HTML!", persons.get(10), posts.get(0)));
		posts.add(new Post("HTML is doch keine Programmiersprache!!!", persons.get(12), posts.get(5)));
		posts.add(new Post("Das sind Markup-Sprache und keine Programmiersprache... ", persons.get(13), posts.get(5)));
		posts.add(new Post("Mein Lieblingsprogrammiersprache ist Python", persons.get(14), posts.get(0)));
		posts.add(new Post("Cool! Bist du im Data-Science und Machine-Leraning Bereich tätig?", persons.get(0), posts.get(8)));
		posts.add(new Post("Naja, ich versuche es mir gerade beizubringen =)", persons.get(14), posts.get(9)));
		posts.add(new Post("Das ist auch meine Lieblingssprache. So einfach und doch so mächtig!", persons.get(15), posts.get(8)));
		// Hello in different languages
		posts.add(new Post("Hallo", persons.get(0)));
		posts.add(new Post("Hello", persons.get(1), posts.get(12)));
		posts.add(new Post("Hola", persons.get(2), posts.get(12)));
		posts.add(new Post("Bonjour", persons.get(3), posts.get(12)));
		posts.add(new Post("Tere", persons.get(4), posts.get(12)));
		posts.add(new Post("Zdravo", persons.get(5), posts.get(12)));
		
		postRepository.saveAll(posts);
	}
	
	/**
     * Creates sample marriage relations among the provided persons.
     *
     * @param persons The list of persons for whom marriage relations are to be created.
     */
	private void createMarriages(List<Person> persons) {
		relationService.addMarriageRelation(persons.get(1), persons.get(2));
		relationService.addMarriageRelation(persons.get(10), persons.get(11));
	}
	
	/**
     * Creates sample family relations among the provided persons.
     *
     * @param persons The list of persons for whom family relations are to be created.
     */
	private void createFamilyMembers(List<Person> persons) {
		relationService.addFamilyRelation(persons.get(0), persons.get(1));
		relationService.addFamilyRelation(persons.get(0), persons.get(3));
		relationService.addFamilyRelation(persons.get(0), persons.get(4));
		relationService.addFamilyRelation(persons.get(0), persons.get(5));
	}
	
	/**
     * Creates sample friend relations among the provided persons.
     *
     * @param persons The list of persons for whom friend relations are to be created.
     */
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
