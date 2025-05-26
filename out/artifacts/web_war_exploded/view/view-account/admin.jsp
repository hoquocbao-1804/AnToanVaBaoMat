<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page import="vn.edu.hcmuaf.st.web.util.PermissionUtil" %>
<%@ page import="vn.edu.hcmuaf.st.web.entity.Role" %>

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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/both-nav.css">
    <title>Trang Quản Trị</title>
</head>
<body>

<!-- Side Nav -->
<nav class="navbar-left" id="navbar-left">
    <div class="sidebar-header">
        <div class="sidebar-title">
            <a href="${pageContext.request.contextPath}/admin" class="brand-logo-mini">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="logo" width="125">
            </a>
            <h2>Little Whale</h2>
        </div>
        <img class="user-avatar" src="${pageContext.request.contextPath}/images/avatar-admin.png" alt="User Image" width="100">
        <div class="user-info">
            <p class="user-name"><c:out value="${sessionScope.fullname}"/></p>
            <p class="user-greeting">Chào mừng bạn trở lại</p>
        </div>
    </div>
    <hr>
    <ul class="sidebar-menu">
        <li class="menu-item">
            <a class="menu-link" href="${pageContext.request.contextPath}/admin">
                <span class="menu-icon"><i class="fa fa-tachometer-alt"></i></span>
                <span class="menu-title">Bảng điều khiển</span>
            </a>
        </li>
        <li class="menu-item">
            <a class="menu-link" href="${pageContext.request.contextPath}/view-admin/admin-user/manage-user.jsp">
                <span class="menu-icon"><i class="fa fa-users"></i></span>
                <span class="menu-title">Quản lý khách hàng</span>
            </a>
        </li>
        <li class="menu-item">
            <a class="menu-link" href="${pageContext.request.contextPath}/view-admin/admin-product/manage-product.jsp">
                <span class="menu-icon"><i class="fa fa-box"></i></span>
                <span class="menu-title">Quản lý sản phẩm</span>
            </a>
        </li>
        <li class="menu-item">
            <a class="menu-link" href="${pageContext.request.contextPath}/view-admin/admin-order/manage-order.jsp">
                <span class="menu-icon"><i class="fa fa-tasks"></i></span>
                <span class="menu-title">Quản lý đơn hàng</span>
            </a>
        </li>
        <li class="menu-item">
            <a class="menu-link" href="${pageContext.request.contextPath}/view-admin/admin-coupon/manage-coupon.jsp">
                <span class="menu-icon"><i class="fa-solid fa-ticket"></i></span>
                <span class="menu-title">Quản lý mã giảm giá</span>
            </a>
        </li>
        <li class="menu-item">
            <a class="menu-link" href="${pageContext.request.contextPath}/view-admin/admin-category/manage-category.jsp">
                <span class="menu-icon"><i class="fa fa-chart-bar"></i></span>
                <span class="menu-title">Quản lý danh mục</span>
            </a>
        </li>
        <li class="menu-item">
            <a class="menu-link" href="${pageContext.request.contextPath}/logout">
                <span class="menu-icon"><i class="fa fa-sign-out-alt"></i></span>
                <span class="menu-title">Đăng xuất</span>
            </a>
        </li>
    </ul>
</nav>

