package ru.sh.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class Teacher {
    private static final Log log = LogFactory.getLog(Teacher.class);
    private int prsId;
    private String fio;
    private String anotherInfo;
    private boolean isAdmin;


    public Teacher(int prsId, String fio, String anotherInfo) {
        this.prsId = prsId;
        this.fio = fio;
        this.anotherInfo = anotherInfo;
    }

    public Teacher(int prsId, String fio, String anotherInfo, boolean isAdmin) {
        this.prsId = prsId;
        this.fio = fio;
        this.anotherInfo = anotherInfo;
        this.isAdmin = isAdmin;
    }

    public void putIntoDatabase(Connection connection) throws SQLException {
            Object[] args = {prsId, fio, anotherInfo};
            PreparedStatement statement = SQLUtil.prepareStatement(
                    connection.prepareStatement("insert into teachers(prs_id, fio, another_info) VALUES (?, ?, ?)"), args);
            statement.executeUpdate();
            log.info("teacher added " + Arrays.toString(args));
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getPrsId() {
        return prsId;
    }

    public void setPrsId(int prsId) {
        this.prsId = prsId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getAnotherInfo() {
        return anotherInfo;
    }

    public void setAnotherInfo(String anotherInfo) {
        this.anotherInfo = anotherInfo;
    }

    @Override
    public String toString() {
        return prsId + " " + fio + " " + anotherInfo;
    }
}
