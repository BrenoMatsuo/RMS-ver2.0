<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Trang chủ quản lý</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container narrow">
    <header class="topbar"><strong>Quản lý nhà hàng</strong><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></header>
    <section class="panel">
        <h1>Xin chào, <c:out value="${sessionScope.staff.user.name}"/></h1>
        <p>Chọn chức năng quản lý.</p>
        <a class="button" href="${pageContext.request.contextPath}/management/dishes">Quản lý món ăn</a>
    </section>
</main>
</body>
</html>
