package de.dhbwheidenheim.informatik.graf.programmentwurf.initialize;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.relation.RelationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

@Controller
public class InitializeController {
	private final PersonService personService;
	private final RelationService relationService;
	private final InitializeService initializeService;
	
	public InitializeController(
		PersonService personService, 
		InitializeService initializeService,
		RelationService relationService
	) {
		this.personService = personService;
		this.relationService = relationService;
		this.initializeService = initializeService;
	}
	
	@GetMapping("/initialize")
	public String initialize() {
		// Delete old Data
		relationService.deleteRelations();
		personService.deletePersons();
		
		// Insert new Data
		List<Person> persons = initializeService.createPersons();
		initializeService.createRelations(persons);
		
		return "redirect:/";
	}
}
