<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN"/>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chọn nguyên liệu nhập kho</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container">
    <header class="topbar"><a href="${pageContext.request.contextPath}/warehouse/home">Trang chủ kho</a><span><c:out value="${sessionScope.staff.user.name}"/> | <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></span></header>
    <header class="page-header">
        <div><h1>Nhập nguyên liệu</h1><p>Nhà cung cấp: <strong><c:out value="${supplier.name}"/></strong></p></div>
        <form method="post" action="${pageContext.request.contextPath}/warehouse/import-lines"
              data-confirm="Đổi nhà cung cấp sẽ xóa danh sách nguyên liệu đang nhập. Bạn có muốn tiếp tục không?">
            <input type="hidden" name="action" value="changeSupplier">
            <button class="secondary" type="submit">Bỏ phiếu nháp và đổi nhà cung cấp</button>
        </form>
    </header>
    <c:if test="${param.supplierLocked == '1'}">
        <p class="notice error">Nhà cung cấp của phiếu nháp đang được giữ nguyên. Hãy xác nhận hoặc bỏ phiếu nháp trước khi đổi.</p>
    </c:if>
    <c:if test="${param.added == '1'}"><p class="notice success">Đã thêm nguyên liệu thành công.</p></c:if>
    <c:if test="${param.linked == '1'}"><p class="notice success">Đã thêm nguyên liệu có sẵn vào danh sách nhà cung cấp.</p></c:if>
    <c:if test="${param.alreadyLinked == '1'}"><p class="notice">Nguyên liệu này đã có trong danh sách nhà cung cấp.</p></c:if>
    <c:if test="${not empty error}"><p class="notice error"><c:out value="${error}"/></p></c:if>
    <form class="search-bar panel" method="get" action="${pageContext.request.contextPath}/warehouse/ingredients">
        <input name="q" placeholder="Tên nguyên liệu" value="<c:out value='${query}'/>">
        <button type="submit">Tìm kiếm</button>
        <a class="button secondary" href="${pageContext.request.contextPath}/warehouse/ingredients/add">Thêm nguyên liệu mới</a>
    </form>
    <section class="panel">
        <c:choose>
            <c:when test="${empty ingredients}"><p>Không tìm thấy nguyên liệu.</p></c:when>
            <c:otherwise><div class="table-wrap"><table>
                <thead><tr><th>Nguyên liệu</th><th>Đơn vị</th><th>Tồn hiện tại</th><th>Chọn và nhập số lượng</th></tr></thead>
                <tbody><c:forEach var="ingredient" items="${ingredients}">
                    <tr>
                        <td><c:out value="${ingredient.name}"/></td>
                        <td><c:out value="${ingredient.unit}"/></td>
                        <td><c:out value="${ingredient.currentAmount}"/></td>
                        <td><form class="inline-form" method="post" action="${pageContext.request.contextPath}/warehouse/import-lines">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="ingredientId" value="${ingredient.id}">
                            <input aria-label="Số lượng" name="quantity" type="number" min="0.001" step="0.001" placeholder="Số lượng" required>
                            <input aria-label="Đơn giá nhập" name="importPrice" type="number" min="0.01" step="0.01" placeholder="Đơn giá" required>
                            <button type="submit">Thêm vào danh sách</button>
                        </form></td>
                    </tr>
                </c:forEach></tbody>
            </table></div></c:otherwise>
        </c:choose>
    </section>
    <section class="panel">
        <h2>Liên kết nguyên liệu đã có</h2>
        <p>Chỉ nguyên liệu đã liên kết với nhà cung cấp này mới xuất hiện trong kết quả nhập ở trên.</p>
        <form class="search-bar" method="get" action="${pageContext.request.contextPath}/warehouse/ingredients">
            <input type="hidden" name="q" value="<c:out value='${query}'/>">
            <input name="catalogQ" placeholder="Tìm trong danh mục nguyên liệu" value="<c:out value='${catalogQuery}'/>">
            <button type="submit">Tìm để liên kết</button>
        </form>
        <c:choose>
            <c:when test="${empty catalogQuery}"><p>Nhập tên để tìm nguyên liệu chưa liên kết với nhà cung cấp này.</p></c:when>
            <c:when test="${empty availableIngredients}"><p>Không tìm thấy nguyên liệu phù hợp chưa được liên kết.</p></c:when>
            <c:otherwise>
                <div class="table-wrap"><table>
                    <thead><tr><th>Nguyên liệu</th><th>Đơn vị</th><th></th></tr></thead>
                    <tbody><c:forEach var="availableIngredient" items="${availableIngredients}">
                        <tr>
                            <td><c:out value="${availableIngredient.name}"/></td>
                            <td><c:out value="${availableIngredient.unit}"/></td>
                            <td><form method="post" action="${pageContext.request.contextPath}/warehouse/ingredients">
                                <input type="hidden" name="action" value="link">
                                <input type="hidden" name="ingredientId" value="${availableIngredient.id}">
                                <button class="secondary small" type="submit">Liên kết với nhà cung cấp</button>
                            </form></td>
                        </tr>
                    </c:forEach></tbody>
                </table></div>
            </c:otherwise>
        </c:choose>
    </section>
    <section class="panel">
        <h2>Danh sách nhập</h2>
        <c:choose>
            <c:when test="${empty importLines}"><p>Chưa chọn nguyên liệu.</p></c:when>
            <c:otherwise>
                <div class="table-wrap"><table>
                    <thead><tr><th>Nguyên liệu</th><th>Đơn vị</th><th>Số lượng</th><th>Đơn giá nhập</th><th>Thành tiền</th><th></th></tr></thead>
                    <tbody><c:forEach var="line" items="${importLines}">
                        <tr>
                            <td><c:out value="${line.ingredient.name}"/></td>
                            <td><c:out value="${line.ingredient.unit}"/></td>
                            <td><c:out value="${line.quantity}"/></td>
                            <td><fmt:formatNumber value="${line.importPrice}" type="number" groupingUsed="true" maxFractionDigits="2"/></td>
                            <td><fmt:formatNumber value="${line.total}" type="number" groupingUsed="true" maxFractionDigits="2"/></td>
                            <td><form method="post" action="${pageContext.request.contextPath}/warehouse/import-lines">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="lineId" value="${line.lineId}">
                                <button class="danger small" type="submit">Xóa</button>
                            </form></td>
                        </tr>
                    </c:forEach></tbody>
                    <tfoot><tr><th colspan="4">Tổng tiền</th><th><fmt:formatNumber value="${importTotal}" type="number" groupingUsed="true" maxFractionDigits="2"/></th><th></th></tr></tfoot>
                </table></div>
            </c:otherwise>
        </c:choose>
        <form class="actions" method="post" action="${pageContext.request.contextPath}/warehouse/import-lines">
            <input type="hidden" name="action" value="confirm">
            <button type="submit" <c:if test="${empty importLines}">disabled</c:if>>Xác nhận</button>
        </form>
    </section>
</main>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
