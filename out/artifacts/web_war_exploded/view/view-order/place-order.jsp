<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="/view/view-index/header.jsp" %>
<html>
<head>
    <title>Thanh toán</title>
</head>
<body>
<section class="section-content padding-y bg">
    <div class="container">



        <!-- ============================ COMPONENT 2 ================================= -->
        <div class="row">
            <main class="col-md-8">

                <article class="card mb-4">
                    <div class="card-body">
                        <h4 class="card-title mb-4">Giỏ hàng</h4>
                        <div class="row">

                            <c:forEach var="item" items="${sessionScope.cart.cartItems.values()}">
                                <div class="col-md-6">
                                    <figure class="itemside mb-4">
                                        <div class="aside"><img src="${item.imageUrl}" class="border img-sm"></div>
                                        <figcaption class="info">
                                            <p> ${item.productTitle} </p>
                                            <span class="text-muted d-block"> Số lượng: ${item.quantity} </span>
                                            <span class="text-muted d-block"> Giá: <fmt:formatNumber value="${item.price}" pattern="#,##0 đ"/> </span>
                                        </figcaption>
                                    </figure>
                                </div>
                            </c:forEach>

                        </div>
                    </div>
                </article>


                <article class="card mb-4">
                    <div class="card-body">
                        <h4 class="card-title mb-4">Thông tin liên lạc</h4>
                        <form action="">
                            <div class="row">
                                <div class="form-group col-sm-6">
                                    <label> Họ </label>
                                    <input type="text" name="firstName" placeholder="Nhập thông tin" class="form-control">
                                </div>
                                <div class="form-group col-sm-6">
                                    <label> Tên </label>
                                    <input type="text" name="lastName" placeholder="Nhập thông tin" class="form-control">
                                </div>
                                <div class="form-group col-sm-6">
                                    <label>Số điện thoại</label>
                                    <input type="text" name="phone" placeholder="+98" class="form-control">
                                </div>
                                <div class="form-group col-sm-6">
                                    <label>Email</label>
                                    <input type="email" name="email" placeholder="example@gmail.com" class="form-control">
                                </div>
                            </div> <!-- row.// -->
                        </form>
                    </div> <!-- card-body.// -->
                </article> <!-- card.// -->


                <article class="card mb-4">
                    <div class="card-body">
                        <h4 class="card-title mb-4">Thông tin giao hàng</h4>
                        <form action="">


                            <div class="row">
                                <div class="form-group col-sm-12">
                                    <label> Địa chỉ* </label>
                                    <input type="text" name="address" placeholder="Type here" class="form-control">
                                </div>
                                <div class="form-group col-sm-4">
                                    <label> Phường/Xã* </label>
                                    <input type="text" name="ward" placeholder="Type here" class="form-control">
                                </div>
                                <div class="form-group col-sm-4">
                                    <label> Quận/Huyện* </label>
                                    <input type="text" name="district" placeholder="" class="form-control">
                                </div>
                                <div class="form-group col-sm-4">
                                    <label> Tỉnh/Thành phố* </label>
                                    <input type="text" name="province" placeholder="" class="form-control">
                                </div>
                            </div> <!-- row.// -->
                        </form>
                    </div> <!-- card-body.// -->
                </article> <!-- card.// -->


                <article class="accordion" id="accordion_pay">
                    <div class="card">
                        <header class="card-header">
                            <img src="./images/misc/payment-paypal.png" class="float-right" height="24">
                            <label class="form-check collapsed" data-toggle="collapse" data-target="#pay_paynet">
                                <input class="form-check-input" name="payment-option" checked type="radio" value="option2">
                                <h6 class="form-check-label">
                                    Paypal
                                </h6>
                            </label>
                        </header>
                        <div id="pay_paynet" class="collapse show" data-parent="#accordion_pay">
                            <div class="card-body">
                                <p class="text-center text-muted">Connect your PayPal account and use it to pay your bills. You'll be redirected to PayPal to add your billing information.</p>
                                <p class="text-center">
                                    <a href="#"><img src="./images/misc/btn-paypal.png" height="32"></a>
                                    <br><br>
                                </p>
                            </div> <!-- card body .// -->
                        </div> <!-- collapse .// -->
                    </div> <!-- card.// -->
                    <div class="card">
                        <header class="card-header">
                            <img src="./images/misc/payment-card.png" class="float-right" height="24">
                            <label class="form-check" data-toggle="collapse" data-target="#pay_payme">
                                <input class="form-check-input" name="payment-option" type="radio" value="option2">
                                <h6 class="form-check-label"> Credit Card  </h6>
                            </label>
                        </header>
                        <div id="pay_payme" class="collapse" data-parent="#accordion_pay">
                            <div class="card-body">
                                <p class="alert alert-success">Some information or instruction</p>
                                <form class="form-inline">
                                    <input type="text" class="form-control mr-2" placeholder="xxxx-xxxx-xxxx-xxxx" name="">
                                    <input type="text" class="form-control mr-2" style="width: 100px"  placeholder="dd/yy" name="">
                                    <input type="number" maxlength="3" class="form-control mr-2"  style="width: 100px"  placeholder="cvc" name="">
                                    <button class="btn btn btn-success">Button</button>
                                </form>
                            </div> <!-- card body .// -->
                        </div> <!-- collapse .// -->
                    </div> <!-- card.// -->

                </article>
                <!-- accordion end.// -->

            </main> <!-- col.// -->
            <aside class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <dl class="dlist-align">
                            <dt>Tổng tiền:</dt>
                            <dd class="text-right">$69.97</dd>
                        </dl>
                        <dl class="dlist-align">
                            <dt>Giảm:</dt>
                            <dd class="text-right"> $10.00</dd>
                        </dl>
                        <dl class="dlist-align">
                            <dt>Tổng:</dt>
                            <dd class="text-right text-dark b">
                                <strong><fmt:formatNumber value="${cart.totalPrice}" pattern="#,##0 đ"/></strong>
                            </dd>
                        </dl>
                        <hr>
                        <p class="text-center mb-3">
                            <img src="./images/misc/payments.png" height="26">
                        </p>
                        <form action="${pageContext.request.contextPath}/place-order" method="post" id="submitForm">
                            <!-- 1.form thông tin người dùng -->
                            <input type="hidden" name="firstName">
                            <input type="hidden" name="lastName">
                            <input type="hidden" name="phone">
                            <input type="hidden" name="email">

                            <!-- 2.form thông tin giao hàng -->
                            <input type="hidden" name="address">
                            <input type="hidden" name="ward">
                            <input type="hidden" name="district">
                            <input type="hidden" name="province">

                            <!-- 3.form payment method -->

                            <button type="submit" class="btn btn-primary btn-block">Thanh toán</button>
                        </form>

                    </div> <!-- card-body.// -->
                </div> <!-- card.// -->
            </aside> <!-- col.// -->
        </div> <!-- row.// -->
        <!-- ============================ COMPONENT 2 END//  ================================= -->
    </div> <!-- container .//  -->
</section>
<!-- ========================= SECTION CONTENT END// ========================= -->

<%@ include file="/view/view-index/footer.jsp" %>

<script>
    document.getElementById("submitForm").addEventListener("submit", function(e) {
        const fields = ["firstName", "lastName", "phone", "email", "address", "ward", "district", "province"];

        fields.forEach(field => {
            const sourceInput = document.querySelector(`[name="${field}"]`);
            const hiddenInput = document.querySelector(`#submitForm [name="${field}"]`);
            if (sourceInput && hiddenInput) {
                hiddenInput.value = sourceInput.value;
            }
        });
    });
</script>

</body>
</html>
