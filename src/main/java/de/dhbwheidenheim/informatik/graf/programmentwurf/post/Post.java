package de.dhbwheidenheim.informatik.graf.programmentwurf.post;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(
		name = "content",
		columnDefinition = "TEXT",
		nullable = false
	)
	private String content;
	
	@CreationTimestamp
	@JsonIgnore
	@Column(
		name = "created_at",
		nullable = false, 
		updatable = false
	)
	private LocalDateTime createdAt;
	
	@ManyToOne
    @JoinColumn(
    	name = "creator_id",
    	nullable = false
    )
    private Person creator;

    @ManyToOne
    @JoinColumn(
    	name = "parent_post_id",
    	updatable = false
    )
    private Post parentPost;
    
    @Transient
    private List<Post> childPosts;
    
    @Transient
    private String timeAgo;
    
    public Post() { }
    
    public Post(String content, Person creator) {
    	this.content = content;
    	this.creator = creator;
    }
    
    public Post(String content, Person creator, Post parentPost) {
    	this.content = content;
    	this.creator = creator;
    	this.parentPost = parentPost;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Post getParentPost() {
		return parentPost;
	}

	public void setParentPost(Post parentPost) {
		this.parentPost = parentPost;
	}

	public List<Post> getChildPosts() {
		return childPosts;
	}

	public void setChildPosts(List<Post> childPosts) {
		this.childPosts = childPosts;
	}
	
	public String getTimeAgo() {
		LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, currentTime);

        if (duration.getSeconds() < 60) {
            return "vor " + duration.getSeconds() + " Sekunden";
        } else if (duration.toMinutes() < 60) {
            return "vor " + duration.toMinutes() + " Minuten";
        } else if (duration.toHours() < 24) {
            return "vor " + duration.toHours() + " Stunden";
        } else if (duration.toDays() < 30) {
            return "vor " + duration.toDays() + " Tagen";
        } else {
            Long years = duration.toDays() / 365;
            return "vor " + years + " Jahren";
        }
	}
	
	public void setTimeAgo(String timeAgo) {
		this.timeAgo = timeAgo;
	}
}
