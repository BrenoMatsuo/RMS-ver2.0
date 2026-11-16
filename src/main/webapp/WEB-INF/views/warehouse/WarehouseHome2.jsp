<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Trang chủ nhân viên kho</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container narrow">
    <header class="topbar"><strong>Nhân viên kho</strong><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></header>
    <section class="panel">
        <h1>Xin chào, <c:out value="${sessionScope.staff.user.name}"/></h1>
        <p>Bắt đầu tạo phiếu nhập nguyên liệu.</p>
        <a class="button" href="${pageContext.request.contextPath}/warehouse/suppliers">Nhập nguyên liệu</a>
    </section>
</main>
</body>
</html>
