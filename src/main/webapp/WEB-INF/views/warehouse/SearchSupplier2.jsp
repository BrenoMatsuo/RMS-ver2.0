<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Chọn nhà cung cấp</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container">
    <header class="topbar"><a href="${pageContext.request.contextPath}/warehouse/home">Trang chủ kho</a><span><c:out value="${sessionScope.staff.user.name}"/> | <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></span></header>
    <header class="page-header"><div><h1>Chọn nhà cung cấp</h1></div><a class="button secondary" href="${pageContext.request.contextPath}/warehouse/suppliers/add">Thêm nhà cung cấp mới</a></header>
    <c:if test="${param.added == '1'}"><p class="notice success">Đã thêm nhà cung cấp thành công.</p></c:if>
    <form class="search-bar panel" method="get" action="${pageContext.request.contextPath}/warehouse/suppliers">
        <input name="q" placeholder="Tên nhà cung cấp" value="<c:out value='${query}'/>">
        <button type="submit">Tìm kiếm</button>
    </form>
    <section class="panel">
        <c:choose>
            <c:when test="${empty suppliers}"><p>Không tìm thấy nhà cung cấp.</p></c:when>
            <c:otherwise><div class="table-wrap"><table>
                <thead><tr><th>Tên</th><th>Điện thoại</th><th>Email</th><th>Địa chỉ</th><th></th></tr></thead>
                <tbody><c:forEach var="supplier" items="${suppliers}">
                    <tr>
                        <td><c:out value="${supplier.name}"/></td>
                        <td><c:out value="${supplier.phoneNumber}"/></td>
                        <td><c:out value="${supplier.email}"/></td>
                        <td><c:out value="${supplier.address}"/></td>
                        <td><a class="button small" href="${pageContext.request.contextPath}/warehouse/suppliers?select=${supplier.id}">Chọn</a></td>
                    </tr>
                </c:forEach></tbody>
            </table></div></c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
