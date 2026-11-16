<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Quản lý món ăn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container">
    <header class="topbar">
        <a href="${pageContext.request.contextPath}/management/home">Trang chủ quản lý</a>
        <span><c:out value="${sessionScope.staff.user.name}"/> | <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></span>
    </header>
    <header class="page-header">
        <div><h1>Quản lý món ăn</h1><p>Danh sách món trong nhà hàng</p></div>
        <a class="button" href="${pageContext.request.contextPath}/management/dishes/add">Thêm món</a>
    </header>
    <c:if test="${param.added == '1'}"><p class="notice success">Thêm món ăn thành công.</p></c:if>
    <section class="panel">
        <c:choose>
            <c:when test="${empty dishes}"><p>Chưa có món ăn nào.</p></c:when>
            <c:otherwise>
                <div class="table-wrap"><table>
                    <thead><tr><th>Ảnh</th><th>Tên món</th><th>Danh mục</th><th>Giá</th><th>Trạng thái</th><th>Mô tả</th></tr></thead>
                    <tbody><c:forEach var="dish" items="${dishes}">
                        <tr>
                            <td>
                                <c:if test="${not empty dish.picture}">
                                    <c:choose>
                                        <c:when test="${fn:startsWith(fn:toLowerCase(dish.picture), 'http://') or fn:startsWith(fn:toLowerCase(dish.picture), 'https://')}">
                                            <img class="dish-thumbnail" src="<c:out value='${dish.picture}'/>" alt="<c:out value='${dish.name}'/>">
                                        </c:when>
                                        <c:when test="${fn:startsWith(dish.picture, '//')}">
                                            <img class="dish-thumbnail" src="<c:out value='${dish.picture}'/>" alt="<c:out value='${dish.name}'/>">
                                        </c:when>
                                        <c:when test="${fn:startsWith(dish.picture, '/')}">
                                            <img class="dish-thumbnail" src="${pageContext.request.contextPath}<c:out value='${dish.picture}'/>" alt="<c:out value='${dish.name}'/>">
                                        </c:when>
                                        <c:otherwise>
                                            <img class="dish-thumbnail" src="${pageContext.request.contextPath}/<c:out value='${dish.picture}'/>" alt="<c:out value='${dish.name}'/>">
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <c:if test="${empty dish.picture}">Chưa có ảnh</c:if>
                            </td>
                            <td><c:out value="${dish.name}"/></td>
                            <td><c:out value="${dish.category.name}"/></td>
                            <td><fmt:formatNumber value="${dish.price}" type="number" groupingUsed="true" maxFractionDigits="2"/></td>
                            <td><c:out value="${dish.state}"/></td>
                            <td><c:out value="${dish.description}"/></td>
                        </tr>
                    </c:forEach></tbody>
                </table></div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
