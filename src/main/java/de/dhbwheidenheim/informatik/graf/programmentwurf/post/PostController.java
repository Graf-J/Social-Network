package de.dhbwheidenheim.informatik.graf.programmentwurf.post;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.IdNotFoundException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.RedirectException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonService;

@Controller
public class PostController {
	private final PostService postService;
	private final PersonService personService;
	
	public PostController(PostService postService, PersonService personService) {
		this.postService = postService;
		this.personService = personService;
	}
	
	@GetMapping("posts/{id}")
	public @ResponseBody List<Post> getPosts(@PathVariable Long id) {
		Person creator = personService.getPerson(id)
			.orElseThrow(() -> new IdNotFoundException("Person with Id " + id + " not found"));
		
		return postService.getByCreator(creator);
	}
	
	@GetMapping("/person/{personId}/post")
	public String addPostView(
		Model model, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long personId
	) {
		try {
			Post post = new Post();
			
			// Check if Person Exists
			personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + personId + " not found"));
			
			// Add Attributes to Model
			model.addAttribute("post", post);
			model.addAttribute("personId", personId);
			
			return "addPostView";
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/"; 
		}
	}
	
	@GetMapping("/person/{personId}/post/{postId}")
	public String addCommentView(
		Model model,
		RedirectAttributes redirectAttributes,
		@PathVariable Long personId,
		@PathVariable Long postId
	) {
		try {
			Post post = new Post();
			
			// Check if Person Exists
			personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + personId + " not found"));
			
			// Check if Post Exists
			Post parentPost = postService.getPost(postId)
				.orElseThrow(() -> new RedirectException(personId, "Post with Id " + postId + " not found"));
			
			// Add Attributes to Model
			model.addAttribute("post", post);
			model.addAttribute("personId", personId);
			model.addAttribute("parentPost", parentPost);
			model.addAttribute("parentPost", parentPost);
			
			return "addCommentView";
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/person/" + ex.getRedirectId();
		}
	}
	
	@PostMapping("/person/{personId}/post")
	public String addPost(
		Post post, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long personId
	) {
		try {
			// Get Creator and throw Exception if not exists
			Person creator = personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + personId + " not found"));
			
			// Add Post
			postService.addPost(new Post(post.getContent(), creator));
			
			return "redirect:/person/" + personId;
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		}
	}
	
	@PostMapping("/person/{personId}/post/{postId}")
	public String addComment(
		Post post, 
		RedirectAttributes redirectAttributes,
		@PathVariable Long personId,
		@PathVariable Long postId
	) {
		try {
			// Get Creator and throw Exception if not exists
			Person creator = personService.getPerson(personId)
				.orElseThrow(() -> new IdNotFoundException("Person with Id " + personId + " not found"));
			
			// Get Parent Post and throw Exception if not exists
			Post parentPost = postService.getPost(postId)
				.orElseThrow(() -> new RedirectException(personId, "Post with Id " + postId + " not found"));
			
			// Add Post
			postService.addPost(new Post(post.getContent(), creator, parentPost));
			
			return "redirect:/person/" + personId;
		} catch (IdNotFoundException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/";
		} catch (RedirectException ex) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/person/" + ex.getRedirectId();
		}
	}
}
