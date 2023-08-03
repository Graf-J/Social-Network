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
		relationService.deleteRelations();
		
		personService.deletePersons();
		List<Person> persons = initializeService.getPersons();
		personService.addPersons(persons);
		
		return "redirect:/";
	}
}
