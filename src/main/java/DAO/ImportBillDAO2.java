package DAO;

import Model.ImportBill2;
import Model.ImportBillDetail2;
import Model.Ingredient2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImportBillDAO2 extends DAO2 {
    public boolean saveImportBill(ImportBill2 importBill) throws SQLException {
        if (importBill == null || importBill.getSupplier() == null || importBill.getStaff() == null
                || importBill.getDetails() == null || importBill.getDetails().isEmpty()) {
            throw new SQLException("Phiếu nhập phải có nhà cung cấp, nhân viên và ít nhất một dòng nguyên liệu.");
        }

        BigDecimal grandTotal = BigDecimal.ZERO;
        for (ImportBillDetail2 detail : importBill.getDetails()) {
            if (detail.getIngredient() == null || detail.getIngredient().getId() <= 0
                    || detail.getQuantity() == null || detail.getImportPrice() == null
                    || detail.getTotal() == null
                    || detail.getQuantity().signum() <= 0 || detail.getImportPrice().signum() <= 0
                    || detail.getTotal().signum() <= 0) {
                throw new SQLException("Dòng nguyên liệu không hợp lệ.");
            }
            grandTotal = grandTotal.add(detail.getTotal());
        }
        importBill.setTotalAmount(grandTotal);

        String insertBill = "INSERT INTO \"tblImportBill2\" "
                + "(\"ImportDate\", \"TotalAmount\", \"PayMethod\", \"State\", \"Note\", "
                + "\"tblSupplier2ID\", \"tblStaff2tblUser2ID\") VALUES (CURRENT_DATE, ?, ?, ?, ?, ?, ?) "
                + "RETURNING \"ID\"";
        String insertDetail = "INSERT INTO \"tblImportBillDetail2\" "
                + "(\"Quantity\", \"ImportPrice\", \"Total\", \"tblImportBill2ID\", \"tblIngredient2ID\") "
                + "VALUES (?, ?, ?, ?, ?)";
        String lockIngredient = "SELECT \"CurrentAmount\", \"AveragePrice\" FROM \"tblIngredient2\" "
                + "WHERE \"ID\" = ? FOR UPDATE";
        String updateIngredient = "UPDATE \"tblIngredient2\" SET \"CurrentAmount\" = ?, \"AveragePrice\" = ? "
                + "WHERE \"ID\" = ?";

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try {
                int billId;
                try (PreparedStatement statement = connection.prepareStatement(insertBill)) {
                    statement.setBigDecimal(1, grandTotal);
                    statement.setString(2, importBill.getPayMethod());
                    statement.setString(3, "da thanh toan");
                    statement.setString(4, importBill.getNote());
                    statement.setInt(5, importBill.getSupplier().getId());
                    statement.setInt(6, importBill.getStaff().getId());
                    try (ResultSet keys = statement.executeQuery()) {
                        if (!keys.next()) {
                            throw new SQLException("Không lấy được mã phiếu nhập vừa tạo.");
                        }
                        billId = keys.getInt(1);
                    }
                }

                for (ImportBillDetail2 detail : importBill.getDetails()) {
                    Ingredient2 ingredient = detail.getIngredient();
                    BigDecimal oldAmount;
                    BigDecimal oldAveragePrice;
                    try (PreparedStatement statement = connection.prepareStatement(lockIngredient)) {
                        statement.setInt(1, ingredient.getId());
                        try (ResultSet result = statement.executeQuery()) {
                            if (!result.next()) {
                                throw new SQLException("Không tìm thấy nguyên liệu mã " + ingredient.getId());
                            }
                            oldAmount = result.getBigDecimal("CurrentAmount");
                            oldAveragePrice = result.getBigDecimal("AveragePrice");
                        }
                    }
                    BigDecimal newAmount = oldAmount.add(detail.getQuantity());
                    BigDecimal weightedCost = oldAmount.multiply(oldAveragePrice)
                            .add(detail.getQuantity().multiply(detail.getImportPrice()));
                    BigDecimal newAveragePrice = newAmount.signum() == 0 ? oldAveragePrice
                            : weightedCost.divide(newAmount, 4, RoundingMode.HALF_UP);

                    try (PreparedStatement statement = connection.prepareStatement(insertDetail)) {
                        statement.setBigDecimal(1, detail.getQuantity());
                        statement.setBigDecimal(2, detail.getImportPrice());
                        statement.setBigDecimal(3, detail.getTotal());
                        statement.setInt(4, billId);
                        statement.setInt(5, ingredient.getId());
                        statement.executeUpdate();
                    }
                    try (PreparedStatement statement = connection.prepareStatement(updateIngredient)) {
                        statement.setBigDecimal(1, newAmount);
                        statement.setBigDecimal(2, newAveragePrice);
                        statement.setInt(3, ingredient.getId());
                        if (statement.executeUpdate() != 1) {
                            throw new SQLException("Không cập nhật được tồn kho nguyên liệu mã " + ingredient.getId());
                        }
                    }
                }
                connection.commit();
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
}
