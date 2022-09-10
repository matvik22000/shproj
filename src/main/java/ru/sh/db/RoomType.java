package ru.sh.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoomType {
    private int typeId;
    private String typeDescription;

    public RoomType(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public RoomType(int typeId, String typeDescription) {
        this.typeId = typeId;
        this.typeDescription = typeDescription;
    }

    public void putIntoDatabase(Connection connection) throws SQLException {
        Object[] args = {this.typeDescription};
        PreparedStatement statement = SQLUtil.prepareStatement(
                connection.prepareStatement("insert into class_types(type_description) VALUES (?)"), args);
        statement.executeUpdate();


    }

    public void updateRoomType(Connection connection) throws SQLException {
        Object[] args = {this.typeDescription, typeId};
        PreparedStatement statement = SQLUtil.prepareStatement(
                connection.prepareStatement("update class_types set type_description = ? where type_id = ?"), args);
        statement.executeUpdate();


    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }
}
