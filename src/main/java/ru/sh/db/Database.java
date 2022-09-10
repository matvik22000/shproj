package ru.sh.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sh.exceptions.TimeOccupiedException;
import ru.sh.exceptions.WrongTimeException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * database schema:
 * https://dbdiagram.io/d/5e04ecd6edf08a25543f6d66
 */

@Component
public class Database {
    private static final Log log = LogFactory.getLog(Database.class);
    @Autowired
    private DataSource dataSource;

    public void addRoom(String classNumber, int seats, int responsible) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Room room = new Room(classNumber, seats, responsible);
            room.putIntoDatabase(conn);
        }
    }

    public Room[] getRoomsList() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from classes"), args);
            ResultSet cursor = statement.executeQuery();
            List<Room> rooms = new ArrayList<>();

            while (cursor.next()) {
                String classNumber = cursor.getString("class_number");
                int seats = cursor.getInt("seats");
                int responsible = cursor.getInt("responsible");
                Room room = new Room(classNumber, seats, responsible);

                Object[] args2 = {classNumber};
                PreparedStatement statement2 = SQLUtil.prepareStatement(conn.prepareStatement("select * from class2type where class = ?"), args2);
                ResultSet cursor2 = statement2.executeQuery();
                List<Integer> classTypes = new ArrayList<>();
                while (cursor2.next()) {
                    classTypes.add(cursor2.getInt("type"));
                }

                room.setClassTypes(classTypes);
                rooms.add(room);
            }
            return rooms.toArray(new Room[0]);
        }
    }

    public void updateRoomInfo(String classNumber, int seats, int responsible) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Room room = new Room(classNumber, seats, responsible);
            room.updateInfoInDatabase(conn);
        }
    }

    public void addClassType(String classNumber, int type) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Room room = new Room(classNumber);
            room.addClassType(conn, type);
        }
    }

    public void removeClassType(String classNumber, int type) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Room room = new Room(classNumber);
            room.removeClassType(conn, type);
        }
    }

    public void reserveRoom(String classNumber, int teacherId, String reason, Date startTime, Date endTime, int customerId) throws SQLException {
        if (endTime.compareTo(startTime) < 0) {
            throw new WrongTimeException("startTime < endTime");
        }
        if (customerId == 0) {
            customerId = teacherId;
        }
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {classNumber, startTime, endTime, startTime, endTime};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select count(*) from schedule where class_number = ? and (start_time >= ? and start_time <= ?) or (end_time >= ? and end_time <= ?)"), args);
            ResultSet cursor = statement.executeQuery();
            cursor.next();
            log.debug(cursor.getInt(1));
            if (cursor.getInt(1) > 0) {
                throw new TimeOccupiedException("This time is already occupied");
            }
            Reservation reservation = new Reservation(classNumber, teacherId, reason, startTime, endTime, customerId);
            reservation.putIntoDatabase(conn);
        }

    }

    public void deleteReservation(int reservationId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {reservationId};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("delete from schedule where reservation_id = ?"), args);
            statement.executeUpdate();
        }
    }

    public void periodicalReservation(String classNumber, int teacherId, String reason,
                                      long startTime, long endTime,
                                      int customerId, int daysCount, boolean[] daysOfWeek) throws SQLException {

        final int MSINDAY = 86400000;
        final long reservationDuration = endTime - startTime;
        int currentDayOfWeek = Instant.ofEpochMilli(new Date(startTime).getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().getDayOfWeek().getValue();
        long currentTime = startTime;
        for (int i = 0; i <= daysCount; i++) {
            if (daysOfWeek[currentDayOfWeek - 1]) {
                this.reserveRoom(classNumber, teacherId, reason, new Date(currentTime), new Date(currentTime + reservationDuration), customerId);
            }
            currentTime += MSINDAY;
            currentDayOfWeek ++;
            if (currentDayOfWeek > 7) currentDayOfWeek = 1;
        }

    }

    public void updateReservationInfo(int reservationId, String classNumber, int teacherId, String reason, Date startTime, Date endTime, int customerId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Reservation reservation = new Reservation(classNumber, teacherId, reason, startTime, endTime, customerId);
            reservation.updateInfoInDatabase(conn, reservationId);
        }
    }

    public Reservation[] getReservations(Date searchStartTime, Date searchEndTime) throws SQLException {
        PreparedStatement statement;
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {searchStartTime, searchEndTime, searchStartTime, searchEndTime};
            statement = SQLUtil.prepareStatement(
                    conn.prepareStatement("select * from schedule where (start_time >= ? and start_time <= ?) or (end_time >= ? and end_time <= ?) order by start_time, end_time"), args
            );

            ResultSet cursor = statement.executeQuery();
            List<Reservation> result = new ArrayList<>();
            while (cursor.next()) {
                int reservationId = cursor.getInt("reservation_id");
                String classNumber = cursor.getString("class_number");
                int teacherId = cursor.getInt("teacher_id");
                String reason = cursor.getString("reason");
                Date startTime = cursor.getTimestamp("start_time");
                Date endTime = cursor.getTimestamp("end_time");
                int customerId = cursor.getInt("customer_id");
                Reservation reservation = new Reservation(reservationId, classNumber, teacherId, reason, startTime, endTime, customerId);
                result.add(reservation);
            }
            return result.toArray(new Reservation[0]);
        }
    }


    public String scheduleToCsv(Date searchStartTime, Date searchEndTime) throws SQLException {
        PreparedStatement statement;
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {searchStartTime, searchEndTime, searchStartTime, searchEndTime};
            statement = SQLUtil.prepareStatement(
                    conn.prepareStatement("select class_number, start_time, end_time, reason, fio from (schedule left join teachers on customer_id = teachers.prs_id) where (start_time >= ? and start_time <= ?) or (end_time >= ? and end_time <= ?) order by start_time, end_time"), args
            );
            ResultSet cursor = statement.executeQuery();
            StringBuilder result = new StringBuilder();
            while (cursor.next()) {
                String classNumber = cursor.getString("class_number");
                Date startTime = cursor.getTimestamp("start_time");
                Date endTime = cursor.getTimestamp("end_time");
                String reason = cursor.getString("reason");
                String fio = cursor.getString("fio");
                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startTimeStr = formatter.format(startTime);
                String endTimeStr = formatter.format(endTime);
                result.append(String.format("%s,%s,%s,%s,%s\n\r", classNumber, startTimeStr, endTimeStr, reason, fio));
            }
            return result.toString();
        }
    }

    public int getReservationId(String classNumber, Date startTime, Date endTime) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return getReservationId(conn, classNumber, startTime, endTime);
        }
    }

    public int getReservationId(Connection conn, String classNumber, Date startTime, Date endTime) throws SQLException {
        Object[] args = {classNumber, startTime, endTime};
        PreparedStatement statement = SQLUtil.prepareStatement(
                conn.prepareStatement("select reservation_id from schedule where class_number= ? and start_time= ? and end_time = ?"), args);
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt("reservation_id");

    }

    public Reservation getReservationInfo(int reservationId) throws SQLException {
        PreparedStatement statement;
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {reservationId};
            statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from schedule where reservation_id = ?"), args);

            ResultSet cursor = statement.executeQuery();
            cursor.next();
            return new Reservation(
                    cursor.getInt("reservation_id"),
                    cursor.getString("class_number"),
                    cursor.getInt("teacher_id"),
                    cursor.getString("reason"),
                    cursor.getTimestamp("start_time"),
                    cursor.getTimestamp("end_time"),
                    cursor.getInt("customer_id")
            );
        }
    }

    public void addTeacher(int prsId, String fio, String anotherInfo) throws SQLException {
        Teacher teacher = new Teacher(prsId, fio, anotherInfo);
        try (Connection conn = dataSource.getConnection()) {
            log.debug("trying to add teacher " + teacher.toString());
            teacher.putIntoDatabase(conn);
        }
    }

    public void deleteTeacher(int prsId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            log.debug("deleting teacher: " + prsId);
            Object[] args = {prsId};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("delete from teachers where prs_id = ?"), args);
            statement.executeUpdate();
        }
    }

    public Teacher getTeacher(int prsId) throws SQLException {
        PreparedStatement statement;
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {prsId};
            statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from teachers where prs_id = ?"), args);

            ResultSet cursor = statement.executeQuery();
            cursor.next();
            return new Teacher(
                    cursor.getInt("prs_id"),
                    cursor.getString("fio"),
                    cursor.getString("another_info"),
                    cursor.getBoolean("is_admin")
            );
        }
    }

    public Teacher[] getTeachersList() throws SQLException {
        PreparedStatement statement;
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.prepareStatement("select * from teachers");
            ResultSet cursor = statement.executeQuery();
            List<Teacher> result = new ArrayList<>();
            while (cursor.next()) {
                int prsId = cursor.getInt("prs_id");
                String fio = cursor.getString("fio");
                String anotherInfo = cursor.getString("another_info");
                result.add(new Teacher(prsId, fio, anotherInfo));
            }
            return result.toArray(new Teacher[0]);
        }

    }

    public boolean isInTeacherList(int prsId) throws SQLException {
        Teacher[] teacherList = this.getTeachersList();
        boolean response = false;
        for (Teacher teacher : teacherList) {
            if (teacher.getPrsId() == prsId) {
                response = true;
                break;
            }
        }
        return response;
    }

    public void addRoomType(String classDescription) throws SQLException {
        RoomType roomType = new RoomType(classDescription);
        try (Connection conn = dataSource.getConnection()) {
            roomType.putIntoDatabase(conn);
        }
    }

    public void deleteRoom(String classNumber) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {classNumber};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("delete from class2type where class = ?"), args);
            statement.executeUpdate();

            PreparedStatement statement2 = SQLUtil.prepareStatement(conn.prepareStatement("delete from schedule where class_number = ?"), args);
            statement2.executeUpdate();

            PreparedStatement statement3 = SQLUtil.prepareStatement(conn.prepareStatement("delete from classes where class_number = ?"), args);
            statement3.executeUpdate();
        }
    }


    public void changeRoomType(int typeId, String description) throws SQLException {
        RoomType roomType = new RoomType(typeId, description);
        try (Connection conn = dataSource.getConnection()) {
            roomType.updateRoomType(conn);
        }
    }

    public void deleteRoomType(int typeId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {typeId};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("delete from class_types where type_id = ?"), args);
            statement.executeUpdate();
        }
    }

    public RoomType getTypeDescription(int typeId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Object[] args = {typeId};
            PreparedStatement statement = SQLUtil.prepareStatement(conn.prepareStatement("select * from class_types where type_id = ?"), args);
            statement.executeQuery();
            ResultSet cursor = statement.executeQuery();
            cursor.next();
            String typeDescription = cursor.getString("type_description");
            return new RoomType(typeId, typeDescription);

        }
    }

    public RoomType[] getRoomTypesList() throws SQLException {
        PreparedStatement statement;
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.prepareStatement("select * from class_types");
            ResultSet cursor = statement.executeQuery();
            List<RoomType> result = new ArrayList<>();
            while (cursor.next()) {
                int typeId = cursor.getInt("type_id");
                String typeDescription = cursor.getString("type_description");
                result.add(new RoomType(typeId, typeDescription));
            }
            return result.toArray(new RoomType[0]);
        }
    }

    public void deleteOld() throws SQLException {
        // TODO use this function somewhere 
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("delete from schedule where start_time >= current_timestamp - interval '7 days'");
            statement.executeUpdate();
        }
    }

    ;
}
