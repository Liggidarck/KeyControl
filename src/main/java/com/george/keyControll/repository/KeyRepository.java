package com.george.keyControll.repository;

import com.george.keyControll.model.Key;

import java.sql.*;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class KeyRepository {

    private Connection connection;
    private Statement statement;

    public KeyRepository() {

        Preferences appPreferences = Preferences.userRoot().node("appPreferences");
        String databasePath = appPreferences.get("databasePath", "C:/database/");

        String KEY_DATABASE_PATH = "jdbc:sqlite:" + databasePath + "keys.db";

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(KEY_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public void createKey(Key key) throws SQLException {
        String query = ("INSERT INTO keys (uid, number, available) " +
                "VALUES ('"+key.getUid()+"','"+key.getNumber()+"', '"+key.getAvailable()+"' );");

        System.out.println(query);

        statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    public void updateKey(Key key, int id) throws SQLException {
        String query = ("UPDATE keys SET uid = '"+key.getUid()+"', number = '"+key.getNumber()+"', " +
                "available = '"+key.getAvailable()+"' WHERE id = " + id + ";");
        System.out.println(query);

        statement = connection.createStatement();
        statement.execute(query);
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

    public Key getKeyByNumber(String keyNumber) throws SQLException {
        Key key = null;

        String query = "SELECT id, uid, number, available FROM keys WHERE number = '" + keyNumber + "'";
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String uid = resultSet.getString("uid");
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

}
