package smoke;

import Utils.DBConnection2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class DBConnectionTest2 {
    private DBConnectionTest2() {
    }

    public static void main(String[] args) throws Exception {
        String sql = "SELECT COUNT(*) FROM \"tblUser2\"";
        try (Connection connection = DBConnection2.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            if (!result.next()) {
                throw new IllegalStateException("Supabase did not return the tblUser2 row count.");
            }
            System.out.println("Supabase connection OK; tblUser2 rows: " + result.getLong(1));
        }
    }
}
