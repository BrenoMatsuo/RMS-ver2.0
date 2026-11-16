package DAO;

import Model.User2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO2 extends DAO2 {
    public User2 login(String username, String password) throws SQLException {
        String sql = "SELECT u.*, s.\"position\" FROM \"tblUser2\" u "
                + "JOIN \"tblStaff2\" s ON s.\"tblUser2ID\" = u.\"ID\" "
                + "WHERE u.\"username\" = ? AND u.\"password\" = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                User2 user = new User2(result.getInt("ID"), result.getString("Name"),
                        result.getString("Address"), result.getDate("birthday"), result.getString("email"),
                        result.getString("PhoneNumber"), result.getString("username"), null);
                user.setPosition(result.getString("position"));
                return user;
            }
        }
    }

    public StaffRecord findStaff(int userId) throws SQLException {
        String sql = "SELECT \"tblUser2ID\", \"position\", \"tblRestaurant2ID\" "
                + "FROM \"tblStaff2\" WHERE \"tblUser2ID\" = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }
                return new StaffRecord(result.getInt("tblUser2ID"),
                        result.getString("position"), result.getInt("tblRestaurant2ID"));
            }
        }
    }

    public static final class StaffRecord {
        private final int userId;
        private final String position;
        private final int restaurantId;

        public StaffRecord(int userId, String position, int restaurantId) {
            this.userId = userId;
            this.position = position;
            this.restaurantId = restaurantId;
        }

        public int getUserId() { return userId; }
        public String getPosition() { return position; }
        public int getRestaurantId() { return restaurantId; }
    }
}
