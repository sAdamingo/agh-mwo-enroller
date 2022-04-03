package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/meeting")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Meeting>(meeting,HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        return meetingService.addMeeting(meeting);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") Long id) {
        return meetingService.deleteMeeting(id);
    }

    @RequestMapping(value = "/{id}/addParticipant/{login}", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") Long id, @PathVariable("login") String login) {
        return meetingService.addParticipantToMeeting(id,login);
    }
    @RequestMapping(value = "{id}/deleteParticipant/{login}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") Long id, @PathVariable("login") String login){
        return meetingService.deleteParticipantFromMeeting(id,login);
    }
    @RequestMapping(value = "/{id}/getParticipants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") Long id) {
        return meetingService.getMeetingParticipants(id);
    }
    @RequestMapping(value = "/{id}/updateTitle/{title}", method = RequestMethod.POST)
    public ResponseEntity<?> updateMeetingTitle(@PathVariable("id") Long id, @PathVariable("title") String title){
        return meetingService.updateTitle(id,title);
    }
    @RequestMapping(value = "/{id}/updateDescription/{description}", method = RequestMethod.POST)
    public ResponseEntity<?> updateMeetingDescription(@PathVariable("id") Long id, @PathVariable("description") String description){
        return meetingService.updateDescription(id,description);
    }
    @RequestMapping(value = "/{id}/updateDate/{date}", method = RequestMethod.POST)
    public ResponseEntity<?> updateMeetingDate(@PathVariable("id") Long id, @PathVariable("date") String date){
        return meetingService.updateDate(id,date);
    }
}
