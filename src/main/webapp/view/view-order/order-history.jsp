<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/view/view-index/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container my-4">
    <h3>Lịch sử mua hàng</h3>
    <!-- hiện số đơn -->
    <p>Số đơn hàng: <strong>${fn:length(orderList)}</strong></p>

    <!-- nếu không có đơn nào -->
    <c:if test="${empty orderList}">
        <p><em>Bạn chưa có đơn hàng nào.</em></p>
    </c:if>

    <!-- nếu có thì lặp -->
    <c:if test="${not empty orderList}">
        <table border="1" cellpadding="8">
            <thead>
            <tr>
                <th>Mã hóa đơn</th>
                <th>Ngày tạo</th>
                <th>Sản phẩm</th>
                <th>Thành tiền</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orderList}">
                <tr>
                    <!-- hiển thị ví dụ #28 -->
                    <td>#${o.idOrder}</td>
                    <!-- format ngày nếu muốn -->
                    <td>${o.createAt}</td>
                    <!-- giả sử trong Order object bạn có sẵn tên sản phẩm và totalPrice-->
                    <td>${o.productName}</td>
                    <td>${o.totalPrice}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<jsp:include page="/view/view-index/footer.jsp"/>
