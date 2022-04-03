package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	@Autowired
	ParticipantService participantService;

	DatabaseConnector connector;

	public MeetingService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Meeting findById(Long id) {
		return connector.getSession().get(Meeting.class, id);
    }

	public ResponseEntity<?> addMeeting(Meeting meeting) {
		if(findById(meeting.getId()) == null) {
			Transaction transaction = connector.getSession().beginTransaction();
			connector.getSession().save(meeting);
			transaction.commit();
			return new ResponseEntity<>("Meeting successfully added",HttpStatus.OK);
		}
		return new ResponseEntity<>("This meeting already exists", HttpStatus.CONFLICT);
	}

	public ResponseEntity<?> deleteMeeting(Long id) {
		Meeting byId = findById(id);
		if (byId == null){
			return new ResponseEntity<>("There is no such meeting",HttpStatus.NOT_FOUND);
		}
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(byId);
		transaction.commit();
		return new ResponseEntity<>("<h1>Meeting with id: "+ id + " was successfully deleted.</h1>",HttpStatus.OK);
	}

	public ResponseEntity<?> addParticipantToMeeting(Long id, String login) {
		Meeting meeting = findById(id);
		if (meeting == null) {
			return new ResponseEntity<>("There is no such meeting Sir!", HttpStatus.BAD_REQUEST);
		}

		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>("There is no such a guy in this company! Put yourself together!",HttpStatus.BAD_REQUEST);
		}
		if (meeting.getParticipants().contains(participant)) {
			return new ResponseEntity<>("This participant already attends to this meeting!",HttpStatus.BAD_REQUEST);
		}
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.addParticipant(participant);
		connector.getSession().save(meeting);
		transaction.commit();
		return new ResponseEntity<>("Participant added to the meeting", HttpStatus.OK);
	}

	public ResponseEntity<?> deleteParticipantFromMeeting(Long id, String login) {
		Meeting meeting = findById(id);
		if (meeting == null) {
			return new ResponseEntity<>("There is no such meeting Sir!", HttpStatus.BAD_REQUEST);
		}

		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity<>("There is no such a guy in this company! Put yourself together!",HttpStatus.BAD_REQUEST);
		}
		if (!meeting.getParticipants().contains(participant)) {
			return new ResponseEntity<>("This user is not attending this session!",HttpStatus.BAD_REQUEST);
		}
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.removeParticipant(participant);
		connector.getSession().save(meeting);
		transaction.commit();
		return new ResponseEntity<>("Participant removed from the meeting", HttpStatus.OK);
	}

	public ResponseEntity<?> getMeetingParticipants(Long id) {
		Meeting meeting = findById(id);
		if (meeting == null) {
			return new ResponseEntity<>("There is no such meeting Sir!", HttpStatus.BAD_REQUEST);
		}
		if (meeting.getParticipants().isEmpty()){
			return new ResponseEntity<>("No one attends to this meeting.", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(meeting.getParticipants(),HttpStatus.OK);
	}

	public ResponseEntity<?> updateTitle(Long id, String title) {
		Meeting meeting = findById(id);
		if (meeting == null) {
			return new ResponseEntity<>("There is no such meeting Sir!", HttpStatus.BAD_REQUEST);
		}
		meeting.setTitle(title);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		return new ResponseEntity<>("Title changed",HttpStatus.OK);
	}

	public ResponseEntity<?> updateDescription(Long id, String description) {
		Meeting meeting = findById(id);
		if (meeting == null) {
			return new ResponseEntity<>("There is no such meeting Sir!", HttpStatus.BAD_REQUEST);
		}
		meeting.setDescription(description);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		return new ResponseEntity<>("Description changed",HttpStatus.OK);
	}

	public ResponseEntity<?> updateDate(Long id, String date) {
		Meeting meeting = findById(id);
		if (meeting == null) {
			return new ResponseEntity<>("There is no such meeting Sir!", HttpStatus.BAD_REQUEST);
		}
		meeting.setDate(date);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		return new ResponseEntity<>("Date changed",HttpStatus.OK);
	}
}
