package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.EmailNotFoundException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.IdNotFoundException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.InvalidFormInputException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.RedirectException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.Pagination;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.PaginationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;

/**
 * Controller class handling operations related to relations.
 */
@Controller
public class RelationController {
	private final RelationService relationService;
	private final PersonService personService;
	private final PaginationService paginationService;
	
	/**
     * Constructor for the RelationController class.
     *
     * Initializes the RelationController with required services for handling relation, person, and pagination data.
     *
     * @param relationService The RelationService instance responsible for handling relation-related operations.
     * @param personService The PersonService instance responsible for handling person-related operations.
     * @param paginationService The PaginationService instance for managing pagination parameters.
     */
	public RelationController(
		RelationService relationService, 
		PersonService personService,
		PaginationService paginationService
	) {
		this.relationService = relationService;
		this.personService = personService;
		this.paginationService = paginationService;
	}
	
	/**
	 * Retrieves the view for adding a marriage relation.
	 *
	 * This method handles the HTTP GET request for "/addMarriage/{id}".
	 * It prepares data for adding a marriage relation between a specified person and eligible singles.
	 * Eligible singles are paginated and sorted by first name.
	 *
	 * @param model Spring Model object for passing data to the view.
	 * @param redirectAttributes RedirectAttributes for adding error messages.
	 * @param page Page number for pagination. Defaults to 0 if not provided.
	 * @param pageSize Number of items per page for pagination. Defaults to 7 if not provided.
	 * @param error Error message from a previous operation, if applicable.
	 * @param id ID of the person for whom a marriage relation is being added.
	 * @return The name of the view template, "addMarriageView", rendered with provided data.
	 */
	@GetMapping("/addMarriage/{id}")
	public String getMarriageView(
		Model model,
		RedirectAttributes redirectAttributes,
		@RequestParam(defaultValue = "0") String page, 
		@RequestParam(defaultValue = "7") String pageSize, 
		@ModelAttribute("error") String error,
		@PathVariable Long id
	) {
		try {
			Person person = new Person();
			
			// Get Person by ID and throw Exception if not exists
			Person queryPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + id + " not found"));
			
			// Get the Pagination Information
			Long singlesCount = personService.countSinglesExcept(queryPerson);
			Pagination pagination = paginationService.getPagination(page, pageSize, 7, singlesCount);
			
			// Query for Persons who are not married including Pagination and Sorting by First Name
			List<Person> singles = personService.getSinglesExcept(queryPerson, pagination);
			
			// Add Attributes to Model
			model.addAttribute("person", person);
			model.addAttribute("queryPerson", queryPerson);
			model.addAttribute("persons", singles);
			model.addAttribute("pagination", pagination);
			model.addAttribute("error", error);
			
			return "addMarriageView";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
	
