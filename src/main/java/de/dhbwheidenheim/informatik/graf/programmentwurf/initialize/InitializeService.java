package de.dhbwheidenheim.informatik.graf.programmentwurf.initialize;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import de.dhbwheidenheim.informatik.graf.programmentwurf.person.Person;

@Service
public class InitializeService {
	public List<Person> getPersons() {
		List<Person> persons = new ArrayList<>();

		persons.add(new Person("Johannes", "Graf", "johannes.graf.1908@gmail.com", LocalDate.of(2000, Month.AUGUST, 19)));
		persons.add(new Person("Stefan", "Graf", "grafstefan@gmx.de", LocalDate.of(1968, Month.DECEMBER, 1)));
		persons.add(new Person("Lioba", "Graf", "graf.lili@gmx.de", LocalDate.of(1972, Month.APRIL, 22)));
		persons.add(new Person("Sophia", "Graf", "sophia.graefin@gmx.de", LocalDate.of(2003, Month.SEPTEMBER, 7)));
		persons.add(new Person("Rosalie", "Graf", "rosi.graefin@gmx.de", LocalDate.of(2010, Month.OCTOBER, 7)));
		persons.add(new Person("Anna", "Winkler", "anna.winkler@gmx.de", LocalDate.of(2001, Month.APRIL, 27)));
		persons.add(new Person("Thomas", "Geberle", "thommy@outlook.de", LocalDate.of(2001, Month.NOVEMBER, 12)));
		persons.add(new Person("Manuel", "Reiter", "reiter03@gmail.com", LocalDate.of(1996, Month.FEBRUARY, 28)));
		persons.add(new Person("Anja", "Kaim", "kaim.anja@outlook.de", LocalDate.of(1998, Month.DECEMBER, 3)));
		persons.add(new Person("Jochen", "Manzenrieder", "jochen.manze@gmx.de", LocalDate.of(1996, Month.AUGUST, 24)));
		persons.add(new Person("Joline", "Mayr", "joline00@gmail.de", LocalDate.of(2000, Month.SEPTEMBER, 13)));
		persons.add(new Person("Maximilian", "Baum", "baummaxi@gmail.com", LocalDate.of(2001, Month.OCTOBER, 14)));
		persons.add(new Person("Verena", "Drexler", "drexler.vere@outlook.de", LocalDate.of(2000, Month.MARCH, 5)));
		persons.add(new Person("Marcus", "Link", "marcus.link@gmx.com", LocalDate.of(2001, Month.SEPTEMBER, 8)));
		persons.add(new Person("Nikolas", "Munz", "munz.niki@gmx.de", LocalDate.of(2001, Month.DECEMBER, 14)));
		persons.add(new Person("Moritz", "Bestle", "bestleretzi@gmail.de", LocalDate.of(2002, Month.APRIL, 1)));
		persons.add(new Person("Sebastian", "Domler", "domler.basti@outlook.com", LocalDate.of(2001, Month.JANUARY, 3)));
		
		return persons;
	}
}
