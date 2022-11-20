package com.george.keyControll.repository;

import com.george.keyControll.model.Key;
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
        String query = ("INSERT INTO persons (uid, personName, personImage, cabinet) " +
                "VALUES ('%s','%s','%s', '%s');")
                .formatted(person.getUid(),
                        person.getName(),
                        person.getImage(),
                        person.getCabinet()
                );

        System.out.println(query);

        statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        connection.close();
    }


    public void updatePerson(Person person) throws SQLException {
        int id = person.getId();
        String query = ("UPDATE persons SET uid = '%s', personName = '%s', personImage = '%s', cabinet = '%s' WHERE id = " + id + ";")
                .formatted(person.getUid(),
                        person.getName(),
                        person.getImage(),
                        person.getCabinet()
                );
        System.out.println(query);

        statement = connection.createStatement();
        statement.execute(query);
        statement.close();
        connection.close();
    }

    public Person getPersonByUid(String uid) throws SQLException {
        Person person = null;

        String query = "SELECT id, uid, personName, personImage, cabinet FROM persons WHERE uid = " + uid;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String namePerson = resultSet.getString("personName");
            String personImage = resultSet.getString("personImage");
            String cabinet = resultSet.getString("cabinet");

            person = new Person(uid, namePerson, personImage, cabinet);
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
        String query = "SELECT id, uid, personName, personImage, cabinet FROM persons";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String uid = resultSet.getString("uid");
            String namePerson = resultSet.getString("personName");
            String personImage = resultSet.getString("personImage");
            String cabinet = resultSet.getString("cabinet");

            Person person = new Person(uid, namePerson, personImage, cabinet);
            person.setId(id);

            persons.add(person);
        }

        return persons;
    }

}