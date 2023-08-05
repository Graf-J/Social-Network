package de.dhbwheidenheim.informatik.graf.programmentwurf.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	/**
     * Retrieves a list of posts for a specific creator using a recursive query.
     * The recursive approach ensures to get all posts a person created and all
     * posts which have one of these posts as a parentPost and so on...
     * Therefore is only one query necessary to receive all posts whereas you would
     * encounter a N + 1 problem with a @OneToMany approach in the Post-Model, which
     * would impact the performance of the application in a bad way.
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
}
