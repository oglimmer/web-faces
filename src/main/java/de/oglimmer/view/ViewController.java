package de.oglimmer.view;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.oglimmer.model.Person;
import de.oglimmer.service.PersonService;

@Named
@RequestScoped
public class ViewController {

	@Inject
	private PersonService personService;

	private Person person = new Person();

	public Person getPerson() {
		return person;
	}

	public void loadData() {
		person = personService.getFull(person.getId());
	}

}
