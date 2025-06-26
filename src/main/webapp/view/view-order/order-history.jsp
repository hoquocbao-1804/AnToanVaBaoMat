<%@ page isErrorPage="true" %>
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

        <p>Số đơn hàng: ${fn:length(orderList)}</p>
<%--o day--%>
        <table>
            <thead>
            <tr>
                <th>Order ID</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
                <th>Xác nhận chữ ký</th>
                <th>Chữ ký điện tử</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="order" items="${orderList}">
                <tr>
                    <td>${order.idOrder}</td>
                    <td><fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫" /></td>
                    <td>${order.status}</td>
                    <td>${empty order.signatureStatus ? 'Chưa ký' : order.signatureStatus}</td>
                    <td>
                        <c:choose>
                            <c:when test="${empty order.digitalSignature}">
                                <button onclick="openSignPopup('${order.idOrder}', '')">Ký</button>
                            </c:when>
                            <c:otherwise>
                                Đã ký
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>


            </tbody>

        </table>
    </c:if>

    <c:if test="${empty orderList}">
        <p class="no-orders">Không tìm thấy đơn hàng nào.</p>
    </c:if>
    <div id="signPopup" style="display:none; position:fixed; top:20%; left:35%; background:#fff; padding:20px; border:1px solid #ccc; z-index:1000;">
        <h4>Ký xác thực đơn hàng</h4>
        <p><b>Mã Hash:</b> <input type="text" id="hashValue" readonly style="width:100%"/></p>
        <p><b>Chữ ký:</b> <input type="text" id="signatureValue" placeholder="Dán chữ ký ở đây" style="width:100%" /></p>
        <a href="/tool/download-sign-tool.zip" target="_blank">Tải tool tại đây</a><br/><br/>
        <button onclick="submitSignature()">Xác nhận</button>
        <button onclick="closeSignPopup()">Hủy</button>
    </div>

</div>
<script>
    let currentOrderId = null;

    function openSignPopup(orderId, hash) {
        currentOrderId = orderId;
        document.getElementById("hashValue").value = hash;
        document.getElementById("signatureValue").value = "";
        document.getElementById("signPopup").style.display = "block";
    }

    function closeSignPopup() {
        document.getElementById("signPopup").style.display = "none";
    }

    function submitSignature() {
        const signature = document.getElementById("signatureValue").value;
        fetch('/sign-order', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                orderId: currentOrderId,
                signature: signature
            })
        }).then(res => {
            if (res.ok) {
                alert("Ký thành công!");
                location.reload();
            } else {
                alert("Ký thất bại!");
            }
        });
    }
</script>

<jsp:include page="/view/view-index/footer.jsp"/>
