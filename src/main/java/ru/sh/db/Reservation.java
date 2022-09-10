package ru.sh.db;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Reservation {
    private int reservationId;
    private String classNumber;
    private int teacherId;
    private String reason;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Date endTime;
    private int customerId;

    public Reservation(String classNumber, int teacherId, String reason, Date startTime, Date endTime, int customerId) {
        this.classNumber = classNumber;
        this.teacherId = teacherId;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
    }

    public Reservation(int reservationId, String classNumber, int teacherId, String reason, Date startTime, Date endTime, int customerId) {
        this.reservationId = reservationId;
        this.classNumber = classNumber;
        this.teacherId = teacherId;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
    }

    public void putIntoDatabase(Connection connection) throws SQLException {
        if (customerId != 0) {
            Object[] args = {classNumber, teacherId, reason, startTime, endTime, customerId};
            PreparedStatement statement = SQLUtil.prepareStatement(
                    connection.prepareStatement("insert into schedule(class_number, teacher_id, reason, start_time, end_time, customer_id) VALUES (?, ?, ?, ?, ?, ?)"), args);
            statement.executeUpdate();
        } else {
            Object[] args = {classNumber, teacherId, reason, startTime, endTime};
            PreparedStatement statement = SQLUtil.prepareStatement(
                    connection.prepareStatement("insert into schedule(class_number, teacher_id, reason, start_time, end_time) VALUES (?, ?, ?, ?, ?)"), args);
            statement.executeUpdate();
        }
    }

    public void updateInfoInDatabase(Connection connection, int reservationId) throws SQLException {
        Object[] args = {classNumber, teacherId, reason, startTime, endTime, customerId, reservationId};
        PreparedStatement statement = SQLUtil.prepareStatement(
                connection.prepareStatement("update schedule set class_number= ?, teacher_id = ?, reason = ?, start_time = ?, end_time = ?, customer_id = ? where reservation_id= ?")
                , args);
        statement.executeUpdate();
    }

    @Override
    public String toString() {
        return this.reservationId + " " + this.startTime.getTime() + " " +  this.endTime.getTime();
    }


    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
