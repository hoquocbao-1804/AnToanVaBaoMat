<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/view/view-index/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container my-4">
    <h3>Lịch sử mua hàng</h3>
    <p>Số đơn hàng: <strong>${fn:length(orderList)}</strong></p>

    <c:if test="${not empty orderList}">
    <table>
        <thead>
        <tr>
            <th>Order ID</th>
            <th>Total Price</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <td>${order.idOrder}</td>
                <td>${order.totalPrice}</td>
                <td>${order.status}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>

    <c:if test="${empty orderList}">
    <p>No orders found.</p>
    </c:if>


<jsp:include page="/view/view-index/footer.jsp"/>