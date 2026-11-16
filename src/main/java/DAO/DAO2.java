package DAO;

import Utils.DBConnection2;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DAO2 {
    protected Connection getConnection() throws SQLException {
        return DBConnection2.getConnection();
    }
}
