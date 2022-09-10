package ru.sh.db;

import java.sql.*;

class SQLUtil {
    public static PreparedStatement prepareStatement(PreparedStatement statement, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {

            Object arg = args[i];
            if (arg instanceof Integer) {
                statement.setInt(i + 1, (int) arg);
            } else if (arg instanceof String) {
                statement.setString(i + 1, (String) arg);
            } else if (arg instanceof java.util.Date) {
                statement.setTimestamp(i + 1, convertUtilToSql((java.util.Date) arg));
            }
        }

        return statement;
    }

    private static java.sql.Timestamp convertUtilToSql(java.util.Date uDate) {
        return new Timestamp(uDate.getTime());
    }
}
