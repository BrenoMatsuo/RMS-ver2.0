package DAO;

import Model.Supplier2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO2 extends DAO2 {
    public List<Supplier2> searchSupplier(String name) throws SQLException {
        String sql = "SELECT * FROM \"tblSupplier2\" WHERE \"Name\" ILIKE ?";
        List<Supplier2> suppliers = new ArrayList<Supplier2>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + (name == null ? "" : name.trim()) + "%");
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    suppliers.add(readSupplier(result));
                }
            }
        }
        return suppliers;
    }

    public Supplier2 getSupplier(int id) throws SQLException {
        String sql = "SELECT \"ID\", \"Name\", \"Address\", \"PhoneNumber\", \"Email\", \"Description\" "
                + "FROM \"tblSupplier2\" WHERE \"ID\" = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readSupplier(result) : null;
            }
        }
    }

    public boolean addSupplier(Supplier2 supplier) throws SQLException {
        String sql = "INSERT INTO \"tblSupplier2\" "
                + "(\"Name\", \"Address\", \"PhoneNumber\", \"Email\", \"Description\") "
                + "VALUES (?, ?, ?, ?, ?) RETURNING \"ID\"";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getAddress());
            statement.setString(3, supplier.getPhoneNumber());
            statement.setString(4, supplier.getEmail());
            statement.setString(5, supplier.getDescription());
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return false;
                }
                supplier.setId(result.getInt("ID"));
                return true;
            }
        }
    }

    private Supplier2 readSupplier(ResultSet result) throws SQLException {
        return new Supplier2(result.getInt("ID"), result.getString("Name"), result.getString("Address"),
                result.getString("PhoneNumber"), result.getString("Email"), result.getString("Description"));
    }
}
