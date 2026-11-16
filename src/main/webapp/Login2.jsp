<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Đăng nhập - Quản lý nhà hàng</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body class="login-page">
<main class="login-card">
    <h1>Quản lý nhà hàng</h1>
    <p>Đăng nhập bằng tài khoản nhân viên quản lý hoặc nhân viên kho.</p>
    <c:if test="${not empty error}"><p class="notice error"><c:out value="${error}"/></p></c:if>
    <c:if test="${param.logout == '1'}"><p class="notice success">Bạn đã đăng xuất.</p></c:if>
    <c:if test="${param.required == '1'}"><p class="notice">Vui lòng đăng nhập để tiếp tục.</p></c:if>
    <form method="post" action="${pageContext.request.contextPath}/login" class="form-grid">
        <label>Tên đăng nhập
            <input name="username" autocomplete="username" required value="<c:out value='${param.username}'/>">
        </label>
        <label>Mật khẩu
            <input name="password" type="password" autocomplete="current-password" required>
        </label>
        <button type="submit">Đăng nhập</button>
    </form>
</main>
</body>
</html>
