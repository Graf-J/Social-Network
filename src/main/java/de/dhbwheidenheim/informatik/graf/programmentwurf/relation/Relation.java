package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

/**
 * Represents a relation entity with basic information.
 * This class is annotated with JPA annotations to map it to the corresponding database table.
 */
@Entity
@Table(name = "relation")
public class Relation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(
		name = "creator_id",
		nullable = false
	)
	private Person creator;
	
	@ManyToOne
	@JoinColumn(
		name = "receiver_id",
		nullable = false
	)
	private Person receiver;
	
	@Enumerated(EnumType.STRING)
	@Column(
		name = "type",
		nullable = false
	)
	private RelationType type;
	

	@CreationTimestamp
	@JsonIgnore
	@Column(
		name = "created_at",
		nullable = false, 
		updatable = false
	)
	private LocalDateTime createdAt;
	
	public Relation() { }

	public Relation(
		Person creator, 
		Person receiver,
		RelationType type
	) {
		this.creator = creator;
		this.receiver = receiver;
		this.type = type;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Person getReceiver() {
		return receiver;
	}

	public void setReceiver(Person receiver) {
		this.receiver = receiver;
	}

	public RelationType getType() {
		return type;
	}

	public void setType(RelationType type) {
		this.type = type;
	}
}
