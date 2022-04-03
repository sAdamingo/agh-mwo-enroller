package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Participant findByLogin(String login) {
		return connector.getSession().get(Participant.class, login);
	}

	public ResponseEntity addParticipant(Participant participant) {
		if (findByLogin(participant.getLogin()) != null) {
			return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
		return new ResponseEntity(HttpStatus.OK);
	}

	public ResponseEntity deleteParticipant(String login) {
		Participant participant = findByLogin(login);
		if (participant == null) {
			return new ResponseEntity("Unable to delete. Participant does not exist.", HttpStatus.CONFLICT);
		}
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
		return new ResponseEntity(HttpStatus.OK);
	}
}
