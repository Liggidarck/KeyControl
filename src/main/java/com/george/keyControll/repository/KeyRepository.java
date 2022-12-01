package com.george.keyControll.repository;

import com.george.keyControll.model.Key;

import java.sql.*;
import java.util.ArrayList;

public class KeyRepository {

    private Connection connection;
    private Statement statement;

    public KeyRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            String PERSON_DATABASE_PATH = "jdbc:sqlite:src/main/resources/database/keys.db";
            connection = DriverManager.getConnection(PERSON_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public void createKey(Key key) throws SQLException {

        String query = ("INSERT INTO keys (uid, number, available) " +
                "VALUES ('%s','%s', '%s');")
                .formatted(key.getUid(),
                        key.getNumber(),
                        key.getAvailable()
                );

        System.out.println(query);

        statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
        connection.close();
    }

    public void updateKey(Key key) throws SQLException {
        int id = key.getId();
        String query = ("UPDATE keys SET uid = '%s', number = '%s', available = '%s' WHERE id = " + id + ";")
                .formatted(key.getUid(),
                        key.getNumber(),
                        key.getAvailable()
                );
        System.out.println(query);

        statement = connection.createStatement();
        statement.execute(query);
        statement.close();
        connection.close();
    }


    public void deleteKey(int id) throws SQLException {
        String query = "DELETE FROM keys WHERE id = " + id;
        statement = connection.createStatement();
        statement.executeUpdate(query);
    }
    public Key getKeyByUid(String uid) throws SQLException {
        Key key = null;

        String query = "SELECT id, uid, number, available FROM keys WHERE uid = " + uid;
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String number = resultSet.getString("number");
            String available = resultSet.getString("available");

            key = new Key(uid, number, available);
            key.setId(id);
        }

        return key;
    }

    public ArrayList<Key> getAllKeys() throws SQLException {
        ArrayList<Key> keys = new ArrayList<>();

        statement = connection.createStatement();
        String query = "SELECT id, uid, number, available FROM keys";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String uid = resultSet.getString("uid");
            String number = resultSet.getString("number");
            String available = resultSet.getString("available");

            Key key = new Key(uid, number, available);
            key.setId(id);

            keys.add(key);
        }

        return keys;
    }


    //

}
