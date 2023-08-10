package de.dhbwheidenheim.informatik.graf.programmentwurf.initialize;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.PostService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

@Controller
public class InitializeController {
	private final InitializeService initializeService;
	private final PersonService personService;
	private final PostService postService;
	private final RelationService relationService;
	
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
