<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thêm nhà cung cấp</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container narrow">
    <header class="topbar"><a href="${pageContext.request.contextPath}/warehouse/home">Trang chủ kho</a><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></header>
    <h1>Thêm nhà cung cấp</h1>
    <c:if test="${not empty error}"><p class="notice error"><c:out value="${error}"/></p></c:if>
    <form class="panel form-grid" method="post" action="${pageContext.request.contextPath}/warehouse/suppliers/add">
        <label class="full">Tên nhà cung cấp<input name="name" maxlength="255" required value="<c:out value='${supplier.name}'/>"></label>
        <label>Điện thoại<input name="phoneNumber" maxlength="15" value="<c:out value='${supplier.phoneNumber}'/>"></label>
        <label>Email<input name="email" type="email" maxlength="255" value="<c:out value='${supplier.email}'/>"></label>
        <label class="full">Địa chỉ<input name="address" maxlength="255" value="<c:out value='${supplier.address}'/>"></label>
        <label class="full">Mô tả<textarea name="description" rows="3" maxlength="255"><c:out value="${supplier.description}"/></textarea></label>
        <div class="actions full"><button type="submit">Thêm</button><a class="button secondary" href="${pageContext.request.contextPath}/warehouse/suppliers">Quay lại</a></div>
    </form>
</main>
</body>
</html>
