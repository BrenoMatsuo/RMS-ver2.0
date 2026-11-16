package Controller.Warehouse;

import DAO.IngredientDAO2;
import Model.Ingredient2;
import Model.Supplier2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/warehouse/ingredients/add")
public class AddIngredientServlet2 extends HttpServlet {
    private final IngredientDAO2 ingredientDAO = new IngredientDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!(request.getSession().getAttribute("importSupplier") instanceof Supplier2)) {
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/warehouse/AddIngredient2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Object supplierValue = request.getSession().getAttribute("importSupplier");
        if (!(supplierValue instanceof Supplier2)) {
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
            return;
        }
        Supplier2 supplier = (Supplier2) supplierValue;
        Ingredient2 ingredient = new Ingredient2();
        ingredient.setName(value(request.getParameter("name")));
        ingredient.setUnit(value(request.getParameter("unit")));
        if (ingredient.getName().isEmpty() || ingredient.getUnit().isEmpty()) {
            request.setAttribute("ingredient", ingredient);
            request.setAttribute("error", "Tên và đơn vị nguyên liệu không được để trống.");
            request.getRequestDispatcher("/WEB-INF/views/warehouse/AddIngredient2.jsp").forward(request, response);
            return;
        }
        try {
            if (!ingredientDAO.addIngredient(ingredient, supplier.getId())) {
                throw new SQLException("Cơ sở dữ liệu không thêm được nguyên liệu.");
            }
            response.sendRedirect(request.getContextPath() + "/warehouse/ingredients?added=1");
        } catch (SQLException exception) {
            throw new ServletException("Không thể thêm nguyên liệu.", exception);
        }
    }

    private String value(String input) {
        return input == null ? "" : input.trim();
    }
}
