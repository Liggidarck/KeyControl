package com.george.keyControll.viewModel;

import com.george.keyControll.model.Person;
import com.george.keyControll.repository.PersonRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class PersonViewModel {

    private final PersonRepository personRepository;

    public PersonViewModel() {
        personRepository = new PersonRepository();
    }

    public void createPerson(Person person) {
        try {
            personRepository.createPerson(person);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePerson(Person person) {
        try {
            personRepository.updatePerson(person);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePerson(int id) {
        try {
            personRepository.deletePerson(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Person> getAllPersons() {
        try {
            return personRepository.getAllPersons();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
