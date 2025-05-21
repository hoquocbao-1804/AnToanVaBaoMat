<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:include page="../view-index/header.jsp"/>

<div class="container mt-4">
  <div class="row">

    <!-- Nội dung chính: Lịch sử mua hàng -->
    <div class="col-md-9">
      <h3>Lịch sử mua hàng</h3>

      <c:choose>
        <c:when test="${not empty orders}">
          <table class="table table-striped">
            <thead>
            <tr>
              <th>Mã ĐH</th>
              <th>Ngày đặt</th>
              <th>Tổng tiền</th>
              <th>Trạng thái</th>
              <th>Chi tiết</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="o" items="${orders}">
              <tr>
                <td>${o.idOrder}</td>
                <td><fmt:formatDate value="${o.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td><fmt:formatNumber value="${o.totalPrice}" type="currency"/></td>
                <td>${o.status}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/order-detail?orderId=${o.idOrder}"
                     class="btn btn-sm btn-primary">Xem</a>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </c:when>
        <c:otherwise>
          <div class="alert alert-info">Bạn chưa có đơn hàng nào.</div>
        </c:otherwise>
      </c:choose>

    </div>
  </div>
</div>

<jsp:include page="../view-index/footer.jsp"/>
