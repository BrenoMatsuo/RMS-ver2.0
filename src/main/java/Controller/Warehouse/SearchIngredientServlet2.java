package Controller.Warehouse;

import DAO.IngredientDAO2;
import Model.ImportBillDetail2;
import Model.Supplier2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebServlet("/warehouse/ingredients")
public class SearchIngredientServlet2 extends HttpServlet {
    private final IngredientDAO2 ingredientDAO = new IngredientDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Supplier2 supplier = (Supplier2) session.getAttribute("importSupplier");
        if (supplier == null) {
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
            return;
        }
        try {
            String query = request.getParameter("q");
            String catalogQuery = request.getParameter("catalogQ");
            List<ImportBillDetail2> lines = getLines(session);
            BigDecimal total = lines.stream().map(ImportBillDetail2::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            request.setAttribute("query", query == null ? "" : query);
            request.setAttribute("catalogQuery", catalogQuery == null ? "" : catalogQuery);
            request.setAttribute("supplier", supplier);
            request.setAttribute("ingredients", ingredientDAO.searchIngredient(query, supplier.getId()));
            request.setAttribute("availableIngredients",
                    catalogQuery == null || catalogQuery.trim().isEmpty()
                            ? Collections.emptyList()
                            : ingredientDAO.searchUnlinkedIngredients(catalogQuery, supplier.getId()));
            request.setAttribute("importLines", lines);
            request.setAttribute("importTotal", total);
            request.getRequestDispatcher("/WEB-INF/views/warehouse/ImportIngredient2.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("Không thể tìm nguyên liệu.", exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object supplierValue = session.getAttribute("importSupplier");
        if (!(supplierValue instanceof Supplier2)) {
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
            return;
        }
        if (!"link".equals(request.getParameter("action"))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thao tác nguyên liệu không hợp lệ.");
            return;
        }
        try {
            int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
            if (ingredientId <= 0) {
                throw new NumberFormatException("ID must be positive");
            }
            Supplier2 supplier = (Supplier2) supplierValue;
            boolean linked = ingredientDAO.linkIngredientToSupplier(ingredientId, supplier.getId());
            String result = linked ? "linked=1" : "alreadyLinked=1";
            response.sendRedirect(request.getContextPath() + "/warehouse/ingredients?" + result);
        } catch (NumberFormatException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã nguyên liệu không hợp lệ.");
        } catch (SQLException exception) {
            throw new ServletException("Không thể liên kết nguyên liệu với nhà cung cấp.", exception);
        }
    }

    @SuppressWarnings("unchecked")
    static List<ImportBillDetail2> getLines(HttpSession session) {
        Object value = session.getAttribute("importLines");
        if (value instanceof List<?>) {
            return (List<ImportBillDetail2>) value;
        }
        List<ImportBillDetail2> lines = new ArrayList<ImportBillDetail2>();
        session.setAttribute("importLines", lines);
        return lines;
    }
}
