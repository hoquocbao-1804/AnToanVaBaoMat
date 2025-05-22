<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="/view/view-index/header.jsp"/>

<div class="container my-4">
  <h3>Lịch sử mua hàng</h3>
  <c:choose>
    <c:when test="${not empty orders}">
      <c:forEach var="o" items="${orders}">
        <!-- Invoice card -->
        <div class="card mb-4">
          <div class="card-body">
            <h5 class="card-title">Hóa đơn: #${o.idOrder}</h5>
            <p class="card-text">
              Phương thức: ${o.status} <br/>
              Ngày: <fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy"/>
            </p>

            <!-- Bảng sản phẩm -->
            <table class="table table-bordered">
              <thead>
              <tr>
                <th>Sản phẩm</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach var="d" items="${detailsMap[o.idOrder]}">
                <tr>
                  <td>${d.nameProduct}</td>
                  <td>${d.quantity}</td>
                  <td><fmt:formatNumber value="${d.price * d.quantity}" type="currency"/></td>
                </tr>
              </c:forEach>
              </tbody>
            </table>

            <!-- Tổng cộng -->
            <div class="text-right">
              <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${o.totalPrice}" type="currency"/></p>
              <!-- Nếu có phí vận chuyển -->
              <p><strong>Phí vận chuyển:</strong> <fmt:formatNumber value="${o.address != null ? o.address.getShippingFee() : 0}" type="currency"/></p>
              <p><strong>Tổng thanh toán:</strong>
                <fmt:formatNumber value="${o.totalPrice + (o.address != null ? o.address.getShippingFee() : 0)}" type="currency"/>
              </p>
            </div>
          </div>
        </div>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="alert alert-info">Bạn chưa có đơn hàng nào.</div>
    </c:otherwise>
  </c:choose>
</div>

<jsp:include page="/view/view-index/footer.jsp"/>
