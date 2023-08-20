package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.IdNotFoundException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.RedirectException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.Pagination;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.PaginationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.Post;
import de.dhbwheidenheim.informatik.graf.programmentwurf.post.PostService;


/**
 * Controller class handling operations related to persons.
 */
@Controller
public class PersonController {
	private final PersonService personService;
	private final PostService postService;
	private final PaginationService paginationService;
	
	/**
	 * Constructor for the PersonController class.
	 *
	 * Initializes the PersonController with required services for handling person and post data.
	 *
	 * @param personService The PersonService instance responsible for handling person-related operations.
	 * @param postService The PostService instance responsible for handling post-related operations.
	 * @param paginationService The PaginationService instance for managing pagination parameters.
	 */
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
	 * Retrieves a paginated view of persons and renders it to the "personsView" template.
	 *
	 * Mapped to the HTTP GET request for the root URL ("/") using the @GetMapping annotation.
	 * Fetches a paginated list of persons based on provided page and pageSize parameters.
	 * Default values are used if parameters are missing.
	 * Renders the most recent persons first.
	 *
	 * @param model Spring Model object for passing data to the view.
	 * @param page Page number for pagination. Defaults to 0 if not provided.
	 * @param pageSize Number of items per page. Defaults to 7 if not provided.
	 * @param error Error message retrieved from flash attributes.
	 * @return Name of the view template, "personsView", rendered with provided data.
	 */
	@GetMapping(path={ "/", "/persons" })
	public String showPersonsView(
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
	
	/**
	 * Retrieves the view for displaying detailed information about a specific person.
	 * 
	 * This method is mapped to the HTTP GET request for "/persons/{id}" using the @GetMapping annotation.
	 * It fetches the details of the person with the specified ID from the data source.
	 * If the person with the given ID does not exist, an IdNotFoundException is thrown.
	 * 
	 * The method also handles the pagination of family members and friends of the person.
	 * It calculates the pagination parameters for family members and friends lists.
	 * 
	 * The method retrieves the spouse, family members, friends, posts, and comment counts for the person.
	 * These details are added to the Model to be rendered in the view.
	 * 
	 * If any errors occur during the process, such as a RedirectException, an error message is set
	 * in the RedirectAttributes and the user is redirected to the appropriate page.
	 * 
	 * @param model The Spring Model object used to pass data to the view template.
	 * @param familyPage The page number for family members' pagination. Defaults to 0 if not provided in the request.
	 * @param familyPageSize The number of family members per page for pagination. Defaults to 4 if not provided in the request.
	 * @param friendPage The page number for friends' pagination. Defaults to 0 if not provided in the request.
	 * @param friendPageSize The number of friends per page for pagination. Defaults to 4 if not provided in the request.
	 * @param error The error message from a previous operation, if applicable.
	 * @param redirectAttributes The RedirectAttributes used to add error messages for redirection.
	 * @param id The ID of the person whose details are to be displayed.
	 * @return The name of the view template, "personView", which will be rendered with the provided data.
	 */
	@GetMapping("persons/{id}")
	public String showPersonView(
		Model model,
		@RequestParam(defaultValue = "0") String familyPage, 
		@RequestParam(defaultValue = "4") String familyPageSize, 
		@RequestParam(defaultValue = "0") String friendPage, 
		@RequestParam(defaultValue = "4") String friendPageSize,
		@ModelAttribute("error") String error,
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			// Get Person by ID and throw Exception if not exists
			Person person = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + id + " not found"));
			
			// Extract  Family Pagination Parameters
			Long familyMemberCount = personService.countFamilyMembers(person);
			Pagination familyPagination = paginationService.getPagination(familyPage, familyPageSize, 4, familyMemberCount);
			// Extract  Friend Pagination Parameters
			Long friendCount = personService.countFriends(person);
			Pagination friendPagination = paginationService.getPagination(friendPage, friendPageSize, 4, friendCount);
			
