<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100..900;1,100..900&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
    <link rel="stylesheet" href="../../css/view.css">
    <link rel="stylesheet" href="../../css/both-nav.css">
    <title>Chi tiết đơn hàng</title>
</head>
<body>

<!-- Side Nav -->
<%@ include file="../../assert/sidenav.jsp" %>

<div class="container">
    <!-- Navbar -->
    <%@ include file="../../assert/topnav.jsp" %>

    <!-- Content -->
    <div class="container-content">
        <h2 class="title">Chi Tiết Đơn Hàng</h2>

        <!-- Thông tin khách hàng -->
        <div class="card">
            <div class="card-header">
                <h4>Thông Tin Cá Nhân</h4>
            </div>
            <div class="card-body">
                <p><strong>ID:</strong> #CD12837</p>
                <p><strong>Tên Tài Khoản:</strong> nguyenA</p>
                <p><strong>Họ và Tên:</strong> Nguyễn Văn A</p>
                <p><strong>Email:</strong> nguyenvana@example.com</p>
                <p><strong>Số Điện Thoại:</strong> 0901234567</p>
                <p><strong>Địa Chỉ:</strong> 123 Đường ABC, Quận 1, TP.HCM</p>
            </div>
        </div>

        <!-- Thông tin tài khoản -->
        <div class="card">
            <div class="card-header">
                <h4>Thông Tin Tài Khoản</h4>
            </div>
            <div class="card-body">
                <p><strong>Hình Thức Đăng Nhập:</strong> Google</p>
                <p><strong>Trạng Thái:</strong> Hoạt động</p>
                <p><strong>Ngày Đăng Ký:</strong> 2024-01-15</p>
            </div>
        </div>

        <!-- Danh sách hàng hóa đã mua -->
        <div class="card">
            <div class="card-header">
                <h4>Danh Sách Hàng Hóa Đã Mua</h4>
            </div>
            <div class="card-body">
                <table class="table" id="buy-table">
                    <thead>
                    <tr>
                        <th>Sản Phẩm</th>
                        <th>Số Lượng</th>
                        <th>Giá</th>
                        <th>Thành Tiền</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Áo Thun Trẻ Em</td>
                        <td>2</td>
                        <td>200,000 VND</td>
                        <td>400,000 VND</td>
                    </tr>
                    <tr>
                        <td>Giày Bé Gái</td>
                        <td>1</td>
                        <td>500,000 VND</td>
                        <td>500,000 VND</td>
                    </tr>
                    <tr>
                        <td>Xe Đạp Trẻ Em</td>
                        <td>1</td>
                        <td>4,000,000 VND</td>
                        <td>4,000,000 VND</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>


        <!-- Ghi chú đơn hàng -->
        <div class="card">
            <div class="card-header">
                <h4>Ghi Chú Đơn Hàng</h4>
            </div>
            <div class="card-body">
                <p>Khách hàng yêu cầu giao hàng trước 18:00.</p>
                <p>Ghi chú thêm: Vui lòng gọi trước khi giao.</p>
            </div>
        </div>

        <!-- Nút quay lại -->
        <div class="back-btn">
            <a href="./manage-order.jsp" class="btn btn-secondary"><i class="fas fa-arrow-left"></i></a>
        </div>
    </div>

</div>


<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="../../js/admin.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script>
    $(document).ready(function () {
        $('#buy-table').DataTable({
            "paging": true,         // Bật phân trang
            "searching": true,      // Bật tìm kiếm
            "ordering": true,       // Bật sắp xếp cột
            "info": true,           // Hiển thị thông tin tổng số dòng
            "language": {           // Tùy chỉnh ngôn ngữ
                "search": "Tìm kiếm:",
                "lengthMenu": "Hiển thị _MENU_ dòng",
                "info": "Hiển thị _START_ đến _END_ của _TOTAL_",
                "paginate": {
                    "first": "Đầu",
                    "last": "Cuối",
                    "next": "Tiếp",
                    "previous": "Trước"
                }
            }
        });
    });
</script>
</body>
</html>
