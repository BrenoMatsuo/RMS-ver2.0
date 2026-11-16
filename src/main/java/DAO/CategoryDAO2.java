package DAO;

import Model.Category2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO2 extends DAO2 {
    public List<Category2> getAllCategory() throws SQLException {
        String sql = "SELECT * FROM \"tblCategory2\" ORDER BY \"Name\"";
        List<Category2> categories = new ArrayList<Category2>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                categories.add(new Category2(result.getInt("ID"),
                        result.getString("Name"), result.getString("Description")));
            }
        }
        return categories;
    }

    public Category2 getCategory(int id) throws SQLException {
        String sql = "SELECT \"ID\", \"Name\", \"Description\" FROM \"tblCategory2\" WHERE \"ID\" = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? new Category2(result.getInt("ID"),
                        result.getString("Name"), result.getString("Description")) : null;
            }
        }
    }
}
