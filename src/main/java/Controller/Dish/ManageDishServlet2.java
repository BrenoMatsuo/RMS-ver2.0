package Controller.Dish;

import DAO.DishDAO2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/management/dishes")
public class ManageDishServlet2 extends HttpServlet {
    private final DishDAO2 dishDAO = new DishDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("dishes", dishDAO.getAllDish());
            request.getRequestDispatcher("/WEB-INF/views/dish/ManageDish2.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("Không tải được danh sách món ăn.", exception);
        }
    }
}
