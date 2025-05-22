<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/view/view-index/header.jsp"/>

<div class="container my-4">
    <h3>Lịch sử mua hàng</h3>

    <c:choose>
        <c:when test="${not empty orders}">
            <div class="order-history d-flex flex-column gap-3">
                <c:forEach var="order" items="${orders}">
                    <div class="order-item d-flex align-items-start border rounded p-3 gap-4 flex-wrap">
                        <!-- Meta đơn hàng -->
                        <div class="order-meta text-nowrap">
                            <strong>Đơn #${order.idOrder}</strong><br/>
                            <small>
                                <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                            </small><br/>
                            <strong>
                                Tổng: <fmt:formatNumber value="${order.totalPrice}" type="currency" groupingUsed="true"/> đ
                            </strong>
                        </div>
                        <!-- Chi tiết sản phẩm nằm ngang -->
                        <div class="order-details d-flex flex-wrap gap-3">
                            <c:forEach var="detail" items="${detailsMap[order.idOrder]}">
                                <div class="detail-item text-center border rounded p-2" style="min-width:120px">
                                    <div class="fw-semibold">${detail.nameProduct}</div>
                                    <div>x${detail.quantity}</div>
                                    <div>
                                        <fmt:formatNumber value="${detail.price * detail.quantity}"
                                                          type="currency" groupingUsed="true"/> đ
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                Bạn chưa có đơn hàng nào.
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/view/view-index/footer.jsp"/>
