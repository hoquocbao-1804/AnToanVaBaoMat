document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll(".increase-qty, .decrease-qty").forEach(button => {
        button.addEventListener("click", function () {
            let input = this.closest(".input-group").querySelector(".quantity-input");
            let variantId = input.dataset.variantId;
            let newQuantity = parseInt(input.value);

            if (this.classList.contains("increase-qty")) {
                newQuantity++;
            } else if (this.classList.contains("decrease-qty") && newQuantity > 1) {
                newQuantity--;
            }

            input.value = newQuantity;

            fetch("/web/cart", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: `action=updateQuantity&idVariant=${variantId}&quantity=${newQuantity}`
            })
                .then(response => response.text())
                .then(totalPrice => {
                    document.getElementById("totalPrice").innerText = totalPrice + " đ";
                });
        });
    });

    const btn = document.getElementById("test-btn");
    if (btn) {
        btn.addEventListener("click", () => {
            alert("Event success");
            console.log("Button with ID 'test-btn' found!");
        });
    } else {
        console.log("Button with ID 'test-btn' not found!");
    }

    // Thêm sự kiện tăng số lượng khi click vào nút "add thêm hàng"
    const addButtons = document.querySelectorAll(".add-item-btn");
    addButtons.forEach(button => {
        button.addEventListener("click", function () {
            const input = this.closest(".input-group").querySelector(".quantity-input");
            let newQuantity = parseInt(input.value);
            let variantId = input.dataset.variantId;

            newQuantity++;
            input.value = newQuantity;

            fetch("/web/cart", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: `action=updateQuantity&idVariant=${variantId}&quantity=${newQuantity}`
            })
                .then(response => response.text())
                .then(totalPrice => {
                    document.getElementById("totalPrice").innerText = totalPrice + " đ";
                });
        });
    });

});