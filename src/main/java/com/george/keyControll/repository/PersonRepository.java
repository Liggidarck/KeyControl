package com.george.keyControll.repository;

import com.george.keyControll.model.Person;

import java.sql.*;
import java.util.ArrayList;

public class PersonRepository {

    private Connection connection;
    private Statement statement;

    public PersonRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            String PERSON_DATABASE_PATH = "jdbc:sqlite:src/main/resources/database/persons.db";
            connection = DriverManager.getConnection(PERSON_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createPerson(Person person) throws SQLException {
        String query = ("INSERT INTO persons (uid, name) " +
                "VALUES ('%s','%s');")
                .formatted(person.getUid(),
                        person.getName()
                );

        System.out.println(query);

        statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        connection.close();
    }


    public void updatePerson(Person person) throws SQLException {
        int id = person.getId();
        String query = ("UPDATE persons SET uid = '%s', name = '%s' WHERE id = " + id + ";")
                .formatted(person.getUid(),
                        person.getName()
                );
        System.out.println(query);

        statement = connection.createStatement();
        statement.execute(query);
        statement.close();
        connection.close();
    }

    public Person getPersonByUid(String uid) throws SQLException {
        Person person = null;

        String query = "SELECT id, uid, name FROM persons WHERE uid = " + uid;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String namePerson = resultSet.getString("name");

            person = new Person(uid, namePerson);
            person.setId(id);
        }

        return person;
    }

    public void deletePerson(int id) throws SQLException {
        String query = "DELETE FROM persons WHERE id = " + id;
        statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    public ArrayList<Person> getAllPersons() throws SQLException {
        ArrayList<Person> persons = new ArrayList<>();

        statement = connection.createStatement();
        String query = "SELECT id, uid, name FROM persons";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String uid = resultSet.getString("uid");
            String namePerson = resultSet.getString("name");

            Person person = new Person(uid, namePerson);
            person.setId(id);

            persons.add(person);
        }

        return persons;
    }

}