package de.dhbwheidenheim.informatik.graf.programmentwurf.post;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

/**
 * Service class responsible for handling operations related to posts.
 */
@Service
public class PostService {
	private final PostRepository postRepository;
	
	/**
	 * Constructor for the PostService class.
	 *
	 * @param postRepository The PostRepository instance used for accessing and manipulating post-related data.
	 */
	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	/**
	 * Counts the number of posts created by a specific person. 
	 * Posts are entities without a parent-post.
	 *
	 * @param creator The Person instance representing the creator of the posts.
	 * @return The count of posts created by the specified creator.
	 */
	public Long countPosts(Person creator) {
		return postRepository.countPosts(creator);
	}
	
	/**
	 * Counts the number of comments created by a specific person.
	 * Comments are entities with a parent-post.
	 *
	 * @param creator The Person instance representing the creator of the comments.
	 * @return The count of comments created by the specified creator.
	 */
	public Long countComments(Person creator) {
		return postRepository.countComments(creator);
	}
	
	/**
	 * Retrieves a Post entity by its ID.
	 *
	 * @param id The ID of the Post to retrieve.
	 * @return An Optional containing the retrieved Post, or an empty Optional if the Post is not found.
	 */
	public Optional<Post> getPost(Long id) {
		return postRepository.findById(id);
	}
	
	/**
	 * Get posts by a creator and organize them into a nested structure within the child-posts.
	 *
	 * @param creator The creator for whom to fetch posts.
	 * @return A list of posts organized in a nested structure.
	 */
	public List<Post> getPostsByCreator(Person creator) {
		List<Post> flatPosts = postRepository.findAllRecursive(creator.getId());
		
		// Return empty List if flatPosts is empty
		if (flatPosts.isEmpty()) {
			return new ArrayList<>();
		}
		// Convert posts to HashMap with Key of parentPostId and Value of childPosts
		HashMap<Long, List<Post>> parentMap = convertPostsToHashMap(flatPosts);
		// Convert HashMap recursively into nested Structure with ChildPosts
		List<Post> posts = createChildPostStructure(parentMap.get(-1L), parentMap);
		
		return posts;
	}
	
	/**
	 * Convert a flat list of posts to a HashMap for easier organization.
	 * The Key is the ParentPostId and the value is a List of the Comments of the Post.
	 *
	 * @param posts The flat list of posts to convert.
	 * @return A HashMap with parentPostId as key and childPosts as value.
	 */
	private HashMap<Long, List<Post>> convertPostsToHashMap(List<Post> posts) {
		// HashMap stores a list of all Posts which have a certain parent
		HashMap<Long, List<Post>> parentMap = new HashMap<Long, List<Post>>();
		
		for (Post post : posts) {
			// Set parentId to -1 if no parent exists
			Long parentId = post.getParentPost() == null ? -1 : post.getParentPost().getId();
			
			// Create List for parentId if not exists
			if (!parentMap.containsKey(parentId)) {
				parentMap.put(parentId, new ArrayList<Post>());
			}
			// Set Parent Post to Null to prevent Recursion Problem
			post.setParentPost(null);
			// Add the Post to the List behind the Index of the Parent
			parentMap.get(parentId).add(post);
		}
		
		return parentMap;
	}
	
	/**
	 * Organize posts into a nested structure using the parentMap.
	 * This function gets executed recursively to append nested Child Posts to every Post.
	 *
	 * @param posts The list of posts to organize.
	 * @param parentMap The HashMap containing parent-post relationships.
	 * @return A list of posts organized in a nested structure.
	 */
	private List<Post> createChildPostStructure(List<Post> posts, HashMap<Long, List<Post>> parentMap) {
		for (Post post : posts) {
			List<Post> childPosts;
			
			// If HashMap has nothing stored at Index of ParentId, the Post has no Children
			if (!parentMap.containsKey(post.getId())) {
				childPosts = null;
			} 
			// If HashMap has a List at Index of ParentId, store ChildPosts in variable
			else {
				childPosts = parentMap.get(post.getId());
				// Call function recursively for ChildPosts
				createChildPostStructure(childPosts, parentMap);
			}
			
			post.setChildPosts(childPosts);
		}
		
		return posts;
	}
	
	/**
	 * Add a new post to the repository.
	 *
	 * @param post The post to be added.
	 */
	public void addPost(Post post) {
		postRepository.save(post);
	}
	
	/**
	 * Deletes all posts and their associated comments recursively.
	 * This method initiates the deletion process by calling the recursive
	 * deletion method for each top-level post.
	 */
	public void deletePosts() {
		List<Post> posts = postRepository.getPosts();
		
		for (Post post : posts) {
			deletePostsRecursively(post);
		}
	}
	
	/**
	 * Recursively deletes a post and its associated comments.
	 * This method first deletes all comments of the current post recursively,
	 * and then deletes the post itself. This is necessary due to the constrains
	 * between the different posts.
	 *
	 * @param post The post to be deleted.
	 */
	private void deletePostsRecursively(Post post) {
		List<Post> comments = postRepository.getComments(post);
		
		for (Post comment : comments) {
			deletePostsRecursively(comment);
		}
		
		postRepository.delete(post);
	}
}
