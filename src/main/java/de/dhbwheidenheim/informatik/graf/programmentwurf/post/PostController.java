package de.dhbwheidenheim.informatik.graf.programmentwurf.post;

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
 * Controller class handling operations related to posts.
 */
@Controller
public class PostController {
	private final PostService postService;
	private final PersonService personService;
	private final PaginationService paginationService;
	
	/**
     * Constructor for the PostController class.
     *
     * Initializes the PostController with required services for handling post and person data.
     *
     * @param postService The PostService instance responsible for handling post-related operations.
     * @param personService The PersonService instance responsible for handling person-related operations.
     * @param paginationService The PaginationService instance for managing pagination parameters.
     */
	public PostController(
		PostService postService, 
		PersonService personService,
		PaginationService paginationService
	) {
		this.postService = postService;
		this.personService = personService;
		this.paginationService = paginationService;
	}
	
	/**
	 * Retrieves the view for adding a new post.
	 * 
	 * This method is mapped to the HTTP GET request for "/person/{personId}/post" using the @GetMapping annotation.
	 * It prepares a new Post object and adds it to the Model, along with the personId and any error message from a previous operation.
	 * 
	 * The provided data is used to render the "addPostView" template, allowing users to input details for a new post.
	 * 
	 * @param model The Spring Model object used to pass data to the view template.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @param error The error message from a previous operation, if applicable.
	 * @param personId The ID of the person for whom the post is being added.
	 * @return The name of the view template, "addPostView", which will be rendered with the provided data.
	 */
	@GetMapping("/person/{personId}/post")
	public String addPostView(
		Model model, 
		RedirectAttributes redirectAttributes,
		@ModelAttribute("error") String error,
		@PathVariable Long personId
	) {
		try {
			Post post = new Post();
			
			// Check if Person Exists
			personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + personId + " not found"));
			
			// Add Attributes to Model
			model.addAttribute("post", post);
			model.addAttribute("personId", personId);
			model.addAttribute("error", error);
			
