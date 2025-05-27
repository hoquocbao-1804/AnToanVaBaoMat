<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/view/view-index/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container my-4">
    <h3>Lịch sử mua hàng</h3>
    <p>Số đơn hàng: ${fn:length(orderList)}</p>
    <p>DEBUG: ${orderList}</p>

    <table border="1" cellpadding="10">
        <thead>
        <tr>
            <th>Mã đơn</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Ngày tạo</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <td>${order.idOrder}</td>
                <td>${order.totalPrice}</td>
                <td>${order.status}</td>
                <td>${order.createdAt}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/view/view-index/footer.jsp"/>
