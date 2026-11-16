package Controller;

import DAO.UserDAO2;
import Model.ManagementStaff2;
import Model.Staff2;
import Model.User2;
import Model.WarehouseStaff2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginServlet2 extends HttpServlet {
    private final UserDAO2 userDAO = new UserDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if ("/logout".equals(request.getServletPath())) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/Login2.jsp?logout=1");
            return;
        }
        request.getRequestDispatcher("/Login2.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = value(request.getParameter("username"));
        String password = request.getParameter("password");
        password = password == null ? "" : password;
        if (username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập tên đăng nhập và mật khẩu.");
            request.getRequestDispatcher("/Login2.jsp").forward(request, response);
            return;
        }
        try {
            User2 user = userDAO.login(username, password);
            if (user == null) {
                request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng.");
                request.getRequestDispatcher("/Login2.jsp").forward(request, response);
                return;
            }
            UserDAO2.StaffRecord record = userDAO.findStaff(user.getId());
            if (record == null || user.getPosition() == null) {
                request.setAttribute("error", "Tài khoản không thuộc nhân viên của hệ thống.");
                request.getRequestDispatcher("/Login2.jsp").forward(request, response);
                return;
            }

            Staff2 staff;
            String home;
            if ("manager".equals(user.getPosition())) {
                staff = new ManagementStaff2(record.getUserId(), record.getRestaurantId(), user);
                home = "/management/home";
            } else if ("warehouse".equals(user.getPosition())) {
                staff = new WarehouseStaff2(record.getUserId(), record.getRestaurantId(), user);
                home = "/warehouse/home";
            } else {
                request.setAttribute("error", "Vai trò tài khoản không được hỗ trợ.");
                request.getRequestDispatcher("/Login2.jsp").forward(request, response);
                return;
            }
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            request.getSession(true).setAttribute("staff", staff);
            response.sendRedirect(request.getContextPath() + home);
        } catch (SQLException exception) {
            throw new ServletException("Không thể xác thực tài khoản. Kiểm tra cấu hình PostgreSQL.", exception);
        }
    }

    private String value(String input) {
        return input == null ? "" : input.trim();
    }
}
