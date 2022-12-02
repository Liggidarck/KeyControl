package com.george.keyControll.repository;

import com.george.keyControll.model.Info;

import java.sql.*;
import java.util.ArrayList;

public class InfoRepository {

    private Connection connection;
    private Statement statement;

    public InfoRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            String PERSON_DATABASE_PATH = "jdbc:sqlite:src/main/resources/database/info.db";
            connection = DriverManager.getConnection(PERSON_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createInfo(Info info) throws SQLException {
        String query = ("INSERT INTO info (personName, personUid, " +
                "cabinet, cabinetUid, dateTake, timeTake, timeReturn) " +
                "VALUES ('%s','%s', '%s', '%s', '%s', '%s', '%s');")
                .formatted(info.getPersonName(),
                        info.getPersonUid(),
                        info.getCabinet(),
                        info.getCabinetUid(),
                        info.getDateTake(),
                        info.getTimeTake(),
                        info.getTimeReturn()
                );

        System.out.println(query);

        statement = connection.createStatement();
        statement.executeUpdate(query);
    }

    public void updateInfo(Info info, int id) throws SQLException {
        String query = ("UPDATE info SET personName = '%s', personUid = '%s', cabinet = '%s', cabinetUid = '%s', dateTake = '%s', timeTake = '%s', timeReturn = '%s' WHERE id = " + id + ";")
                .formatted(info.getPersonName(),
                        info.getPersonUid(),
                        info.getCabinet(),
                        info.getCabinetUid(),
                        info.getDateTake(),
                        info.getTimeTake(),
                        info.getTimeReturn()
                );
        System.out.println(query);

        statement = connection.createStatement();
        statement.execute(query);
    }

    public Info getInfoByPersonUidAndDate(String uid, String date) throws SQLException {
        Info info = null;

        statement = connection.createStatement();
        String query = "SELECT id, personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn FROM info WHERE personUid = '" + uid + "' AND dateTake = '" + date +"' ;";
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

    public Info getInfoByKeyUidAndDate(String uid, String date) throws SQLException {
        Info info = null;

        statement = connection.createStatement();
        String query = "SELECT id, personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn FROM info WHERE cabinetUid = '" + uid + "' AND dateTake = '" + date + "'; ";
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

    public ArrayList<Info> getInfoByDate(String today) throws SQLException {
        ArrayList<Info> infoArrayList = new ArrayList<>();

        statement = connection.createStatement();
        String query = "SELECT id, personName, personUid, cabinet, cabinetUid, dateTake, timeTake, timeReturn FROM info WHERE dateTake = '" + today + "' ;";
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
