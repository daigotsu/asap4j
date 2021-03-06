package com.epam.asap4j.service;

import com.epam.asap4j.dao.GroupParticipationDao;
import com.epam.asap4j.dao.PersonDao;
import com.epam.asap4j.dto.Group;
import com.epam.asap4j.dto.GroupParticipation;
import com.epam.asap4j.dto.Person;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "txManager")
public class GroupParticipationServiceImpl implements GroupParticipationService {

    @Autowired
    private UpsaRestService restService;

    @Autowired
    private GroupParticipationDao participationDao;

    @Autowired
    private PersonDao personDao;

    @Override
    public void fillGroupParticipations(Group group) {
        switch (group.getGroupType()) {
            case PROJECT:
                fillGroupParticipationsProjects(group.getExternalId(), group);
                break;
            case UNIT:
                fillGroupParticipationsUnit(group.getExternalId(), group);
                break;
            case LOCATION:
                fillGroupParticipationsLocation(group.getExternalId(), group);
            break;
        }
    }

    private void fillGroupParticipationsProjects(String projectId, Group group) {
        JSONArray projectTeam = restService.getProjectTeam(projectId);
        for (int i = 0; i < projectTeam.length(); i++) {
            JSONObject personObject = projectTeam.getJSONObject(i);
            String personId = personObject.getString("employeeId");
            Person person = personDao.getEntityById(personId);
            if (person == null) {
                person = new Person(personId, personObject.getString("fullName"));
                personDao.saveOrUpdate(person);
            }
            GroupParticipation participation = participationDao.getByGroupPersonId(group.getGroupId(), person.getPersonId());
            if (participation == null) {
                participation = new GroupParticipation(group, person);
                participationDao.saveOrUpdate(participation);
            }
        }
    }

    private void fillGroupParticipationsUnit(String unitId, Group group) {
        JSONArray unitEmployees = restService.getUnitEmployees(unitId);
        for (int i = 0; i < unitEmployees.length(); i++) {
            JSONObject personObject = unitEmployees.getJSONObject(i);
            String personId = personObject.getString("employeeId");
            Person person = personDao.getEntityById(personId);
            if (person == null) {
                person = new Person(personId, personObject.getString("fullName"));
                personDao.saveOrUpdate(person);
            }
            GroupParticipation participation = participationDao.getByGroupPersonId(group.getGroupId(), person.getPersonId());
            if (participation == null) {
                participation = new GroupParticipation(group, person);
                participationDao.saveOrUpdate(participation);
            }
        }
    }

    private void fillGroupParticipationsLocation(String personId, Group group) {
        Person person = personDao.getEntityById(personId);
        List<Person> persons = personDao.getPersonsFromSameLocation(person);
        for(Person p : persons) {
            GroupParticipation participation = participationDao.getByGroupPersonId(group.getGroupId(), p.getPersonId());
            if (participation == null) {
                participation = new GroupParticipation(group, p);
                participationDao.saveOrUpdate(participation);
            }
        }
    }
}