<div class="container">
    <!-- Navbar -->
    <nav class="navbar">
        <div class="navbar-menu">
            <button class="navbar-toggler" id="navbar-toggle">
                <span class="navbar-icon">☰</span>
            </button>
            <div class="search">
                <input type="text" placeholder="Tìm kiếm sản phẩm">
            </div>
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/view-admin/admin-product/add-product.jsp" class="nav-link">
                        <i class="fa-solid fa-plus"></i> Thêm sản phẩm mới
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link"><i class="fa-solid fa-message"></i> Tin Nhắn <span class="badge">0</span></a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link"><i class="fa-solid fa-bell"></i> Thông Báo <span class="badge">0</span></a>
                </li>
            </ul>
            <div class="profile-container">
                <a href="#" class="profile-link">
                    <img src="https://anhcute.net/wp-content/uploads/2024/09/Hinh-anh-chibi-Spiderman-sieu-dang-yeu.jpg"
                         alt="Profile" class="profile-pic">
                    <span class="profile-name"><c:out value="${sessionScope.fullname}"/></span>
                </a>
            </div>
        </div>
    </nav>

    <!-- Overview -->
    <section class="overview">
        <h1 class="header-overview">Bảng điều khiển</h1>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>
        <c:if test="${not empty message}">
            <p style="color: green;">${message}</p>
        </c:if>
        <!-- Stats -->
        <div class="stats">
            <div class="stat-card">
                <div class="stat-card-icon"><i class="fa-solid fa-user"></i></div>
                Tổng khách hàng: <span><c:out value="${users.size()}"/> khách hàng</span>
            </div>
            <div class="stat-card">
                <div class="stat-card-icon"><i class="fa-solid fa-kaaba"></i></div>
                Tổng sản phẩm: <span>1850 sản phẩm</span>
            </div>
            <div class="stat-card">
                <div class="stat-card-icon"><i class="fa-solid fa-cart-shopping"></i></div>
                Tổng đơn hàng: <span>247 đơn hàng</span>
            </div>
            <div class="stat-card">
                <div class="stat-card-icon"><i class="fa-brands fa-sellsy"></i></div>
                Tổng sản phẩm bán được (6 tháng): <span>1000 sản phẩm</span>
            </div>
        </div>
    </section>

    <!-- Table -->
    <section class="tables">
        <!-- Order Table -->
        <div class="container-table">
            <h2>Tình trạng đơn hàng</h2>
            <table class="table" id="orderTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên khách hàng</th>
                    <th>Tổng tiền</th>
                    <th>Hình thức thanh toán</th>
                    <th>Ngày bắt đầu</th>
                    <th>Trạng thái</th>
                    <th>Tính năng</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>AL3947</td>
                    <td>Phạm Thị Ngọc</td>
                    <td>19.770.000 đ</td>
                    <td>Thẻ tín dụng</td>
                    <td>2024-11-01</td>
                    <td><span class="status-label pending">Chờ xử lý</span></td>
                    <td>
                        <button class="btn btn-trash"><i class="fas fa-trash-alt"></i></button>
                        <button class="btn btn-edit"><i class="fas fa-edit"></i></button>
                    </td>
                </tr>
                <tr>
                    <td>ER3835</td>
                    <td>Nguyễn Thị Mỹ Yến</td>
                    <td>16.770.000 đ</td>
                    <td>Thanh toán khi nhận hàng</td>
                    <td>2024-11-02</td>
                    <td><span class="status-label in-transit">Đang vận chuyển</span></td>
                    <td>
                        <button class="btn btn-trash"><i class="fas fa-trash-alt"></i></button>
                        <button class="btn btn-edit"><i class="fas fa-edit"></i></button>
                    </td>
                </tr>
                <tr>
                    <td>MD0837</td>
                    <td>Triệu Thanh Phú</td>
                    <td>9.400.000 đ</td>
                    <td>Ví điện tử</td>
                    <td>2024-11-03</td>
                    <td><span class="status-label completed">Đã hoàn thành</span></td>
                    <td>
                        <button class="btn btn-trash"><i class="fas fa-trash-alt"></i></button>
                        <button class="btn btn-edit"><i class="fas fa-edit"></i></button>
                    </td>
                </tr>
                <tr>
                    <td>MT9835</td>
                    <td>Đặng Hoàng Phúc</td>
                    <td>40.650.000 đ</td>
                    <td>Chuyển khoản ngân hàng</td>
                    <td>2024-11-04</td>
                    <td><span class="status-label cancelled">Đã hủy</span></td>
                    <td>
                        <button class="btn btn-trash"><i class="fas fa-trash-alt"></i></button>
                        <button class="btn btn-edit"><i class="fas fa-edit"></i></button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <!-- User Table -->
        <div class="container-table">
            <h2>Khách hàng mới</h2>
            <table class="table" id="userTable">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên tài khoản</th>
                    <th>Họ và tên</th>
                    <th>Email</th>
                    <th>Số điện thoại</th>
                    <th>Địa chỉ</th>
                    <th>Vai trò</th>
                    <th>Tính năng</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>#${user.idUser}</td>
                        <td><c:out value="${user.username}"/></td>
                        <td><c:out value="${user.fullName}"/></td>
                        <td><c:out value="${user.email}"/></td>
                        <td><c:out value="${user.phoneNumber}"/></td>
                        <td><c:out value="${user.address != null ? user.address.address : 'Chưa cập nhật'}"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/admin" method="post">
                                <input type="hidden" name="action" value="updateRole">
                                <input type="hidden" name="userId" value="${user.idUser}">
                                <select name="newRole">
                                    <option value="ROLE_USER" <c:if test="${user.role == 'ROLE_USER'}">selected</c:if>>Người dùng</option>
                                    <option value="ROLE_ADMIN" <c:if test="${user.role == 'ROLE_ADMIN'}">selected</c:if>>Quản trị</option>
                                </select>
                                <button type="submit">Cập nhật</button>
                            </form>
                        </td>
                        <td>
                            <button class="btn btn-trash"><i class="fas fa-trash-alt"></i></button>
                            <button class="btn btn-edit"><i class="fas fa-edit"></i></button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <!-- Chart -->
    <section>
        <div class="container-chart">
            <h2>Biểu đồ thống kê</h2>
            <div class="charts">
                <div class="chart" id="barChartDemo">
                    <h3>Thống kê 6 tháng doanh thu</h3>
                    <canvas id="barChart"></canvas>
                </div>
                <div class="chart" id="pieChartDemo">
                    <h3>Thống kê 6 sản phẩm bán chạy</h3>
                    <canvas id="pieChart"></canvas>
                </div>
            </div>
        </div>
    </section>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="${pageContext.request.contextPath}/js/admin.js"></script>
