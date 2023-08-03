package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.exceptions.InvalidRelationException;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;
import de.dhbwheidenheim.informatik.graf.programmentwurf.person.PersonRepository;

@Service
public class RelationService {
	private final RelationRepository relationRepository;
	private final PersonRepository personRepository;
	
	public RelationService(RelationRepository relationRepository, PersonRepository personRepository) {
		this.relationRepository = relationRepository;
		this.personRepository = personRepository;
	}
	
	public void addMarriageRelation(Person creator, Person receiver) {
		// Check that the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new InvalidRelationException(creator.getId(), "You can't marry yourself");
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
			
			throw new InvalidRelationException(creator.getId(), errorMessage);
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
	
	public void addFamilyRelation(Person creator, Person receiver) {
		// Check if the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new InvalidRelationException(creator.getId(), "You can't be in a family relation with yourself");
		}
		
		// Check if Family Relation already exists
		Optional<Relation> familyRelation = relationRepository.findFamily(creator, receiver);
		if (familyRelation.isPresent()) {
			throw new InvalidRelationException(creator.getId(), "Family relation between " + creator.getFirstName() + " and " + receiver.getFirstName() + " already exists");
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
	
	public void addFriendRelation(Person creator, Person receiver) {
		// Check if the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new InvalidRelationException(creator.getId(), "You can't be in a friend relation with yourself");
		}
		
		// Check if Friend Relation already exists
		Optional<Relation> friendRelation = relationRepository.findFriend(creator, receiver);
		if (friendRelation.isPresent()) {
			throw new InvalidRelationException(creator.getId(), "Friend relation between " + creator.getFirstName() + " and " + receiver.getFirstName() + " already exists");
		}
		
		Relation relation = new Relation(creator, receiver, RelationType.friend);
		relationRepository.save(relation);
	}
	
	public void deleteRelations() {
		relationRepository.deleteAll();
	}
}
