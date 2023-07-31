package de.dhbwheidenheim.informatik.graf.programmentwurf.person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "person")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(
		name = "first_name",
		nullable = false
	)
	private String firstName;
	
	@Column(
		name = "last_name",
		nullable = false
	)
	private String lastName;
	
	@Column(
		name = "email",
		nullable = false, 
		unique = true
	)
	private String email;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(
		name = "birthday",
		nullable = false
	)
	private LocalDate birthday;
	
	@CreationTimestamp
	@JsonIgnore
	@Column(
		name = "created_at",
		nullable = false, 
		updatable = false
	)
	private LocalDateTime createdAt;
	
	@Transient
	private Integer age;
	

	public Person() { }
	
	public Person(
		String firstName,
		String lastName,
		String email,
		LocalDate birthday
	) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.birthday = birthday;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public Integer getAge() {
		return Period.between(birthday, LocalDate.now()).getYears();
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
