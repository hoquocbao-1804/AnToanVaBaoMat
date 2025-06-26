<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="/view/view-index/header.jsp"/>

<style>
    .order-container {
        max-width: 900px;
        margin: 0 auto;
        padding: 30px 20px;
        background-color: #fff;
        box-shadow: 0 0 12px rgba(0,0,0,0.1);
        border-radius: 12px;
        margin-bottom: 40px;
    }

    .order-title {
        font-size: 26px;
        font-weight: 600;
        margin-bottom: 10px;
    }

    .order-count {
        font-size: 16px;
        color: #555;
        margin-bottom: 25px;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        font-size: 16px;
    }

    thead {
        background-color: #f5f5f5;
    }

    th, td {
        padding: 12px 16px;
        text-align: center;
        border-bottom: 1px solid #ddd;
    }

    tr:nth-child(even) {
        background-color: #fafafa;
    }

    tr:hover {
        background-color: #f1f1f1;
    }

    .no-orders {
        text-align: center;
        color: #888;
        font-style: italic;
    }
</style>

<div class="order-container">
    <h3 class="order-title">Lịch sử mua hàng</h3>
    <p class="order-count">Số đơn hàng: <strong>${fn:length(orderList)}</strong></p>

    <c:if test="${not empty orderList}">
        <table>
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="order" items="${orderList}">
                <tr>
                    <td>${order.idOrder}</td>
                    <td><fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫" /></td>
                    <td>${order.status}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty orderList}">
        <p class="no-orders">Không tìm thấy đơn hàng nào.</p>
    </c:if>
</div>

<jsp:include page="/view/view-index/footer.jsp"/>
