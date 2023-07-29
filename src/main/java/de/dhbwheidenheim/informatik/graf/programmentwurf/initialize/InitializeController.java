package de.dhbwheidenheim.informatik.graf.programmentwurf.initialize;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

@Controller
public class InitializeController {
	private final PersonService personService;
	private final InitializeService initializeService;
	
	@Autowired
	public InitializeController(PersonService personService, InitializeService initializeService) {
		this.personService = personService;
		this.initializeService = initializeService;
	}
	
	@GetMapping("/initialize")
	public String initialize() {
		List<Person> persons = initializeService.getPersons();
		personService.deletePersons();
		personService.addPersons(persons);
		
		return "redirect:/";
	}
}
