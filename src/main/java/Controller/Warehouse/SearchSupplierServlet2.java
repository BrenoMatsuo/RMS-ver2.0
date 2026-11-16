package Controller.Warehouse;

import DAO.SupplierDAO2;
import Model.Supplier2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/warehouse/suppliers")
public class SearchSupplierServlet2 extends HttpServlet {
    private final SupplierDAO2 supplierDAO = new SupplierDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String selectId = request.getParameter("select");
        try {
            if (selectId != null) {
                int id = Integer.parseInt(selectId);
                Supplier2 supplier = supplierDAO.getSupplier(id);
                if (supplier == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy nhà cung cấp.");
                    return;
                }
                HttpSession session = request.getSession();
                Object linesValue = session.getAttribute("importLines");
                Supplier2 selectedSupplier = (Supplier2) session.getAttribute("importSupplier");
                if (linesValue instanceof java.util.List<?> && !((java.util.List<?>) linesValue).isEmpty()
                        && (selectedSupplier == null || selectedSupplier.getId() != supplier.getId())) {
                    response.sendRedirect(request.getContextPath() + "/warehouse/ingredients?supplierLocked=1");
                    return;
                }
                session.setAttribute("importSupplier", supplier);
                response.sendRedirect(request.getContextPath() + "/warehouse/ingredients");
                return;
            }
            String query = request.getParameter("q");
            request.setAttribute("query", query == null ? "" : query);
            request.setAttribute("suppliers", supplierDAO.searchSupplier(query));
            request.getRequestDispatcher("/WEB-INF/views/warehouse/SearchSupplier2.jsp").forward(request, response);
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã nhà cung cấp không hợp lệ.");
        } catch (SQLException exception) {
            throw new ServletException("Không thể tìm nhà cung cấp.", exception);
        }
    }
}
