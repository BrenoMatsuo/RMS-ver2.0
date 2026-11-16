package DAO;

import Model.Ingredient2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO2 extends DAO2 {
    public List<Ingredient2> searchIngredient(String name, int supplierId) throws SQLException {
        String sql = "SELECT DISTINCT i.\"ID\", i.\"Name\", i.\"Unit\", i.\"CurrentAmount\", i.\"AveragePrice\" "
                + "FROM \"tblIngredient2\" i JOIN \"tblSupplierIngredient2\" si "
                + "ON si.\"tblIngredient2ID\" = i.\"ID\" "
                + "WHERE si.\"tblSupplier2ID\" = ? AND i.\"Name\" ILIKE ? ORDER BY i.\"Name\"";
        List<Ingredient2> ingredients = new ArrayList<Ingredient2>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);
            statement.setString(2, "%" + (name == null ? "" : name.trim()) + "%");
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ingredients.add(readIngredient(result));
                }
            }
        }
        return ingredients;
    }

    public List<Ingredient2> searchUnlinkedIngredients(String name, int supplierId) throws SQLException {
        String sql = "SELECT i.\"ID\", i.\"Name\", i.\"Unit\", i.\"CurrentAmount\", i.\"AveragePrice\" "
                + "FROM \"tblIngredient2\" i WHERE i.\"Name\" ILIKE ? "
                + "AND NOT EXISTS (SELECT 1 FROM \"tblSupplierIngredient2\" si "
                + "WHERE si.\"tblSupplier2ID\" = ? AND si.\"tblIngredient2ID\" = i.\"ID\") "
                + "ORDER BY i.\"Name\"";
        List<Ingredient2> ingredients = new ArrayList<Ingredient2>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + (name == null ? "" : name.trim()) + "%");
            statement.setInt(2, supplierId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ingredients.add(readIngredient(result));
                }
            }
        }
        return ingredients;
    }

    public Ingredient2 getIngredientForSupplier(int ingredientId, int supplierId) throws SQLException {
        String sql = "SELECT i.\"ID\", i.\"Name\", i.\"Unit\", i.\"CurrentAmount\", i.\"AveragePrice\" "
                + "FROM \"tblIngredient2\" i JOIN \"tblSupplierIngredient2\" si "
                + "ON si.\"tblIngredient2ID\" = i.\"ID\" "
                + "WHERE i.\"ID\" = ? AND si.\"tblSupplier2ID\" = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ingredientId);
            statement.setInt(2, supplierId);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readIngredient(result) : null;
            }
        }
    }

    public boolean addIngredient(Ingredient2 ingredient, int supplierId) throws SQLException {
        String sql = "INSERT INTO \"tblIngredient2\" "
                + "(\"Name\", \"Unit\", \"CurrentAmount\", \"AveragePrice\") VALUES (?, ?, ?, ?) RETURNING \"ID\"";
        String linkSql = "INSERT INTO \"tblSupplierIngredient2\" "
                + "(\"tblSupplier2ID\", \"tblIngredient2ID\") VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            try {
                statement.setString(1, ingredient.getName());
                statement.setString(2, ingredient.getUnit());
                statement.setBigDecimal(3, BigDecimal.ZERO);
                statement.setBigDecimal(4, BigDecimal.ZERO);
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        throw new SQLException("Cơ sở dữ liệu không tạo được nguyên liệu.");
                    }
                    ingredient.setId(result.getInt("ID"));
                }
                try (PreparedStatement linkStatement = connection.prepareStatement(linkSql)) {
                    linkStatement.setInt(1, supplierId);
                    linkStatement.setInt(2, ingredient.getId());
                    if (linkStatement.executeUpdate() != 1) {
                        throw new SQLException("Không liên kết được nguyên liệu với nhà cung cấp.");
                    }
                }
                connection.commit();
                ingredient.setCurrentAmount(BigDecimal.ZERO);
                ingredient.setAveragePrice(BigDecimal.ZERO);
                return true;
            } catch (SQLException | RuntimeException exception) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    exception.addSuppressed(rollbackException);
                }
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public boolean linkIngredientToSupplier(int ingredientId, int supplierId) throws SQLException {
        String sql = "INSERT INTO \"tblSupplierIngredient2\" "
                + "(\"tblSupplier2ID\", \"tblIngredient2ID\") "
                + "SELECT ?, ? WHERE EXISTS (SELECT 1 FROM \"tblSupplier2\" WHERE \"ID\" = ?) "
                + "AND EXISTS (SELECT 1 FROM \"tblIngredient2\" WHERE \"ID\" = ?) "
                + "AND NOT EXISTS (SELECT 1 FROM \"tblSupplierIngredient2\" "
                + "WHERE \"tblSupplier2ID\" = ? AND \"tblIngredient2ID\" = ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, supplierId);
            statement.setInt(2, ingredientId);
            statement.setInt(3, supplierId);
            statement.setInt(4, ingredientId);
            statement.setInt(5, supplierId);
            statement.setInt(6, ingredientId);
            return statement.executeUpdate() == 1;
        }
    }

    private Ingredient2 readIngredient(ResultSet result) throws SQLException {
        return new Ingredient2(result.getInt("ID"), result.getString("Name"), result.getString("Unit"),
                result.getBigDecimal("CurrentAmount"), result.getBigDecimal("AveragePrice"));
    }
}
