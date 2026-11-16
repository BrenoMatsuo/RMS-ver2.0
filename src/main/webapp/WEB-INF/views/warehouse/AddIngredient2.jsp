<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thêm nguyên liệu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container narrow">
    <header class="topbar"><a href="${pageContext.request.contextPath}/warehouse/home">Trang chủ kho</a><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></header>
    <h1>Thêm nguyên liệu</h1>
    <c:if test="${not empty error}"><p class="notice error"><c:out value="${error}"/></p></c:if>
    <form class="panel form-grid" method="post" action="${pageContext.request.contextPath}/warehouse/ingredients/add">
        <label class="full">Tên nguyên liệu<input name="name" maxlength="255" required value="<c:out value='${ingredient.name}'/>"></label>
        <label class="full">Đơn vị tính<input name="unit" maxlength="255" required placeholder="kg, lít, cái..." value="<c:out value='${ingredient.unit}'/>"></label>
        <div class="actions full"><button type="submit">Thêm</button><a class="button secondary" href="${pageContext.request.contextPath}/warehouse/ingredients">Quay lại</a></div>
    </form>
</main>
</body>
</html>
