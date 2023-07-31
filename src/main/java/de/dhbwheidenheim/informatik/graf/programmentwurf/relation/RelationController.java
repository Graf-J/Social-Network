package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;

@Controller
public class RelationController {
	private final RelationService relationService;
	private final PersonService personService;
	
	@Autowired
	public RelationController(RelationService relationService, PersonService personService) {
		this.relationService = relationService;
		this.personService = personService;
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
		Person person = new Person();
		
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
		
		// Check if Person with Id of the URL exists
		Optional<Person> queryPerson = personService.getPerson(id);
		if (queryPerson.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Person with Id " + id + " not found");
			return "redirect:/";
		}
		
		// Calculate amount of Pages for Pagination-Rendering
		Long personCount = personService.countSinglesExcept(queryPerson.get());
		Integer numPages = (int)Math.ceil((double)personCount / (double)pageSizeParam);
		List<Integer> pages = new ArrayList<>();
		for (int i = 0; i < numPages; i++) {
			pages.add(i);
		}
		
		// Query for Persons for corresponding Page ordered by the First Name
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pageParam, pageSizeParam, sort);
		List<Person> persons = personService.getSinglesExcept(queryPerson.get(), pageRequest);
		
		// Add Attributes to Model
		model.addAttribute("id", id);
		model.addAttribute("person", person);
		model.addAttribute("persons", persons);
		model.addAttribute("pages", pages);
		model.addAttribute("pageSize", pageSizeParam);
		model.addAttribute("error", error);
		
		return "addMarriageView";
	}
	
	@PostMapping("/addMarriage/{id}")
	public String addMarriage(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		// Check if Person with Id exists
		Optional<Person> creatorPerson = personService.getPerson(id);
		
		if (creatorPerson.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Person with Id " + id + " not found");
			return "redirect:/";
		}
		// Check if Creator Person exists
		Optional<Person> receiverPerson = personService.getPerson(person.getEmail());
		
		if (receiverPerson.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Person with email " + person.getEmail() + " not found");
			return "redirect:/addMarriage/" + id; 
		}

		// Create Marriage
		try {
        	relationService.addMarriageRelation(creatorPerson.get(), receiverPerson.get());
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			
			return "redirect:/addMarriage/" + id;
		}
		
		return "redirect:/person/" + id;
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
		Person person = new Person();
		
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
		
		// Check if Person with Id of the URL exists
		Optional<Person> queryPerson = personService.getPerson(id);
		if (queryPerson.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Person with Id " + id + " not found");
			return "redirect:/";
		}
		
		// Calculate amount of Pages for Pagination-Rendering
		Long personCount = personService.countNonFamilyMembers(queryPerson.get());
		Integer numPages = (int)Math.ceil((double)personCount / (double)pageSizeParam);
		List<Integer> pages = new ArrayList<>();
		for (int i = 0; i < numPages; i++) {
			pages.add(i);
		}
		
		// Query for Persons for corresponding Page ordered by the First Name
		Sort sort = Sort.by(Sort.Direction.ASC, "firstName");
		PageRequest pageRequest = PageRequest.of(pageParam, pageSizeParam, sort);
		List<Person> persons = personService.getNonFamilyMembers(queryPerson.get(), pageRequest);
		
		// Add Attributes to Model
		model.addAttribute("id", id);
		model.addAttribute("person", person);
		model.addAttribute("persons", persons);
		model.addAttribute("pages", pages);
		model.addAttribute("pageSize", pageSizeParam);
		model.addAttribute("error", error);
		
		return "addFamilyMemberView";
	}
	
	@PostMapping("/addFamilyMember/{id}")
	public String addFamily(
		Person person, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long id
	) {
		// Check if Person with Id exists
		Optional<Person> creatorPerson = personService.getPerson(id);
		
		if (creatorPerson.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Person with Id " + id + " not found");
			return "redirect:/";
		}
		// Check if Creator Person exists
		Optional<Person> receiverPerson = personService.getPerson(person.getEmail());
		
		if (receiverPerson.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Person with email " + person.getEmail() + " not found");
			return "redirect:/addFamilyMember/" + id; 
		}

		// Create Family Member
		try {
        	relationService.addFamilyRelation(creatorPerson.get(), receiverPerson.get());
		} catch (IllegalArgumentException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			
			return "redirect:/addMarriage/" + id;
		}
		
		return "redirect:/person/" + id;
	}
}
