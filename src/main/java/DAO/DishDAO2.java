package DAO;

import Model.Category2;
import Model.Dish2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishDAO2 extends DAO2 {
    public List<Dish2> getAllDish() throws SQLException {
        String sql = "SELECT d.\"ID\" AS \"dishId\", d.\"Name\" AS \"dishName\", d.\"Price\", "
                + "d.\"Picture\", d.\"State\", d.\"Description\", c.\"ID\" AS \"categoryId\", "
                + "c.\"Name\" AS \"categoryName\", c.\"Description\" AS \"categoryDescription\" "
                + "FROM \"tblDish2\" d JOIN \"tblCategory2\" c ON c.\"ID\" = d.\"tblCategory2ID\" "
                + "ORDER BY d.\"Name\"";
        List<Dish2> dishes = new ArrayList<Dish2>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Category2 category = new Category2(result.getInt("categoryId"),
                        result.getString("categoryName"), result.getString("categoryDescription"));
                dishes.add(new Dish2(result.getInt("dishId"), result.getString("dishName"),
                        result.getBigDecimal("Price"), result.getString("Picture"), result.getString("State"),
                        result.getString("Description"), category));
            }
        }
        return dishes;
    }

    public boolean addDish(Dish2 dish) throws SQLException {
        String sql = "INSERT INTO \"tblDish2\" "
                + "(\"Name\", \"Price\", \"Picture\", \"State\", \"Description\", \"tblCategory2ID\") "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING \"ID\"";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, dish.getName());
            statement.setBigDecimal(2, dish.getPrice());
            statement.setString(3, dish.getPicture());
            statement.setString(4, dish.getState());
            statement.setString(5, dish.getDescription());
            statement.setInt(6, dish.getCategory().getId());
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return false;
                }
                dish.setId(result.getInt("ID"));
                return true;
            }
        }
    }
}
