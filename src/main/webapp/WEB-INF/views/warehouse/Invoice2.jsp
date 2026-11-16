<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hóa đơn nhập nguyên liệu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container">
    <header class="topbar no-print">
        <a href="${pageContext.request.contextPath}/warehouse/home">Trang chủ kho</a>
        <span><c:out value="${sessionScope.staff.user.name}"/> | <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></span>
    </header>
    <section class="invoice panel">
        <h1 class="center">HÓA ĐƠN NHẬP NGUYÊN LIỆU</h1>
        <p>Nhà cung cấp: <strong><c:out value="${importBill.supplier.name}"/></strong></p>
        <p>Nhân viên kho: <strong><c:out value="${importBill.staff.user.name}"/></strong></p>
        <p>Ngày lập: <c:out value="${importBill.importDate}"/></p>
        <p>Trạng thái: <c:out value="${importBill.state}"/></p>
        <c:if test="${not empty importBill.payMethod}"><p>Phương thức thanh toán: <c:out value="${importBill.payMethod}"/></p></c:if>
        <div class="table-wrap"><table>
            <thead><tr><th>Nguyên liệu</th><th>Đơn vị</th><th>Số lượng</th><th>Đơn giá nhập</th><th>Thành tiền</th></tr></thead>
            <tbody><c:forEach var="line" items="${importBill.details}">
                <tr>
                    <td><c:out value="${line.ingredient.name}"/></td>
                    <td><c:out value="${line.ingredient.unit}"/></td>
                    <td><c:out value="${line.quantity}"/></td>
                    <td><fmt:formatNumber value="${line.importPrice}" type="number" groupingUsed="true" maxFractionDigits="2"/></td>
                    <td><fmt:formatNumber value="${line.total}" type="number" groupingUsed="true" maxFractionDigits="2"/></td>
                </tr>
            </c:forEach></tbody>
            <tfoot><tr><th colspan="4">Tổng cộng</th><th><fmt:formatNumber value="${importBill.totalAmount}" type="number" groupingUsed="true" maxFractionDigits="2"/></th></tr></tfoot>
        </table></div>
        <c:if test="${not empty importBill.note}"><p>Ghi chú: <c:out value="${importBill.note}"/></p></c:if>
    </section>
    <c:if test="${not empty error}"><p class="notice error no-print"><c:out value="${error}"/></p></c:if>
    <c:if test="${paid}"><p class="notice success">Thanh toán và lưu phiếu nhập thành công.</p></c:if>
    <p id="print-message" class="notice success hidden no-print">In hóa đơn thành công.</p>
    <div class="actions no-print">
        <button type="button" data-print-invoice>In hóa đơn</button>
    </div>
    <c:choose>
        <c:when test="${paid}">
            <a class="button no-print" href="${pageContext.request.contextPath}/warehouse/home">Về trang chủ</a>
        </c:when>
        <c:otherwise>
            <form class="actions no-print" method="post" action="${pageContext.request.contextPath}/warehouse/import-bill">
                <label>Phương thức thanh toán
                    <select name="payMethod" required>
                        <option value="">-- Chọn phương thức --</option>
                        <option value="Cash">Tiền mặt</option>
                        <option value="Bank transfer">Chuyển khoản</option>
                    </select>
                </label>
                <label>Ghi chú<input name="note" maxlength="255" value="<c:out value='${importBill.note}'/>"></label>
                <button type="submit">Thanh toán cho nhà cung cấp</button>
            </form>
            <div class="actions no-print">
                <a class="button secondary" href="${pageContext.request.contextPath}/warehouse/ingredients">Quay lại danh sách nhập</a>
            </div>
        </c:otherwise>
    </c:choose>
</main>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