			return "addPostView";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath(); 
		}
	}
	
	/**
	 * Retrieves the view for adding a new comment to a post.
	 * 
	 * This method is mapped to the HTTP GET request for "/person/{personId}/post/{postId}" using the @GetMapping annotation.
	 * It prepares a new Post object (comment) and adds it to the Model, along with the parent post details, the person's details,
	 * and any error message from a previous operation.
	 * 
	 * The method also retrieves a paginated list of all persons for display in the view.
	 * 
	 * The provided data is used to render the "addCommentView" template, allowing users to input details for a new comment.
	 * 
	 * @param model The Spring Model object used to pass data to the view template.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @param page Page number for pagination of persons. Defaults to 0 if not provided.
	 * @param pageSize Number of items per page for persons' pagination. Defaults to 7 if not provided.
	 * @param error The error message from a previous operation, if applicable.
	 * @param personId The ID of the person who is adding the comment.
	 * @param postId The ID of the parent post for which the comment is being added.
	 * @return The name of the view template, "addCommentView", rendered with provided data.
	 */
	@GetMapping("/person/{personId}/post/{postId}")
	public String addCommentView(
		Model model,
		RedirectAttributes redirectAttributes,
		@RequestParam(defaultValue = "0") String page, 
		@RequestParam(defaultValue = "7") String pageSize, 
		@ModelAttribute("error") String error,
		@PathVariable Long personId,
		@PathVariable Long postId
	) {
		try {
			Post post = new Post();
			
			// Check if Person Exists
			Person person = personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + personId + " not found"));
			
			// Extract Pagination Parameters
			Long personsCount = personService.countPersons();
			Pagination pagination = paginationService.getPagination(page, pageSize, 7, personsCount);
			// Get all Persons
			List<Person> persons = personService.getPersons(pagination);
			
			// Check if Post Exists
			Post parentPost = postService.getPost(postId)
				.orElseThrow(() -> new RedirectException("/person/" + personId, "Post with Id " + postId + " not found"));
			
			// Add Attributes to Model
			model.addAttribute("post", post);
			model.addAttribute("parentPost", parentPost);
			model.addAttribute("person", person);
			model.addAttribute("persons", persons);
			model.addAttribute("pagination", pagination);
			model.addAttribute("error", error);
			
			return "addCommentView";
		}  catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
	
	/**
	 * Adds a new post to a person's profile.
	 * 
	 * This method is mapped to the HTTP POST request for "/person/{personId}/post" using the @PostMapping annotation.
	 * It receives a Post object from the form submission, adds it to the system using the postService, 
	 * and then redirects the user back to the person's profile view.
	 * 
	 * If any errors occur during the addition of the post, a RedirectException is caught. In this case, the error message
	 * is added as a flash attribute and the user is redirected back to the person's profile view to display the error.
	 * 
	 * @param post The Post object containing the content of the new post.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @param personId The ID of the person for whom the post is being added.
	 * @return A redirection URL based on the outcome of the addition operation.
	 */
	@PostMapping("/person/{personId}/post")
	public String addPost(
		Post post, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long personId
	) {
		try {
			// Check Form-Input Data
			if (post.getContent() == null || post.getContent().isEmpty()) {
				throw new InvalidFormInputException("/person/" + personId + "/post", "Content has to be specified");
			}
			
			// Get Creator and throw Exception if not exists
			Person creator = personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + personId + " not found"));
			
			// Add Post
			postService.addPost(new Post(post.getContent(), creator));
			
			return "redirect:/person/" + personId;
		}  catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
	
	/**
	 * Adds a new comment to a post on a person's profile.
	 *
	 * This method is mapped to the HTTP POST request for "/person/{personId}/post/{postId}" using the @PostMapping annotation.
	 * It receives a Post object from the form submission, representing the comment content and the creator's email.
	 * The comment is added to the specified parent post using the postService, and then the user is redirected back
	 * to the person's profile view.
	 *
	 * If any errors occur during the addition of the comment, a RedirectException is caught. In this case, the error message
	 * is added as a flash attribute and the user is redirected back to the parent post's view to display the error.
	 *
	 * @param post The Post object containing the content of the new comment and the creator's email.
	 * @param redirectAttributes The RedirectAttributes object to add flash attributes for redirection.
	 * @param personId The ID of the person whose post the comment is being added to.
	 * @param postId The ID of the parent post to which the comment is being added.
	 * @return A redirection URL based on the outcome of the addition operation.
	 */
	@PostMapping("/person/{personId}/post/{postId}")
	public String addComment(
		Post post, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long personId,
		@PathVariable Long postId
	) {
		try {
			// Check Form-Input Data
			if (post.getCreator() == null) {
				throw new InvalidFormInputException("/person/" + personId + "/post/" + postId, "Creator with Email has to be specified");
			}
			if (post.getCreator().getEmail() == null || post.getCreator().getEmail().isEmpty()) {
				throw new InvalidFormInputException("/person/" + personId + "/post/" + postId, "Creator with Email has to be specified");
			}
			if (post.getContent() == null || post.getContent().isEmpty()) {
				throw new InvalidFormInputException("/person/" + personId + "/post/" + postId, "Content has to be specified");
			}
			
			// Check if personId exists
			personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("/", "Person with Id " + personId + " not found"));
			
			// Get Parent Post and throw Exception if not exists
			Post parentPost = postService.getPost(postId)
				.orElseThrow(() -> new IdNotFoundException("/person/" + personId, "Post with Id " + postId + " not found"));
			
			// Get Creator and throw Exception if not exists
			Person creator = personService.getPerson(post.getCreator().getEmail())
				.orElseThrow(() -> new EmailNotFoundException("/person/" + personId + "/post/" + postId, "Person with email " + post.getCreator().getEmail() + " not found"));
			
			// Add Post
			postService.addPost(new Post(post.getContent(), creator, parentPost));
			
			return "redirect:/person/" + personId;
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:" + ex.getRedirectPath();
		}
	}
}
