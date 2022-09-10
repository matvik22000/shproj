package ru.sh.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Room {
    private String classNumber;
    private int seats;
    private int responsible;
    private List<Integer> classTypes;

    public Room (String classNumber) {
        this.classNumber = classNumber;
    }

    public Room(String classNumber, int seats, int responsible) {
        this.classNumber = classNumber;
        this.seats = seats;
        this.responsible = responsible;
    }

    public void putIntoDatabase(Connection connection) throws SQLException {

        Object[] args = {this.classNumber, this.seats, this.responsible};
        PreparedStatement statement = SQLUtil.prepareStatement(
                connection.prepareStatement("INSERT INTO classes(class_number, seats, responsible) values (?, ?, ?)"), args);
        statement.executeUpdate();

    }

    public void updateInfoInDatabase(Connection connection) throws SQLException {
        Object[] args = {this.seats, this.responsible, this.classNumber};
        PreparedStatement statement = SQLUtil.prepareStatement(
                connection.prepareStatement("update classes set seats=?, responsible=? where class_number=?"), args);
        statement.executeUpdate();
    }

    public void addClassType(Connection connection, int classType) throws SQLException {
        Object[] args = {this.classNumber, classType};
        PreparedStatement statement = SQLUtil.prepareStatement(
                connection.prepareStatement("insert into class2type (class, type) VALUES (?, ?)"), args);
        statement.executeUpdate();
    }

    public void removeClassType(Connection connection, int classType) throws SQLException {
        Object[] args = {this.classNumber, classType};
        PreparedStatement statement = SQLUtil.prepareStatement(
                connection.prepareStatement("delete from class2type where class=? and type=?"), args);
        statement.executeUpdate();
    }

    public String getClassNumber() {
        return classNumber;
    }

    public List<Integer> getClassTypes() {
        return classTypes;
    }

    public void setClassTypes(List<Integer> classTypes) {
        this.classTypes = classTypes;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getResponsible() {
        return responsible;
    }

    public void setResponsible(int responsible) {
        this.responsible = responsible;
    }
}