<script>
    $(document).ready(function () {
        $('#orderTable').DataTable({
            "paging": true,
            "searching": true,
            "ordering": true,
            "info": true,
            "language": {
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

        $('#userTable').DataTable({
            "paging": true,
            "searching": true,
            "ordering": true,
            "info": true,
            "language": {
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

    $(document).ready(function () {
        // Dữ liệu cho Bar Chart - Doanh thu trong 6 tháng
        var revenueData = {
            labels: [<c:forEach var="entry" items="${revenueData}" varStatus="loop">'${entry.key}'<c:if test="${!loop.last}">,</c:if></c:forEach>],
            datasets: [{
                label: 'Doanh thu (VNĐ)',
                data: [<c:forEach var="entry" items="${revenueData}" varStatus="loop">${entry.value}<c:if test="${!loop.last}">,</c:if></c:forEach>],
                backgroundColor: 'rgba(54, 162, 235, 0.5)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        };
        var barOptions = {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        };
        if ($("#barChart").length) {
            var barChartCanvas = $("#barChart").get(0).getContext("2d");
            new Chart(barChartCanvas, {
                type: 'bar',
                data: revenueData,
                options: barOptions
            });
        }

        // Dữ liệu cho Pie Chart - Sản phẩm bán chạy (giữ tĩnh)
        var pieData = {
            labels: ["Sản phẩm A", "Sản phẩm B", "Sản phẩm C", "Sản phẩm D", "Sản phẩm E"],
            datasets: [{
                data: [20, 30, 15, 25, 10],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.5)',
                    'rgba(54, 162, 235, 0.5)',
                    'rgba(255, 206, 86, 0.5)',
                    'rgba(75, 192, 192, 0.5)',
                    'rgba(153, 102, 255, 0.5)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)'
                ],
                borderWidth: 1
            }]
        };
        var pieOptions = {
            responsive: true,
            animation: {
                animateScale: true,
                animateRotate: true
            }
        };
        if ($("#pieChart").length) {
            var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
            new Chart(pieChartCanvas, {
                type: 'pie',
                data: pieData,
                options: pieOptions
            });
        }
    });
</script>
</body>
</html>