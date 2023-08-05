package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.IdNotFoundException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.Pagination;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.PaginationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.Post;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.PostService;


@Controller
public class PersonController {
	private final PersonService personService;
	private final PostService postService;
	private final PaginationService paginationService;
	
	public PersonController(
		PersonService personService, 
		PostService postService,
		PaginationService paginationService
	) {
		this.personService = personService;
		this.postService = postService;
		this.paginationService = paginationService;
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
	public String getPersonsView(
		Model model, 
		@RequestParam(defaultValue = "0") String page, 
		@RequestParam(defaultValue = "7") String pageSize, 
		@ModelAttribute("error") String error
	) {
		// Extract Pagination Parameters
		Long personCount = personService.countPersons();
		Pagination pagination = paginationService.getPagination(page, pageSize, 7, personCount);
		
		// Query for Persons for corresponding Page Order by createdAt in Descending Order
		List<Person> persons = personService.getPersons(pagination);
		
		// Add Attributes to Model
		model.addAttribute("persons", persons);
		model.addAttribute("error", error);
		model.addAttribute("pagination", pagination);
		
		return "personsView";
	}
	
	// @GetMapping("/all")
	// public @ResponseBody List<Person> getAllUsers() {
	// 	return personService.getPersons();
	// }
	
	@GetMapping("person/{id}")
	public String getPersonView(
		Model model,
		@RequestParam(defaultValue = "0") String familyPage, 
		@RequestParam(defaultValue = "4") String familyPageSize, 
		@RequestParam(defaultValue = "0") String friendPage, 
		@RequestParam(defaultValue = "4") String friendPageSize, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			// Get Person by ID and throw Exception if not exists
			Person person = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
			
			// Extract  Family Pagination Parameters
			Long familyMemberCount = personService.countFamilyMembers(person);
			Pagination familyPagination = paginationService.getPagination(familyPage, familyPageSize, 4, familyMemberCount);
			// Extract  Friend Pagination Parameters
			Long friendCount = personService.countFriends(person);
			Pagination friendPagination = paginationService.getPagination(friendPage, friendPageSize, 4, friendCount);
			
			// Get the husband / wife of the Person
			Optional<Person> spouse = personService.getSpouse(person);
			// Query for the Family Members Page Ordered By CreatedAt Descending
			List<Person> familyMembers = personService.getFamilyMembers(person, familyPagination);
			// Query for the Friends Page Ordered By CreatedAt Descending
			List<Person> friends = personService.getFriends(person, friendPagination);
			// Query for the Posts of the Person
			List<Post> posts = postService.getPostsByCreator(person);
					
			// Add Attributes to Model
			model.addAttribute("person", person);
			model.addAttribute("spouse", spouse);
			model.addAttribute("familyMembers", familyMembers);
			model.addAttribute("familyPagination", familyPagination);
			model.addAttribute("friends", friends);
			model.addAttribute("friendPagination", friendPagination);
			model.addAttribute("posts", posts);
			
			return "personView";
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		}
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
		} catch(IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/addPerson";
		}
	}
}
