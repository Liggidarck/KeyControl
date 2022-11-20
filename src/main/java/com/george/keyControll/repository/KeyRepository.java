package com.george.keyControll.repository;

import com.george.keyControll.model.Key;
import com.george.keyControll.model.Person;

import java.sql.*;
import java.util.ArrayList;

public class KeyRepository {

    private Connection connection;
    private Statement statement;

    public KeyRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            String PERSON_DATABASE_PATH = "jdbc:sqlite:src/main/resources/database/key.db";
            connection = DriverManager.getConnection(PERSON_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createKey(Key key) throws SQLException {
        String query = ("INSERT INTO keys (uid, personName, personImage, cabinet, dateTake, timeTake, timeReturn) " +
                "VALUES ('%s','%s','%s', '%s', '%s', '%s', '%s');")
                .formatted(key.getUid(),
                        key.getPersonName(),
                        key.getPersonImage(),
                        key.getCabinet(),
                        key.getDateTake(),
                        key.getTimeTake(),
                        key.getTimeReturn()
                );

        System.out.println(query);

        statement = connection.createStatement();
        statement.executeUpdate(query);
    }


    public Key getKeyByUid(String uid) throws SQLException {
        Key key = null;

        String query = "SELECT id, uid, personName, personImage, cabinet," +
                " dateTake, timeTake, timeReturn FROM keys WHERE uid = " + uid;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String namePerson = resultSet.getString("personName");
            String personImage = resultSet.getString("personImage");
            String cabinet = resultSet.getString("cabinet");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            key = new Key(uid, namePerson, personImage, cabinet,
                    dateTake, timeTake, timeReturn);
            key.setId(id);
        }

        return key;
    }


    public void updateKey(Key key) throws SQLException {
        int id = key.getId();
        String query = ("UPDATE keys SET uid = '%s', personName = '%s', personImage = '%s', cabinet = '%s'," +
                " dateTake = '%s', timeTake = '%s', timeReturn = '%s' WHERE id = " + id + ";")
                .formatted(key.getUid(),
                        key.getPersonName(),
                        key.getPersonImage(),
                        key.getCabinet(),
                        key.getDateTake(),
                        key.getTimeTake(),
                        key.getTimeReturn()
                );
        System.out.println(query);

        statement = connection.createStatement();
        statement.execute(query);
    }

    public ArrayList<Key> getAllKeys() throws SQLException {
        ArrayList<Key> keys = new ArrayList<>();

        statement = connection.createStatement();
        String query = "SELECT id, uid, personName, personImage, cabinet, dateTake, timeTake, timeReturn FROM keys";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String uid = resultSet.getString("uid");
            String namePerson = resultSet.getString("personName");
            String personImage = resultSet.getString("personImage");
            String cabinet = resultSet.getString("cabinet");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            Key key = new Key(uid, namePerson, personImage, cabinet,
                    dateTake, timeTake, timeReturn);
            key.setId(id);

            keys.add(key);
        }

        return keys;
    }

    public void closeConnection() throws SQLException {
        statement.close();
        connection.close();
    }


}
