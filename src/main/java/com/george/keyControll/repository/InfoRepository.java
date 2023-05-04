package com.george.keyControll.repository;

import com.george.keyControll.model.Info;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class InfoRepository {

    private Connection connection;
    private Statement statement;

    public InfoRepository() {
        Preferences appPreferences = Preferences.userRoot().node("appPreferences");
        String databasePath = appPreferences.get("databasePath", "C:/database/");

        String INFO_DATABASE_PATH = "jdbc:sqlite:" + databasePath + "info.db";

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(INFO_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createInfo(Info info) throws SQLException {
        String query = ("INSERT INTO info (personName, personUid, " +
                "cabinet, cabinetUid, dateTake, timeTake, timeReturn) " +
                "VALUES ('" + info.getPersonName() + "','" + info.getPersonUid() + "'," +
                " '" + info.getCabinet() + "', '" + info.getCabinetUid() + "'," +
                " '" + info.getDateTake() + "', '" + info.getTimeTake() + "', '" + info.getTimeReturn() + "');");

        System.out.println(query);

        statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    public void updateInfo(Info info, int id) throws SQLException {
        String query = ("UPDATE info SET personName = '" + info.getPersonName() + "', personUid = '" + info.getPersonUid() + "', cabinet = '" + info.getCabinet() + "'," +
                " cabinetUid = '" + info.getCabinetUid() + "', dateTake = '" + info.getDateTake() + "', timeTake = '" + info.getTimeTake() + "', timeReturn = '" + info.getTimeReturn() + "' WHERE id = " + id + ";");
        System.out.println(query);

        statement = connection.createStatement();
        statement.execute(query);
    }

    public void deleteInfo(int id) throws SQLException {
        String query = "DELETE FROM info WHERE id = " + id;

        statement = connection.createStatement();
        statement.execute(query);
    }

    public Info getInfoByPersonUidAndDate(String uid, String date) throws SQLException {
        Info info = null;

        String query = ("SELECT id, personName, personUid, cabinet, cabinetUid, dateTake, " +
                "timeTake, timeReturn FROM info WHERE personUid = '" + uid + "' AND dateTake = '" + date + "' ;");

        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String personName = resultSet.getString("personName");
            String personUid = resultSet.getString("personUid");
            String cabinet = resultSet.getString("cabinet");
            String cabinetUid = resultSet.getString("cabinetUid");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            info = new Info(personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn);
            info.setId(id);
        }

        return info;
    }

    public Info getInfoById(int id) throws SQLException {
        Info info = null;
        String query = "SELECT * FROM info WHERE id = " + id;

        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            String personName = resultSet.getString("personName");
            String personUid = resultSet.getString("personUid");
            String cabinet = resultSet.getString("cabinet");
            String cabinetUid = resultSet.getString("cabinetUid");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            info = new Info(personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn);
            info.setId(id);
        }

        return info;
    }

    public List<Info> getListByKeyNumber(String keyNumber, String date) throws SQLException {
        List<Info> infoList = new ArrayList<>();
        ResultSet resultSet;

        String query;
        if (date.isEmpty()) {
            query = "SELECT * FROM info WHERE cabinet = '" + keyNumber + "';";
        } else {
            query = "SELECT * FROM info WHERE cabinet = '" + keyNumber + "' AND dateTake = '" + date + "';";
        }
        resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String personName = resultSet.getString("personName");
            String personUid = resultSet.getString("personUid");
            String cabinet = resultSet.getString("cabinet");
            String cabinetUid = resultSet.getString("cabinetUid");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            Info info = new Info(personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn);
            info.setId(id);

            infoList.add(info);
        }

        return infoList;
    }

    public Info getInfoByKeyUidAndDate(String uid, String date) throws SQLException {
        Info info = null;

        statement = connection.createStatement();
        String query = ("SELECT id, personName, personUid, cabinet, cabinetUid, dateTake," +
                " timeTake, timeReturn FROM info WHERE cabinetUid = '" + uid + "' AND dateTake = '" + date + "'; ");
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String personName = resultSet.getString("personName");
            String personUid = resultSet.getString("personUid");
            String cabinet = resultSet.getString("cabinet");
            String cabinetUid = resultSet.getString("cabinetUid");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            info = new Info(personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn);
            info.setId(id);
        }

        return info;
    }

    public List<Info> getListInfoByPersonUidAndDate(String uid, String date) throws SQLException {
        List<Info> infoList = new ArrayList<>();

        statement = connection.createStatement();
        String query = ("SELECT id, personName, personUid, cabinet, cabinetUid, dateTake," +
                " timeTake, timeReturn FROM info WHERE personUid = '" + uid + "' AND dateTake = '" + date + "'; ");
        System.out.println(query);
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String personName = resultSet.getString("personName");
            String personUid = resultSet.getString("personUid");
            String cabinet = resultSet.getString("cabinet");
            String cabinetUid = resultSet.getString("cabinetUid");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            Info info = new Info(personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn);
            info.setId(id);

            System.out.println(info.getPersonUid());

            infoList.add(info);

        }

        return infoList;
    }


    public List<Info> getAllInfo() throws SQLException {
        List<Info> infoList = new ArrayList<>();

        statement = connection.createStatement();
        String query = ("SELECT * FROM info;");
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String personName = resultSet.getString("personName");
            String personUid = resultSet.getString("personUid");
            String cabinet = resultSet.getString("cabinet");
            String cabinetUid = resultSet.getString("cabinetUid");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            Info info = new Info(personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn);
            info.setId(id);

            infoList.add(info);
        }

        return infoList;
    }

    public List<Info> getInfoByDate(String today) throws SQLException {
        List<Info> infoArrayList = new ArrayList<>();

        statement = connection.createStatement();
        String query = ("SELECT id, personName, personUid, cabinet, cabinetUid, " +
                "dateTake, timeTake, timeReturn FROM info WHERE dateTake = '" + today + "' ;");
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String personName = resultSet.getString("personName");
            String personUid = resultSet.getString("personUid");
            String cabinet = resultSet.getString("cabinet");
            String cabinetUid = resultSet.getString("cabinetUid");
            String dateTake = resultSet.getString("dateTake");
            String timeTake = resultSet.getString("timeTake");
            String timeReturn = resultSet.getString("timeReturn");

            Info info = new Info(personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn);
            info.setId(id);

            infoArrayList.add(info);
        }

        return infoArrayList;
    }

}
