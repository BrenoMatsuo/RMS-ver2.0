package Controller.Warehouse;

import DAO.IngredientDAO2;
import Model.ImportBillDetail2;
import Model.Ingredient2;
import Model.Supplier2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/warehouse/import-lines")
public class ImportIngredientServlet2 extends HttpServlet {
    private final IngredientDAO2 ingredientDAO = new IngredientDAO2();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (!(session.getAttribute("importSupplier") instanceof Supplier2)) {
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
            return;
        }
        Supplier2 supplier = (Supplier2) session.getAttribute("importSupplier");
        String action = request.getParameter("action");
        if ("changeSupplier".equals(action)) {
            session.removeAttribute("importLines");
            session.removeAttribute("importSupplier");
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
            return;
        }
        if ("confirm".equals(action)) {
            @SuppressWarnings("unchecked")
            List<ImportBillDetail2> lines =
                    (List<ImportBillDetail2>) session.getAttribute("importLines");
            if (lines == null || lines.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/warehouse/ingredients");
            } else {
                response.sendRedirect(request.getContextPath() + "/warehouse/import-bill");
            }
            return;
        }
        try {
            List<ImportBillDetail2> lines =
                    SearchIngredientServlet2.getLines(session);
            if ("remove".equals(action)) {
                int lineId = Integer.parseInt(request.getParameter("lineId"));
                lines.removeIf(line -> line.getLineId() == lineId);
                response.sendRedirect(request.getContextPath() + "/warehouse/ingredients");
                return;
            }
            if (!"add".equals(action)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thao tác nguyên liệu không hợp lệ.");
                return;
            }

            int ingredientId = Integer.parseInt(request.getParameter("ingredientId"));
            BigDecimal quantity = new BigDecimal(request.getParameter("quantity"));
            BigDecimal importPrice = new BigDecimal(request.getParameter("importPrice"));
            if (quantity.signum() <= 0 || importPrice.signum() <= 0) {
                throw new IllegalArgumentException("Số lượng và đơn giá phải lớn hơn 0.");
            }
            Ingredient2 ingredient = ingredientDAO.getIngredientForSupplier(ingredientId, supplier.getId());
            if (ingredient == null) {
                throw new IllegalArgumentException("Nguyên liệu này chưa được liên kết với nhà cung cấp đã chọn.");
            }

            ImportBillDetail2 existing = lines.stream()
                    .filter(line -> line.getIngredient().getId() == ingredientId).findFirst().orElse(null);
            if (existing == null) {
                ImportBillDetail2 line = new ImportBillDetail2();
                line.setLineId(ingredientId);
                line.setIngredient(ingredient);
                line.setQuantity(quantity);
                line.setImportPrice(importPrice);
                line.recalculateTotal();
                lines.add(line);
            } else {
                BigDecimal oldValue = existing.getTotal();
                BigDecimal addedValue = quantity.multiply(importPrice);
                BigDecimal newQuantity = existing.getQuantity().add(quantity);
                existing.setQuantity(newQuantity);
                existing.setImportPrice(oldValue.add(addedValue).divide(newQuantity, 12, RoundingMode.HALF_UP));
                existing.setTotal(oldValue.add(addedValue));
            }
            response.sendRedirect(request.getContextPath() + "/warehouse/ingredients");
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage() == null
                    ? "Số lượng, đơn giá hoặc mã nguyên liệu không hợp lệ." : exception.getMessage());
            showIngredientPage(request, response);
        } catch (SQLException exception) {
            throw new ServletException("Không thể thêm nguyên liệu vào phiếu.", exception);
        }
    }

    private void showIngredientPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        try {
            List<ImportBillDetail2> lines = SearchIngredientServlet2.getLines(session);
            request.setAttribute("query", "");
            request.setAttribute("supplier", session.getAttribute("importSupplier"));
            Supplier2 supplier = (Supplier2) session.getAttribute("importSupplier");
            request.setAttribute("ingredients", ingredientDAO.searchIngredient("", supplier.getId()));
            request.setAttribute("importLines", lines);
            request.setAttribute("importTotal", lines.stream().map(ImportBillDetail2::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            request.getRequestDispatcher("/WEB-INF/views/warehouse/ImportIngredient2.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException("Không thể tải lại danh sách nguyên liệu.", exception);
        }
    }
}
