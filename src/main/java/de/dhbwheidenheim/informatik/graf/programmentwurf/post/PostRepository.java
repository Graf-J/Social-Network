package de.dhbwheidenheim.informatik.graf.programmentwurf.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

/**
 * Repository interface for managing post entities in the data source.
 * Extends JpaRepository to inherit common CRUD and pagination operations.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	/**
	 * Retrieves a list of posts for a specific creator using a recursive query.
	 * The recursive approach ensures that all posts created by a person are retrieved,
	 * as well as posts that have any of these posts as a parentPost, and so on.
	 * This single query retrieves all necessary posts, avoiding the N + 1 problem
	 * associated with a @OneToMany relationship in the Post model. This approach
	 * positively impacts the application's performance.
	 *
	 * @param creatorId The ID of the creator whose posts are to be retrieved.
	 * @return A list of posts organized in a nested structure.
	 */
	@Query(value = 
		"WITH RECURSIVE recursive_posts AS ( " +
			"SELECT p1.id AS id, p1.parent_post_id AS parent_post_id, p1.content AS content, p1.created_at AS created_at, p1.creator_id AS creator_id " + 
			"FROM post p1 " + 
			"WHERE p1.creator_id = :creatorId AND p1.parent_post_id IS NULL " +
			"UNION " +
			"SELECT p2.id AS id, p2.parent_post_id AS parent_post_id, p2.content AS content, p2.created_at AS created_at, p2.creator_id AS creator_id " + 
			"FROM post p2 " +
			"INNER JOIN recursive_posts r ON (r.id = p2.parent_post_id)" +
		")" + 
		"SELECT * FROM recursive_posts " + 
		"ORDER BY created_at DESC", nativeQuery = true)
	List<Post> findAllRecursive(@Param("creatorId") Long creatorId);
	
	/**
	 * Counts the number of top-level posts created by a specific person.
	 *
	 * @param creator The person whose posts are counted.
	 * @return The count of top-level posts.
	 */
	@Query("SELECT COUNT(p) FROM Post p " + 
		   "WHERE p.creator = ?1 AND p.parentPost IS NULL")
	Long countPosts(Person creator);
	
	/**
	 * Retrieves a list of top-level posts (posts without parent posts).
	 *
	 * @return A list of top-level posts.
	 */
	@Query("SELECT p FROM Post p " + 
		   "WHERE p.parentPost IS NULL")
	List<Post> getPosts();
	
	/**
	 * Counts the number of comments created by a specific person.
	 * Comments are non-top-level posts
	 *
	 * @param creator The person whose comments are counted.
	 * @return The count of comments.
	 */
	@Query("SELECT COUNT(p) FROM Post p " + 
		   "WHERE p.creator = ?1 AND p.parentPost IS NOT NULL")
	Long countComments(Person creator);
	
	/**
	 * Retrieves a list of comments for a specific post.
	 *
	 * @param post The parent post for which to retrieve comments.
	 * @return A list of comments associated with the given parent post.
	 */
	@Query("SELECT p FROM Post p " + 
		   "WHERE p.parentPost = ?1")
	List<Post> getComments(Post post);
}
