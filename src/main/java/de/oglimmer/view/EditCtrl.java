package de.oglimmer.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.oglimmer.model.CommunicationChannel;
import de.oglimmer.model.Person;
import de.oglimmer.service.PersonService;

@Named
@ConversationScoped
public class EditCtrl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Conversation conversation;

	@Inject
	private PersonService personService;

	private Person person = new Person();

	public Person getPerson() {
		return person;
	}

	public void add() {
		person.getCommunicationChannels().add(new CommunicationChannel());
	}

	public void remove(CommunicationChannel item) {
	}

	public String begin(String id) {
		if (conversation.isTransient()) {
			conversation.begin();
			if (id != null && !id.isEmpty()) {
				long intId = Long.parseLong(id);
				person = personService.getFull(intId);
			}
			addRow();
			return "edit1";
		}

		throw new IllegalStateException();
	}

	public String cancel() throws IOException {

		conversation.end();

		if (person.getId() == null) {
			return "list?faces-redirect=true";
		} else {
			return "view?id=" + person.getId() + "&faces-redirect=true";
		}
	}

	public String save() throws IOException {
		List<CommunicationChannel> tmp = person.getCommunicationChannels().stream().filter(
				e -> e.getData() != null && e.getType() != null && !e.getData().isEmpty() && !e.getType().isEmpty())
				.collect(Collectors.toList());
		person.setCommunicationChannels(tmp);
		personService.save(person);
		conversation.end();
		return "view?id=" + person.getId() + "&faces-redirect=true";
	}

	public void addRow() {
		CommunicationChannel cc = new CommunicationChannel();
		cc.setPerson(person);
		person.getCommunicationChannels().add(cc);
	}

	public String cont() {
		return "edit2";
	}

	public String back() {
		return "edit1";
	}

	public String deleteObject(String id) {
		long intId = Long.parseLong(id);
		personService.deleteObject(intId);
		
		return "list?faces-redirect=true";
	}
	
}
