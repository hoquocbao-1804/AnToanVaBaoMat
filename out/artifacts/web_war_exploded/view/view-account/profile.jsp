<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<body>
<%@ include file="/view/view-index/header.jsp" %>
<%
    String username = (String) session.getAttribute("username");
    String fullname = (String) session.getAttribute("fullname");
    String email = (String) session.getAttribute("email");
    String phoneNumber = (String) session.getAttribute("phoneNumber");
    String address = (String) session.getAttribute("address");
    String birthday = (String) session.getAttribute("birthDate");
%>

<div class="container"> &nbsp;</div>
<div class="container">
    <div class="row">
        <div class="col-md-3 ">
            <div class="list-group ">
                <a href="#" class="list-group-item list-group-item-action active">Dashboard</a>
                <a href="#" class="list-group-item list-group-item-action">Xem Giỏ Hàng</a>
                <a href="#" class="list-group-item list-group-item-action">Lịch Sử Mua</a>

            </div>
        </div>
        <div class="col-md-9">
            <div class="card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-12">
                            <h4>Thông Tin Tài Khoản</h4>
                            <hr>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <img id="avatar"
                                 src="https://cdn.pixabay.com/photo/2025/03/07/13/12/flower-9453063_1280.jpg"
                                 alt="Avatar" class="img-fluid rounded-circle" width="100" height="100"
                                 style="display: block; margin-left: auto; margin-right: auto;">
                            <div class="form-group row">

                                <label class="col-4 col-form-label">Ảnh</label>
                                <div class="col-8" style="display: flex; justify-content: center; align-items: center;">
                                    <!-- Căn giữa ảnh bằng CSS inline -->

                                    <!-- Input file -->
                                    <input type="file" id="avatar-upload" class="form-control mt-2"
                                           onchange="previewImage(event)">
                                </div>
                            </div>


                            <div class="form-group row">
                                <label for="username" class="col-4 col-form-label">Họ Và Tên:</label>
                                <div class="col-8">
                                    <input id="username" name="username" placeholder="Họ Và Tên"
                                           class="form-control here" required="required" type="text"
                                           value="<%= fullname %>">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label for="name" class="col-4 col-form-label">Email:</label>
                                <div class="col-8">
                                    <input id="name" name="email" placeholder="Địa Chỉ Email" class="form-control here"
                                           type="text" value="<%= email %>">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-4 col-form-label">Số Điện Thoại:</label>
                                <div class="col-8">
                                    <input name="phoneNumber" placeholder="Số Điện Thoại" class="form-control here"
                                           type="text" value="<%= phoneNumber %>">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-4 col-form-label">Địa Chỉ:</label>
                                <div class="col-8">
                                    <input name="lastname" placeholder="Địa Chỉ" class="form-control here" type="text">
                                </div>
                            </div>
                            <div class="form-group row">
                                <label class="col-4 col-form-label">Ngày Sinh:</label>
                                <div class="col-8">
                                    <input name="lastname" placeholder="Ngày Sinh" class="form-control here"
                                           type="date">
                                </div>
                            </div>

                            <div class="form-group row">
                                <div class="offset-4 col-8">
                                    <button name="submit" type="submit" class="btn btn-primary">Cập Nhật Thông Tin
                                    </button>
                                    <a style="color: white"
                                       href="<%= request.getContextPath() %>/view/view-account/forgot-password.jsp">
                                        <button name="submit" type="submit" class="btn btn-primary">Đổi Mật Khẩu
                                        </button>
                                    </a>

                                </div>
                            </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="container"> &nbsp;</div>
<%@ include file="/view/view-index/footer.jsp" %>
</body>
</html>