			// Get amount of Posts
			Long postCount = postService.countPosts(person);
			// Get amount of Comments
			Long commentCount = postService.countComments(person);
			
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
			model.addAttribute("familyMemberCount", familyMemberCount);
			model.addAttribute("familyPagination", familyPagination);
			model.addAttribute("friends", friends);
			model.addAttribute("friendCount", friendCount);
			model.addAttribute("friendPagination", friendPagination);
			model.addAttribute("posts", posts);
			model.addAttribute("postCount", postCount);
			model.addAttribute("commentCount", commentCount);
			model.addAttribute("error", error);
			
			return "personView";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
	
	/**
	 * Retrieves the view for adding a new person.
	 * 
	 * This method is mapped to the HTTP GET request for "/addPerson" using the @GetMapping annotation.
	 * It prepares a new Person object and adds it to the Model, along with any error message from a previous operation.
	 * 
	 * The provided data is used to render the "addPersonView" template, allowing users to input details for a new person.
	 * 
	 * @param model The Spring Model object used to pass data to the view template.
	 * @param error The error message from a previous operation, if applicable.
	 * @return The name of the view template, "addPersonView", which will be rendered with the provided data.
	 */
	@GetMapping("/addPerson")
	public String showAddPersonView(Model model, @ModelAttribute("error") String error) {
		Person person = new Person();
		
		// Add Attributes to Model
		model.addAttribute("person", person);
		model.addAttribute("error", error);
		
		return "addPersonView";
	}
	
	/**
	 * Adds a new person to the system.
	 * 
	 * This method is mapped to the HTTP POST request for "/persons" using the @PostMapping annotation.
	 * It receives a Person object from the form submission, adds it to the system using the personService,
	 * and then redirects the user to the main view ("/").
	 * 
	 * If any errors occur during the addition of the person, a RedirectException is caught. In this case, the error message
	 * is added as a flash attribute and the user is redirected back to the "addPerson" view to display the error.
	 * 
	 * @param person The Person object containing the data from the form submission.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @return A redirection URL based on the outcome of the addition operation.
	 */
	@PostMapping("/persons")
	public String addPerson(Person person, RedirectAttributes redirectAttributes) {
		try {
			personService.addPerson(person);
			
			return "redirect:/";
		} catch(RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
	
	/**
	 * Retrieves a paginated list of persons.
	 *
	 * This method handles the HTTP GET request for "/api/persons".
	 * It retrieves a paginated list of persons based on the provided page and pageSize query parameters.
	 * The retrieved persons are returned as a ResponseEntity containing a list of Person objects.
	 *
	 * @param page The page number for pagination (default: 0).
	 * @param pageSize The number of items per page for pagination (default: 5).
	 * @return A ResponseEntity containing a paginated list of persons.
	 */
	@GetMapping("/api/persons")
	public ResponseEntity<List<Person>> getPersons(
		@RequestParam(defaultValue = "0") String page, 
		@RequestParam(defaultValue = "5") String pageSize
	) {
		// Extract Pagination Parameters
		Long personCount = personService.countPersons();
		Pagination pagination = paginationService.getPagination(page, pageSize, 5, personCount);
		
		// Query for Persons for corresponding Page Order by createdAt in Descending Order
		List<Person> persons = personService.getPersons(pagination);
		
		return ResponseEntity.ok(persons);
	}
	
	/**
	 * Retrieves a person by their ID.
	 *
	 * This method handles the HTTP GET request for "/api/persons/{id}".
	 * It retrieves a person based on the provided ID.
	 * If the person with the specified ID does not exist, a Not Found response is returned.
	 * The retrieved person is returned as a ResponseEntity containing a Person object.
	 *
	 * @param id The ID of the person to retrieve.
	 * @return A ResponseEntity containing the retrieved person if found, or a Not Found response if the person does not exist.
	 */
	@GetMapping("/api/persons/{id}")
	public ResponseEntity<Person> getPerson(@PathVariable Long id) {
		Optional<Person> person = personService.getPerson(id);
		
		// Return not found if Person doesn't exist
		if (person.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(person.get());
	}
}
