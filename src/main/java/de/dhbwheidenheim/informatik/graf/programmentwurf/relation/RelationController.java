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
import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.RedirectException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.Pagination;
import de.dhbwheidenheim.informatik.graf.programmentwurf.pagination.PaginationService;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;

@Controller
public class RelationController {
	private final RelationService relationService;
	private final PersonService personService;
	private final PaginationService paginationService;
	
	public RelationController(
		RelationService relationService, 
		PersonService personService,
		PaginationService paginationService
	) {
		this.relationService = relationService;
		this.personService = personService;
		this.paginationService = paginationService;
	}
	
	
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
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
			
			// Get the Pagination Information
			Long singlesCount = personService.countSinglesExcept(queryPerson);
			Pagination pagination = paginationService.getPagination(page, pageSize, 7, singlesCount);
			
			// Query for Persons who are not married including Pagination and Sorting by First Name
			List<Person> singles = personService.getSinglesExcept(queryPerson, pagination);
			
			// Add Attributes to Model
			model.addAttribute("id", id);
			model.addAttribute("person", person);
			model.addAttribute("persons", singles);
			model.addAttribute("pagination", pagination);
			model.addAttribute("error", error);
			
			return "addMarriageView";
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		}
	}
	
	@PostMapping("/addMarriage/{id}")
	public String addMarriage(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			Person creatorPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
			
			Person receiverPerson = personService.getPerson(person.getEmail())
				.orElseThrow(() -> new EmailNotFoundException(id, "Person with email " + person.getEmail() + " not found"));
	
	        relationService.addMarriageRelation(creatorPerson, receiverPerson);
			
			return "redirect:/person/" + id;
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/addMarriage/" + ex.getRedirectId(); 
		}
	}
	
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
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
			
			// Get the Pagination Information
			Long familyMembersCount = personService.countNonFamilyMembers(queryPerson);
			Pagination pagination = paginationService.getPagination(page, pageSize, 7, familyMembersCount);
			
			// Query for the Family Members including Pagination and Sorting by First Name
			List<Person> persons = personService.getNonFamilyMembers(queryPerson, pagination);
			
			// Add Attributes to Model
			model.addAttribute("id", id);
			model.addAttribute("person", person);
			model.addAttribute("persons", persons);
			model.addAttribute("pagination", pagination);
			model.addAttribute("error", error);
			
			return "addFamilyMemberView";
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		}
	}
	
	@PostMapping("/addFamilyMember/{id}")
	public String addFamily(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			Person creatorPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
			
			Person receiverPerson = personService.getPerson(person.getEmail())
				.orElseThrow(() -> new EmailNotFoundException(id, "Person with email " + person.getEmail() + " not found"));
	
	        relationService.addFamilyRelation(creatorPerson, receiverPerson);
			
			return "redirect:/person/" + id;
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/addFamilyMember/" + ex.getRedirectId(); 
		}
	}
	
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
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
						
			// Get the Pagination Information
			Long friendsCount = personService.countNonFriends(queryPerson);
			Pagination pagination = paginationService.getPagination(page, pageSize, 7, friendsCount);
			
			// Query for Friends including Pagination and Sorting by First Name
			List<Person> persons = personService.getNonFriends(queryPerson, pagination);
			
			// Add Attributes to Model
			model.addAttribute("id", id);
			model.addAttribute("person", person);
			model.addAttribute("persons", persons);
			model.addAttribute("pagination", pagination);
			model.addAttribute("error", error);
			
			return "addFriendView";
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		}
	}
	
	@PostMapping("/addFriend/{id}")
	public String addFriend(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		try {
			Person creatorPerson = personService.getPerson(id)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
			
			Person receiverPerson = personService.getPerson(person.getEmail())
				.orElseThrow(() -> new EmailNotFoundException(id, "Person with email " + person.getEmail() + " not found"));
	
	        relationService.addFriendRelation(creatorPerson, receiverPerson);
			
			return "redirect:/person/" + id;
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/addFriend/" + ex.getRedirectId(); 
		}
	}
}
