package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PersonController {
	private final PersonService personService;
	
	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}
	
	/**
	 * Retrieves a paginated view of persons and renders it to the "persons" template.
	 *
	 * This method is mapped to the HTTP GET request for the root URL ("/") using the @GetMapping annotation.
	 * It fetches a paginated list of persons from the data source based on the provided page and pageSize parameters.
	 * If the parameters are not provided in the request, default values of "0" for page and "10" for pageSize are used.
	 * The persons get returned in a descending order to display the most recent persons first.
	 *
	 * @param model The Spring Model object used to pass data to the view template.
	 * @param page The page number for pagination. Defaults to 0 if not provided in the request.
	 * @param pageSize The number of items per page for pagination. Defaults to 10 if not provided in the request.
	 * @return The name of the view template, in this case, "persons", which will be rendered with the provided data.
	*/
	@GetMapping("/")
	public String getPersonsView(Model model, @RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "7") String pageSize) {
		// Extract Pagination Parameters
		Integer pageParam;
		Integer pageSizeParam;
		try {
			pageParam = Integer.parseInt(page);
			pageSizeParam = Integer.parseInt(pageSize);
		} catch(NumberFormatException ex) {
			pageParam = 0;
			pageSizeParam = 7;
		}
		
		// Calculate amount of Pages for Pagination-Rendering
		Long personCount = personService.getPersonCount();
		Integer numPages = (int)Math.ceil((double)personCount / (double)pageSizeParam);
		List<Integer> pages = new ArrayList<>();
		for (int i = 0; i < numPages; i++) {
			pages.add(i);
		}
		
		// Query for Persons for corresponding Page in descending order
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageRequest = PageRequest.of(pageParam, pageSizeParam, sort);
		List<Person> persons = personService.getPersons(pageRequest);
		
		// Add Attributes to Model
		model.addAttribute("persons", persons);
		model.addAttribute("pages", pages);
		model.addAttribute("pageSize", pageSizeParam);
		
		return "personsView";
	}
	
	@GetMapping("/all")
	public @ResponseBody List<Person> getAllUsers() {
		return personService.getPersons();
	}
	
	/**
	 * Renders the "addPersonView" template to display the form for adding a new person.
	 *
	 * This method is mapped to the HTTP GET request for the "/addPerson" URL using the @GetMapping annotation.
	 * It prepares the view by creating a new Person object and adding it, along with the "error" attribute, to the Spring Model.
	 * The "error" attribute is used to display any error messages related to the previous form submission.
	 *
	 * @param model The Spring Model object used to pass data to the view template.
	 * @param error The error message, if any, from a previous form submission. It will be displayed to the user.
	 * @return The name of the view template, in this case, "addPersonView", which will be rendered with the provided data.
	*/
	@GetMapping("/addPerson")
	public String addPersonView(Model model, @ModelAttribute("error") String error) {
		Person person = new Person();
		
		// Add Attributes to Model
		model.addAttribute("person", person);
		model.addAttribute("error", error);
		
		return "addPersonView";
	}
	
	/**
	 * Handles the HTTP POST request for adding a new person to the system.
	 *
	 * This method is mapped to the "/addPerson" URL using the @PostMapping annotation.
	 * It receives the form data as a bound object of type "Person" and attempts to add the new person to the system
	 * using the personService. If the addition is successful, the method redirects the user to the root URL ("/").
	 * If an exception occurs during the addition process, the error message from the exception is stored in flash
	 * attributes and the user is redirected back to the "addPerson" form view to display the error message.
	 *
	 * @param person The Person object representing the form data submitted by the user.
	 * @param redirectAttributes The RedirectAttributes object used to store flash attributes for the redirected view.
	 * @return A redirection string to the target view based on the outcome of the addition process.
	 *         If successful, it redirects to the root URL ("/"). If an error occurs, it redirects to the "addPerson" view.
	 */
	@PostMapping("/addPerson")
	public String addPerson(Person person, RedirectAttributes redirectAttributes) {
		try {
			personService.addPerson(person);
			
			return "redirect:/";
		} catch(Exception ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			
			return "redirect:/addPerson";
		}
	}
}
