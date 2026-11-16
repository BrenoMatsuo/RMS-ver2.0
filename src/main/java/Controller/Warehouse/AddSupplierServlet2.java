package Controller.Warehouse;

import DAO.SupplierDAO2;
import Model.Supplier2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/warehouse/suppliers/add")
public class AddSupplierServlet2 extends HttpServlet {
    private final SupplierDAO2 supplierDAO = new SupplierDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/warehouse/AddSupplier2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Supplier2 supplier = new Supplier2();
        supplier.setName(value(request.getParameter("name")));
        supplier.setAddress(value(request.getParameter("address")));
        supplier.setPhoneNumber(value(request.getParameter("phoneNumber")));
        supplier.setEmail(value(request.getParameter("email")));
        supplier.setDescription(value(request.getParameter("description")));
        if (supplier.getName().isEmpty()) {
            request.setAttribute("supplier", supplier);
            request.setAttribute("error", "Tên nhà cung cấp không được để trống.");
            request.getRequestDispatcher("/WEB-INF/views/warehouse/AddSupplier2.jsp").forward(request, response);
            return;
        }
        try {
            if (!supplierDAO.addSupplier(supplier)) {
                throw new SQLException("Cơ sở dữ liệu không thêm được nhà cung cấp.");
            }
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers?added=1");
        } catch (SQLException exception) {
            throw new ServletException("Không thể thêm nhà cung cấp.", exception);
        }
    }

    private String value(String input) {
        return input == null ? "" : input.trim();
    }
}
