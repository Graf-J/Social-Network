package de.dhbwheidenheim.informatik.graf.programmentwurf.relation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

@Service
public class RelationService {
	private final RelationRepository relationRepository;
	
	@Autowired
	public RelationService(RelationRepository relationRepository) {
		this.relationRepository = relationRepository;
	}
	
	public void addMarriageRelation(Person creator, Person receiver) {
		// Check that the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new IllegalArgumentException("You can't marry yourself");
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
			
			throw new IllegalArgumentException(errorMessage);
		}
		
		// Save Marry and Family Relation to Database
		Relation marryRelation = new Relation(creator, receiver, RelationType.marriage);
		Relation familyRelation = new Relation(creator, receiver, RelationType.family);
		
		relationRepository.saveAll(List.of(marryRelation, familyRelation));
	}
	
	public void addFamilyRelation(Person creator, Person receiver) {
		// Check if the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new IllegalArgumentException("You can't be in a family relation with yourself");
		}
		
		Relation relation = new Relation(creator, receiver, RelationType.family);
		relationRepository.save(relation);
	}
	
	public void addFriendRelation(Person creator, Person receiver) {
		// Check if the two Persons aren't the same Person
		if (creator.getId() == receiver.getId()) {
			throw new IllegalArgumentException("You can't be in a family relation with yourself");
		}
		
		Relation relation = new Relation(creator, receiver, RelationType.friend);
		relationRepository.save(relation);
	}
}
