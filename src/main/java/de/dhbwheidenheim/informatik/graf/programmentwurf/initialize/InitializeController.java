package de.dhbwheidenheim.informatik.graf.programmentwurf.initialize;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.PostService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

/**
 * Controller responsible for initializing the application with sample data.
 */
@Controller
public class InitializeController {
	private final InitializeService initializeService;
	private final PersonService personService;
	private final PostService postService;
	private final RelationService relationService;
	
	/**
     * Constructor to initialize the controller with required services.
     *
     * @param initializeService The service for initializing data.
     * @param personService The service for managing person-related operations.
     * @param postService The service for managing post-related operations.
     * @param relationService The service for managing relation-related operations.
     */
	public InitializeController(
		InitializeService initializeService,
		PersonService personService, 
		PostService postService,
		RelationService relationService
	) {
		this.initializeService = initializeService;
		this.personService = personService;
		this.postService = postService;
		this.relationService = relationService;
	}
	
	/**
     * Handles the initialization of the application with sample data.
     * Deletes old data, inserts new data, and redirects to the home page.
     *
     * @return A redirection to the home page after data initialization.
     */
	@GetMapping("/initialize")
	public String initialize() {
		// Delete old Data
		postService.deletePosts();
		relationService.deleteRelations();
		personService.deletePersons();
		
		// Insert new Data
		List<Person> persons = initializeService.createPersons();
		initializeService.createRelations(persons);
		initializeService.createPosts(persons);
		
		return "redirect:/";
	}
}
