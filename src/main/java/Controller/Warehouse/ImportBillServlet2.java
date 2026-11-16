package Controller.Warehouse;

import DAO.ImportBillDAO2;
import DAO.SupplierDAO2;
import Model.ImportBill2;
import Model.ImportBillDetail2;
import Model.Staff2;
import Model.Supplier2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/warehouse/import-bill")
public class ImportBillServlet2 extends HttpServlet {
    private final ImportBillDAO2 importBillDAO = new ImportBillDAO2();
    private final SupplierDAO2 supplierDAO = new SupplierDAO2();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if ("1".equals(request.getParameter("paid"))) {
            Object paid = session.getAttribute("paidInvoice");
            if (paid instanceof ImportBill2) {
                request.setAttribute("importBill", paid);
                request.setAttribute("paid", Boolean.TRUE);
                session.removeAttribute("paidInvoice");
                request.getRequestDispatcher("/WEB-INF/views/warehouse/Invoice2.jsp").forward(request, response);
                return;
            }
        }
        ImportBill2 bill = createBill(session, request, response);
        if (bill != null) {
            request.setAttribute("importBill", bill);
            request.getRequestDispatcher("/WEB-INF/views/warehouse/Invoice2.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        synchronized (session) {
            ImportBill2 bill = createBill(session, request, response);
            if (bill == null) {
                return;
            }
            bill.setPayMethod(value(request.getParameter("payMethod")));
            bill.setNote(value(request.getParameter("note")));
            if (bill.getPayMethod().isEmpty()) {
                request.setAttribute("error", "Vui lòng chọn phương thức thanh toán.");
                request.setAttribute("importBill", bill);
                request.getRequestDispatcher("/WEB-INF/views/warehouse/Invoice2.jsp").forward(request, response);
                return;
            }
            try {
                if (!importBillDAO.saveImportBill(bill)) {
                    throw new SQLException("Không lưu được phiếu nhập.");
                }
                session.removeAttribute("importLines");
                session.removeAttribute("importSupplier");
                session.setAttribute("paidInvoice", bill);
                response.sendRedirect(request.getContextPath() + "/warehouse/import-bill?paid=1");
            } catch (SQLException exception) {
                request.setAttribute("error", "Thanh toán thất bại: " + exception.getMessage());
                request.setAttribute("importBill", bill);
                request.getRequestDispatcher("/WEB-INF/views/warehouse/Invoice2.jsp").forward(request, response);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private ImportBill2 createBill(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Object supplierValue = session.getAttribute("importSupplier");
        Object linesValue = session.getAttribute("importLines");
        Object staffValue = session.getAttribute("staff");
        if (!(supplierValue instanceof Supplier2) || !(linesValue instanceof List<?>)
                || ((List<?>) linesValue).isEmpty() || !(staffValue instanceof Staff2)) {
            response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
            return null;
        }
        Supplier2 supplier = (Supplier2) supplierValue;
        try {
            Supplier2 currentSupplier = supplierDAO.getSupplier(supplier.getId());
            if (currentSupplier == null) {
                session.removeAttribute("importSupplier");
                session.removeAttribute("importLines");
                response.sendRedirect(request.getContextPath() + "/warehouse/suppliers");
                return null;
            }
            ImportBill2 bill = new ImportBill2();
            bill.setImportDate(Date.valueOf(LocalDate.now()));
            bill.setSupplier(currentSupplier);
            bill.setStaff((Staff2) staffValue);
            bill.setDetails((List<ImportBillDetail2>) linesValue);
            BigDecimal total = bill.getDetails().stream().map(ImportBillDetail2::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            bill.setTotalAmount(total);
            bill.setState("da thanh toan");
            return bill;
        } catch (SQLException exception) {
            throw new ServletException("Không tải được thông tin nhà cung cấp.", exception);
        }
    }

    private String value(String input) {
        return input == null ? "" : input.trim();
    }
}
