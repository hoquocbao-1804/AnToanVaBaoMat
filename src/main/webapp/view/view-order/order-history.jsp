<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/view/view-index/header.jsp"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container my-4">
    <h3>Lịch sử mua hàng</h3>
    <p>Số đơn hàng: <strong>${fn:length(orderList)}</strong></p>

    <c:if test="${empty orderList}">
        <p><em>Bạn chưa có đơn hàng nào.</em></p>
    </c:if>

    <%--    <c:if test="${not empty orderList}">--%>
    <%--        <table border="1" cellpadding="8">--%>
    <%--            <thead>--%>
    <%--            <tr>--%>
    <%--                <th>Mã hóa đơn</th>--%>
    <%--                <th>Ngày tạo</th>--%>
    <%--                <th>Sản phẩm</th>--%>
    <%--                <th>Thành tiền</th>--%>
    <%--            </tr>--%>
    <%--            </thead>--%>
    <%--            <tbody>--%>
    <%--            <c:forEach var="o" items="${orderList}">--%>
    <%--                <tr>--%>
    <%--                    <td>#${o.idOrder}</td>--%>
    <%--                    <td>${o.createAt}</td>--%>
    <%--                    <td>${o.productName}</td>--%>
    <%--                    <td>${o.totalPrice}</td>--%>
    <%--                </tr>--%>
    <%--            </c:forEach>--%>
    <%--            </tbody>--%>
    <%--        </table>--%>
    <%--    </c:if>--%>
    <c:if test="${not empty orderList}">
        <div class="order-list">
            <c:forEach var="o" items="${orderList}">
                <div class="order-card" style="border:1px solid #ccc; padding:16px; margin-bottom:12px; border-radius:8px;">
                    <p><strong>Mã hóa đơn:</strong> #${o.idOrder}</p>
                    <p><strong>Ngày tạo:</strong> ${o.createAt}</p>
                    <p><strong>Sản phẩm:</strong> ${o.productName}</p>
                    <p><strong>Thành tiền:</strong> ${o.totalPrice}</p>
                </div>
            </c:forEach>
        </div>
    </c:if>
</div>

<jsp:include page="/view/view-index/footer.jsp"/>