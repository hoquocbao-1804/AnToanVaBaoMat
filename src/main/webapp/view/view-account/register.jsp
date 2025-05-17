<%--
  Created by IntelliJ IDEA.
  User: hdanh
  Date: 24/02/2025
  Time: 8:49 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<%@ include file="/view/view-index/header.jsp" %>
<section class="section-content padding-y">
    <!-- ============================ COMPONENT REGISTER   ================================= -->
    <!-- ============================ COMPONENT ĐĂNG KÝ ================================= -->
    <div class="card mx-auto" style="max-width:520px; margin-top:40px;">
        <article class="card-body">
            <header class="mb-4"><h4 class="card-title">Đăng ký</h4></header>
            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="form-row">
                    <div style="display: none;">
                        <label>Role</label>
                        <input type="text" name="idRole" class="form-control" value="2">
                    </div>
                    <div style="display: none;">
                        <label>Active</label>
                        <input type="text" name="active" class="form-control" value="1">
                    </div>
                    <div class="col form-group">
                        <label>Họ Và Tên</label>
                        <input type="text" name="fullname" class="form-control" placeholder="Nhập Họ Và Tên">
                    </div> <!-- form-group end.// -->
                    <div class="col form-group">

                        <label>Email</label>
                        <input type="email" name="email" class="form-control" placeholder="Email">

                    </div> <!-- form-group end.// -->
                </div> <!-- form-row end.// -->
                <div class="form-group">
                    <label>Tên Đăng Nhập</label>
                    <input type="text" name="username" class="form-control" placeholder="Tên Đăng Nhập" required>
                </div>
                <div class="form-group">
                    <label>Số Điện Thoại</label>
                    <input type="text" name="phoneNumber" class="form-control" placeholder="Số Điện Thoại" >
                </div>

                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label>Tạo mật khẩu</label>
                        <input name="password" class="form-control" type="password">
                    </div> <!-- form-group end.// -->
                    <div class="form-group col-md-6">
                        <label>Nhập lại mật khẩu</label>
                        <input name="confirmPassword" class="form-control" type="password">
                    </div> <!-- form-group end.// -->
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block"> Đăng ký</button>
                </div> <!-- form-group// -->
                <%-- hiện thông báo lỗi--%>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>

            </form>
        </article><!-- card-body.// -->
    </div> <!-- card .// -->
    <p class="text-center mt-4">Bạn đã có tài khoản? <a href="">Đăng nhập</a></p>
    <br><br>

    <!-- ============================ COMPONENT REGISTER  END.// ================================= -->
</section>
<%@ include file="/view/view-index/footer.jsp" %>
</body>
</html>


