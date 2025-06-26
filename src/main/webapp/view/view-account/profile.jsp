<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
</head>
<!-- Modal -->
<div class="modal fade" id="publicKeyModal" tabindex="-1" role="dialog" aria-labelledby="publicKeyModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document"> <!-- căn giữa -->
        <div class="modal-content shadow rounded-lg border-0">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="publicKeyModalLabel">🔐 Cập nhật Public Key</h5>
                <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close" style="font-size: 1.4rem;">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body p-4">
                <label for="publicKeyInput" class="font-weight-bold mb-2">Nhập public key mới:</label>
                <input type="text" id="publicKeyInput" class="form-control" placeholder="0x..." style="font-family: monospace;">
            </div>
            <div class="modal-footer d-flex justify-content-between px-4 pb-4">
                <button type="button" class="btn btn-outline-secondary" data-dismiss="modal">❌ Hủy</button>
                <button type="button" class="btn btn-success" onclick="submitPublicKey()">✅ Xác nhận</button>
            </div>
        </div>
    </div>
</div>


<body>
<%@ include file="/view/view-index/header.jsp" %>

<!-- Lấy thông tin người dùng từ request -->
<c:set var="user" value="${requestScope.user}" />

<div class="container"> &nbsp;</div>
<div class="container">
    <div class="row">
        <div class="col-md-3">
            <div class="list-group">
                <a href="#" class="list-group-item list-group-item-action active">Dashboard</a>
                <a href="${pageContext.request.contextPath}/cart" class="list-group-item list-group-item-action">Xem Giỏ Hàng</a>
                <a href="${pageContext.request.contextPath}/order-history" class="list-group-item list-group-item-action">Lịch sử mua hàng</a>

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
                            <form method="post" action="${pageContext.request.contextPath}/profile">
                                <div class="form-group row">
                                    <label for="fullName" class="col-4 col-form-label">Họ Và Tên:</label>
                                    <div class="col-8">
                                        <input id="fullName" name="fullName" placeholder="Họ Và Tên"
                                               class="form-control here" required="required" type="text"
                                               value="${user.fullName}">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label for="email" class="col-4 col-form-label">Email:</label>
                                    <div class="col-8">
                                        <input id="email" name="email" placeholder="Địa Chỉ Email" class="form-control here"
                                               type="email" value="${user.email}">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-4 col-form-label">Số Điện Thoại:</label>
                                    <div class="col-8">
                                        <input name="phoneNumber" placeholder="Số Điện Thoại" class="form-control here"
                                               type="number" value="${user.phoneNumber}">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-4 col-form-label">Địa Chỉ:</label>
                                    <div class="col-8">
                                        <input name="address" placeholder="Địa Chỉ" class="form-control here" type="text"
                                               value="${user.address.address}">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-4 col-form-label">Phường/Xã:</label>
                                    <div class="col-8">
                                        <input name="ward" placeholder="Phường/Xã" class="form-control here" type="text"
                                               value="${user.address.ward}">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-4 col-form-label">Quận/Huyện:</label>
                                    <div class="col-8">
                                        <input name="district" placeholder="Quận/Huyện" class="form-control here" type="text"
                                               value="${user.address.district}">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-4 col-form-label">Tỉnh/Thành Phố:</label>
                                    <div class="col-8">
                                        <input name="province" placeholder="Tỉnh/Thành Phố" class="form-control here" type="text"
                                               value="${user.address.province}">
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <label class="col-4 col-form-label">Ngày Sinh:</label>
                                    <div class="col-8">
                                        <input name="birthDate" placeholder="Ngày Sinh" class="form-control here"
                                               type="date" value="${user.birthDate}">
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <div class="offset-4 col-8">
                                        <button name="submit" type="submit" class="btn btn-primary">Cập Nhật Thông Tin</button>
                                        <a style="color: white" href="<%= request.getContextPath() %>/view/view-account/forgot-password.jsp">
                                            <button type="button" class="btn btn-primary">Đổi Mật Khẩu</button>


                                        </a>
                                        <button type="button" class="btn btn-info" data-toggle="modal" data-target="#publicKeyModal">
                                            Cập nhật PublicKey
                                        </button>


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
<script>
    function submitPublicKey() {
        const publicKey = document.getElementById("publicKeyInput").value;

        if (!publicKey) {
            alert("Vui lòng nhập public key.");
            return;
        }

        // TODO: Gửi publicKey đến server qua AJAX hoặc form ẩn
        console.log("Public key mới:", publicKey);

        // Thông báo thành công (giống ảnh)
        $('#publicKeyModal').modal('hide');
        const successAlert = document.createElement('div');
        successAlert.className = 'alert alert-success mt-3';
        successAlert.innerText = 'Đã cập nhật khóa công khai thành công.';
        document.body.appendChild(successAlert);

        // Tự ẩn thông báo sau 3 giây
        setTimeout(() => successAlert.remove(), 3000);
    }
</script>
<style>
    .modal-content {
        border-radius: 15px;
        transition: all 0.3s ease-in-out;
    }
    .modal-header {
        border-bottom: none;
    }
    .modal-footer {
        border-top: none;
    }
    #publicKeyInput:focus {
        border-color: #28a745;
        box-shadow: 0 0 5px rgba(40, 167, 69, 0.5);
    }
</style>

<div class="container"> &nbsp;</div>
<%@ include file="/view/view-index/footer.jsp" %>
</body>
</html>
