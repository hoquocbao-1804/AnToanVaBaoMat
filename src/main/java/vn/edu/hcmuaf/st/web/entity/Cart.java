package vn.edu.hcmuaf.st.web.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Cart implements Serializable {

    private int idUser;
    private Map<Integer, CartItem> cartItems;
    private double totalPrice;
    private double discountAmount = 0.0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Cart() {
        this.cartItems = new HashMap<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cart(int idUser) {
        this.idUser = idUser;
        this.cartItems = new HashMap<>();
        this.totalPrice = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cart(int idUser, Map<Integer, CartItem> cartItems, double totalPrice) {
        this.idUser = idUser;
        this.cartItems = (cartItems != null) ? cartItems : new HashMap<>();
        this.totalPrice = totalPrice;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cart(int idUser, Map<Integer, CartItem> cartItems, double totalPrice, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idUser = idUser;
        this.cartItems = (cartItems != null) ? cartItems : new HashMap<>();
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addItem(Product product) {
        if (product == null || product.getProductVariants().isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm không hợp lệ.");
        }

        ProductVariant variant = null;
        List<ProductVariant> variants = product.getProductVariants();

        if (variants != null && !variants.isEmpty()) {
            variant = variants.get(0);  // Lấy phần tử đầu tiên trong danh sách
        } else {
            // Xử lý khi không có biến thể nào, ví dụ:
            throw new IllegalArgumentException("Sản phẩm không có biến thể hợp lệ.");
        }

        CartItem item = cartItems.get(variant.getIdvariant());

        if (item == null) {
            // Lấy danh sách màu và size có sẵn của sản phẩm
            List<Color> availableColors = (product.getColors() != null) ? product.getColors() : new ArrayList<>();
            List<Size> availableSizes = (product.getSizes() != null) ? product.getSizes() : new ArrayList<>();

            // Tạo mới CartItem nếu chưa có trong giỏ hàng
            item = new CartItem(
                    variant.getIdvariant(),
                    product.getTitle(),
                    variant.getSize(),
                    variant.getColor(),
                    product.getFinalPrice(),  // Giá từ product
                    0, // Giá giảm chưa có
                    1,
                    product.getProductImages().stream()
                            .filter(Objects::nonNull)
                            .map(ProductImage::getImageUrl)
                            .findFirst()
                            .orElse("")

            );
            // Gán danh sách màu và size có sẵn cho CartItem
            item.setAvailableColors(availableColors);
            item.setAvailableSizes(availableSizes);

            cartItems.put(variant.getIdvariant(), item);
        } else {
            item.setQuantity(item.getQuantity()+1);
        }

        // Cập nhật tổng giá trị giỏ hàng
        updateTotalPrice();
        this.updatedAt = LocalDateTime.now();
    }

    public void addQuantity(int idVariant, int quantity) {
        CartItem item = cartItems.get(idVariant);
        if (item != null && quantity > 0) {
            item.setQuantity(quantity);
            updateTotalPrice();
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void updateTotalPrice() {
        this.totalPrice = cartItems.values().stream()
                .mapToDouble(item -> {
                    double price = item.getDiscountPrice() > 0 ? item.getDiscountPrice() : item.getPrice();
                    return price * item.getQuantity();
                })
                .sum();
    }

    public void removeItem(int idVariant) {
        if (cartItems.containsKey(idVariant)) {
            cartItems.remove(idVariant);
            updateTotalPrice();
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void clearCart() {
        cartItems.clear();
        totalPrice = 0;
        discountAmount = 0;
        updatedAt = LocalDateTime.now();
    }

    public int getTotalQuantity() {
        return cartItems.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getFinalTotal() {
        return totalPrice - discountAmount;
    }

    // Getter & Setter

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Map<Integer, CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Map<Integer, CartItem> cartItems) {
        this.cartItems = (cartItems != null) ? cartItems : new HashMap<>();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "idUser=" + idUser +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                ", discountAmount=" + discountAmount +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
