<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Thêm món ăn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<main class="container narrow">
    <header class="topbar"><a href="${pageContext.request.contextPath}/management/home">Trang chủ quản lý</a><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></header>
    <header class="page-header"><div><h1>Thêm món ăn mới</h1></div></header>
    <c:if test="${not empty error}"><p class="notice error"><c:out value="${error}"/></p></c:if>
    <form class="panel form-grid" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/management/dishes/add">
        <label class="full">Tên món
            <input name="name" maxlength="255" required value="<c:out value='${dish.name}'/>">
        </label>
        <label>Giá
            <input name="price" type="number" min="0.01" step="0.01" required value="<c:out value='${dish.price}'/>">
        </label>
        <label>Danh mục
            <select name="categoryId" required>
                <option value="">-- Chọn danh mục --</option>
                <c:forEach var="category" items="${categories}">
                    <option value="${category.id}" <c:if test="${selectedCategoryId == category.id}">selected</c:if>>
                        <c:out value="${category.name}"/>
                    </option>
                </c:forEach>
            </select>
        </label>
        <label class="full">Link ảnh
            <input data-image-url name="picture" maxlength="255" placeholder="Dán link ảnh https://..." value="<c:out value='${dish.picture}'/>">
        </label>
        <label class="full">Hoặc chọn ảnh từ máy (PNG, JPEG, GIF; tối đa 5 MB)
            <input data-image-file name="image" type="file" accept="image/png,image/jpeg,image/gif">
        </label>
        <img data-image-preview class="image-preview hidden full" alt="Ảnh xem trước">
        <label>Trạng thái
            <select name="state">
                <option value="Available" <c:if test="${empty dish.state || dish.state == 'Available'}">selected</c:if>>Đang bán</option>
                <option value="Unavailable" <c:if test="${dish.state == 'Unavailable'}">selected</c:if>>Tạm ngừng</option>
            </select>
        </label>
        <label class="full">Mô tả
            <textarea name="description" rows="4" maxlength="255"><c:out value="${dish.description}"/></textarea>
        </label>
        <div class="actions full">
            <button type="submit">Thêm</button>
            <a class="button secondary" href="${pageContext.request.contextPath}/management/dishes">Hủy</a>
        </div>
    </form>
</main>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