	/**
	 * Adds a new marriage relation between the specified person and the selected spouse.
	 *
	 * This method is mapped to the HTTP POST request for "/addMarriage/{id}" using the @PostMapping annotation.
	 * It receives the details of the spouse from the form submission and creates a marriage relation between the creator
	 * person (specified by the path variable) and the selected spouse. The creator person's ID is used to retrieve their details.
	 *
	 * If any errors occur during the addition of the marriage relation, such as invalid input or exceptions,
	 * a RedirectException is caught. In this case, an error message is added as a flash attribute and the user is redirected
	 * back to the appropriate view to display the error.
	 *
	 * @param person The Person object containing the spouse's email from the form submission.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @param id The ID of the creator person for the marriage relation.
	 * @return A redirection URL based on the outcome of the addition operation.
	 */
	@PostMapping("/addMarriage/{id}")
	public String addMarriage(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			// Get Person by ID and throw Exception if not exists
			Person creatorPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + id + " not found"));
			
			// Check if Email is specified
			if (person.getEmail() == null || person.getEmail().isEmpty()) {
				throw new InvalidFormInputException("/addMarriage/" + id, "Email has to be specified");
			}
			
			// Get Person by Email and throw Exception if not exists
			Person receiverPerson = personService.getPerson(person.getEmail())
				.orElseThrow(() -> new EmailNotFoundException("/addMarriage/" + id, "Person with email " + person.getEmail() + " not found"));
	
			// Add Relation
	        relationService.addMarriageRelation(creatorPerson, receiverPerson);
			
			return "redirect:/person/" + id;
		}  catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath(); 
		}
	}
	
	/**
	 * Retrieves a paginated view of persons who can be added as family members and renders it to the "addFamilyMemberView" template.
	 *
	 * This method is mapped to the HTTP GET request for "/addFamilyMember/{id}" using the @GetMapping annotation.
	 * Fetches a paginated list of persons who are not family members of the person with the provided ID.
	 * Default pagination values are used if parameters are missing.
	 * 
	 * @param model Spring Model object for passing data to the view.
	 * @param page Page number for pagination. Defaults to 0 if not provided.
	 * @param pageSize Number of items per page. Defaults to 7 if not provided.
	 * @param error Error message retrieved from flash attributes.
	 * @param id The ID of the person for whom family members are being added.
	 * @return Name of the view template, "addFamilyMemberView", rendered with provided data.
	 */
	@GetMapping("/addFamilyMember/{id}")
	public String getFamilyView(
		Model model,
		RedirectAttributes redirectAttributes,
		@RequestParam(defaultValue = "0") String page, 
		@RequestParam(defaultValue = "7") String pageSize, 
		@ModelAttribute("error") String error,
		@PathVariable Long id
	) {
		try {
			Person person = new Person();
			
			// Get Person by ID and throw Exception if not exists
			Person queryPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + id + " not found"));
			
			// Get the Pagination Information
			Long familyMembersCount = personService.countNonFamilyMembers(queryPerson);
			Pagination pagination = paginationService.getPagination(page, pageSize, 7, familyMembersCount);
			
			// Query for the Family Members including Pagination and Sorting by First Name
			List<Person> persons = personService.getNonFamilyMembers(queryPerson, pagination);
			
			// Add Attributes to Model
			model.addAttribute("person", person);
			model.addAttribute("queryPerson", queryPerson);
			model.addAttribute("persons", persons);
			model.addAttribute("pagination", pagination);
			model.addAttribute("error", error);
			
			return "addFamilyMemberView";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
	
	/**
	 * Adds a new family member to the system for a specific person.
	 *
	 * This method is mapped to the HTTP POST request for "/addFamilyMember/{id}" using the @PostMapping annotation.
	 * It receives a Person object from the form submission, identifies the creator person by their ID,
	 * validates the form input, and then adds a family relation between the creator and receiver persons.
	 * 
	 * If any errors occur during the addition of the family member, a RedirectException is caught.
	 * The error message is added as a flash attribute, and the user is redirected back to the "addFamilyMember" view to display the error.
	 * 
	 * @param person The Person object containing the data from the form submission.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @param id The ID of the person for whom a new family member is being added.
	 * @return A redirection URL based on the outcome of the addition operation.
	 */
	@PostMapping("/addFamilyMember/{id}")
	public String addFamily(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			// Get Person by ID and throw Exception if not exists
			Person creatorPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + id + " not found"));
			
			// Check if Email is specified
			if (person.getEmail() == null || person.getEmail().isEmpty()) {
				throw new InvalidFormInputException("/addFamilyMember/" + id, "Email has to be specified");
			}
			
			// Get Person by Email and throw Exception if not exists
			Person receiverPerson = personService.getPerson(person.getEmail())
				.orElseThrow(() -> new EmailNotFoundException("/addFamilyMember/" + id, "Person with email " + person.getEmail() + " not found"));
	
			// Add Relation
	        relationService.addFamilyRelation(creatorPerson, receiverPerson);
			
			return "redirect:/person/" + id;
		}  catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath(); 
		}
	}
	
	/**
	 * Retrieves a paginated view of persons who can be added as friends and renders it to the "addFriendView" template.
	 *
	 * This method is mapped to the HTTP GET request for "/addFriend/{id}" using the @GetMapping annotation.
	 * Fetches a paginated list of persons who are not friends of the person with the provided ID.
	 * Default pagination values are used if parameters are missing.
	 * 
	 * @param model Spring Model object for passing data to the view.
	 * @param redirectAttributes RedirectAttributes object for adding flash attributes.
	 * @param page Page number for pagination. Defaults to 0 if not provided.
	 * @param pageSize Number of items per page. Defaults to 7 if not provided.
	 * @param error Error message retrieved from flash attributes.
	 * @param id The ID of the person for whom friends are being added.
	 * @return Name of the view template, "addFriendView", rendered with provided data.
	 */
	@GetMapping("/addFriend/{id}")
	public String getFriendView(
		Model model,
		RedirectAttributes redirectAttributes,
		@RequestParam(defaultValue = "0") String page, 
		@RequestParam(defaultValue = "7") String pageSize, 
		@ModelAttribute("error") String error,
		@PathVariable Long id
	) {
		try {
			Person person = new Person();
			
			// Get Person by ID and throw Exception if not exists
			Person queryPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + id + " not found"));
						
			// Get the Pagination Information
			Long friendsCount = personService.countNonFriends(queryPerson);
			Pagination pagination = paginationService.getPagination(page, pageSize, 7, friendsCount);
			
			// Query for Friends including Pagination and Sorting by First Name
			List<Person> persons = personService.getNonFriends(queryPerson, pagination);
			
			// Add Attributes to Model
			model.addAttribute("person", person);
			model.addAttribute("queryPerson", queryPerson);
			model.addAttribute("persons", persons);
			model.addAttribute("pagination", pagination);
			model.addAttribute("error", error);
			
			return "addFriendView";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
	
	/**
	 * Adds a new friend relation between persons.
	 * 
	 * This method is mapped to the HTTP POST request for "/addFriend/{id}" using the @PostMapping annotation.
	 * It receives a Person object from the form submission, identifies the creator person by ID, and the receiver person
	 * by email. It then adds a friend relation between the creator and receiver persons using the relationService.
	 * 
	 * If any errors occur during this process, such as an invalid email or a RedirectException, an error message is added as a flash attribute,
	 * and the user is redirected back to the appropriate view to display the error.
	 * 
	 * @param person The Person object containing the receiver's email from the form submission.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @param id The ID of the person for whom the friend relation is being added.
	 * @return A redirection URL based on the outcome of the friend relation addition operation.
	 */
	@PostMapping("/addFriend/{id}")
	public String addFriend(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			// Get Person by ID and throw Exception if not exists
			Person creatorPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + id + " not found"));
			
			// Check if Email is specified
			if (person.getEmail() == null || person.getEmail().isEmpty()) {
				throw new InvalidFormInputException("/addFriend/" + id, "Email has to be specified");
			}
			
			// Get Person by Email and throw Exception if not exists
			Person receiverPerson = personService.getPerson(person.getEmail())
				.orElseThrow(() -> new EmailNotFoundException("/addFriend/" + id, "Person with email " + person.getEmail() + " not found"));
			
			// Add Relation
	        relationService.addFriendRelation(creatorPerson, receiverPerson);
			
			return "redirect:/person/" + id;
		}  catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath(); 
		}
	}
}
