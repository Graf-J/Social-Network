package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.InvalidRelationException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonRepository;

/**
 * Service class responsible for handling operations related to relations.
 */
@Service
public class RelationService {
	private final RelationRepository relationRepository;
	private final PersonRepository personRepository;
	
	public RelationService(RelationRepository relationRepository, PersonRepository personRepository) {
		this.relationRepository = relationRepository;
		this.personRepository = personRepository;
	}
	
	/**
	 * Adds a marriage relation between two persons.
	 * 
	 * This method handles the addition of a marriage relation between the specified creator and receiver persons.
	 * It performs validation checks to ensure that the two persons are not the same, and that they are not already married to someone else.
	 * If the relation already exists, an exception is thrown with an appropriate error message.
	 * 
	 * The method first checks if there's a marriage relation where either the creator or receiver is already married.
	 * If such a relation exists, it generates an error message accordingly.
	 * 
	 * The marriage relation is saved to the database, and if no family relation exists between the two persons, a family relation is also added.
	 * 
	 * @param creator The person who initiated the marriage relation.
	 * @param receiver The person who is being married.
	 * @throws InvalidRelationException If the relation is not valid, such as marrying oneself or marrying someone who is already married.
	 */
	public void addMarriageRelation(Person creator, Person receiver) {
		// Check that the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new InvalidRelationException("/addMarriage/" + creator.getId(), "You can't marry yourself");
		}
		
		// Check if there exists a relation where one of these is already married
		Optional<Relation> marriageRelation = relationRepository.findMarriage(creator, receiver);
		if (marriageRelation.isPresent()) {
			String errorMessage;
			
			if (creator.getId() == marriageRelation.get().getCreator().getId() ||
				creator.getId() == marriageRelation.get().getReceiver().getId()) {
				errorMessage = creator.getFirstName() + " " + creator.getLastName() + " is already married";
			} else {
				errorMessage = receiver.getFirstName() + " " + receiver.getLastName() + " is already married";
			}
			
			throw new InvalidRelationException("/addMarriage/" + creator.getId(), errorMessage);
		}
		
		// Save Marry and Family Relation to Database
		Relation marryRelation = new Relation(creator, receiver, RelationType.marriage);
		relationRepository.save(marryRelation);
		
		// Add Family Relation if not already exists
		Optional<Relation> familyRelation = relationRepository.findFamily(creator, receiver);
		if (familyRelation.isEmpty()) {
			addFamilyRelation(creator, receiver);
		}
	}
	
	/**
	 * Adds a family relation between two persons.
	 * 
	 * This method handles the addition of a family relation between the specified creator and receiver persons.
	 * It performs validation checks to ensure that the two persons are not the same and that a family relation between them does not already exist.
	 * If the relation already exists, an exception is thrown with an appropriate error message.
	 * 
	 * The method creates relations between the two persons, as well as relations between their respective family members.
	 * The family members of each person are connected to the other person through family relations.
	 * 
	 * @param creator The person who is establishing the family relation.
	 * @param receiver The person who is being related as a family member.
	 * @throws InvalidRelationException If the relation is not valid, such as creating a family relation with oneself or if the relation already exists.
	 */
	public void addFamilyRelation(Person creator, Person receiver) {
		// Check if the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new InvalidRelationException("/addFamilyMember/" + creator.getId(), "You can't be in a family relation with yourself");
		}
		
		// Check if Family Relation already exists
		Optional<Relation> familyRelation = relationRepository.findFamily(creator, receiver);
		if (familyRelation.isPresent()) {
			throw new InvalidRelationException("/addFamilyMember/" + creator.getId(), "Family relation between " + creator.getFirstName() + " and " + receiver.getFirstName() + " already exists");
		}

		List<Relation> relations = new ArrayList<>();
		
		// Add Relation between the two Persons
		relations.add(new Relation(creator, receiver, RelationType.family));
		
		// Add Family Members from one Person to the other and vice verca.
		List<Person> creatorFamilyMembers = personRepository.findFamilyMembers(creator);
		List<Person> receiverFamilyMembers = personRepository.findFamilyMembers(receiver);
		for (Person creatorFamilyMember : creatorFamilyMembers) {
			relations.add(new Relation(creatorFamilyMember, receiver, RelationType.family));
		}
		for (Person receiverFamilyMember : receiverFamilyMembers) {
			relations.add(new Relation(creator, receiverFamilyMember, RelationType.family));
		}
		for (Person creatorFamilyMember : creatorFamilyMembers) {
			for (Person receiverFamilyMember : receiverFamilyMembers) {
				relations.add(new Relation(creatorFamilyMember, receiverFamilyMember, RelationType.family));
			}
		}
		
		relationRepository.saveAll(relations);
	}
	
	/**
	 * Adds a friend relation between two persons.
	 * 
	 * This method handles the addition of a friend relation between the specified creator and receiver persons.
	 * It performs validation checks to ensure that the two persons are not the same and that a friend relation between them does not already exist.
	 * If the relation already exists, an exception is thrown with an appropriate error message.
	 * 
	 * The method creates a friend relation between the two persons in the relation repository.
	 * 
	 * @param creator The person initiating the friend relation.
	 * @param receiver The person being related as a friend.
	 * @throws InvalidRelationException If the relation is not valid, such as creating a friend relation with oneself or if the relation already exists.
	 */
	public void addFriendRelation(Person creator, Person receiver) {
		// Check if the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new InvalidRelationException("/addFriend/" + creator.getId(), "You can't be in a friend relation with yourself");
		}
		
		// Check if Friend Relation already exists
		Optional<Relation> friendRelation = relationRepository.findFriend(creator, receiver);
		if (friendRelation.isPresent()) {
			throw new InvalidRelationException("/addFriend/" + creator.getId(), "Friend relation between " + creator.getFirstName() + " and " + receiver.getFirstName() + " already exists");
		}
		
		Relation relation = new Relation(creator, receiver, RelationType.friend);
		relationRepository.save(relation);
	}
	
	/**
	 * Deletes all relation records.
	 */
	public void deleteRelations() {
		relationRepository.deleteAll();
	}
}
