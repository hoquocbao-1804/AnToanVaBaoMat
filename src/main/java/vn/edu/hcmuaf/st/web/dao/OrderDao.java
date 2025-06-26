package vn.edu.hcmuaf.st.web.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import vn.edu.hcmuaf.st.web.dao.db.JDBIConnect;
import vn.edu.hcmuaf.st.web.entity.Address;
import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.User;

import java.util.List;

public class OrderDao {

    private final Jdbi jdbi;

    public OrderDao() {
        try {
            this.jdbi = JDBIConnect.get();
            System.out.println("Kết nối JDBI thành công!");
        } catch (Exception e) {
            System.err.println("Lỗi kết nối JDBI: " + e.getMessage());
            throw new RuntimeException("Không thể kết nối đến cơ sở dữ liệu", e);
        }
    }

    // Thêm đơn hàng mới
    public int insertOrder(Order order) {
        String sql = "INSERT INTO orders (idUser, idAddress, idCoupon, totalPrice, status) " +
                "VALUES (:idUser, :idAddress, :idCoupon, :totalPrice, :status)";
        return jdbi.withHandle(handle -> {
            Update update = handle.createUpdate(sql)
                    .bind("idUser", order.getUser().getIdUser())
                    .bind("idAddress", order.getAddress().getIdAddress())
                    .bind("idCoupon", order.getIdCoupon() != null ? order.getIdCoupon() : null)
                    .bind("totalPrice", order.getTotalPrice())
                    .bind("status", order.getStatus());

            int orderId = update.executeAndReturnGeneratedKeys("idOrder")
                    .mapTo(Integer.class)
                    .findOne()
                    .orElseThrow(() -> new RuntimeException("Failed to insert order"));

            return orderId;
        });
    }

    // Lấy đơn hàng theo idOrder
    public Order getOrderById(int idOrder) {
        String sqlOrderById = """
        SELECT o.idOrder, o.idUser, o.idAddress, o.idCoupon, o.totalPrice, o.status, o.createAt, o.updateAt,
               u.fullName, u.email,
               a.address, a.ward, a.district, a.province 
        FROM orders o
        LEFT JOIN users u ON o.idUser = u.idUser
        LEFT JOIN address a ON o.idAddress = a.idAddress
        WHERE o.idOrder = :idOrder
    """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sqlOrderById)
                        .bind("idOrder", idOrder)
                        .map(new OrderMapper())  // Sử dụng OrderMapper để ánh xạ
                        .findOne()
                        .orElse(null)
        );
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                SELECT o.idOrder, o.totalPrice, o.status, o.createAt, o.updateAt,
                       u.idUser, u.fullName, u.email,
                       a.idAddress, a.address, a.ward, a.district, a.province
                FROM orders o
                LEFT JOIN users u ON o.idUser = u.idUser
                LEFT JOIN address a ON o.idAddress = a.idAddress
            """)
                        .map(new OrderMapper())  // Sử dụng OrderMapper để ánh xạ
                        .list()
        );
    }

    // Cập nhật trạng thái đơn hàng
    public void updateOrderStatus(int orderId, String status) {
        jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE orders SET status = :status, updatedAt = NOW() WHERE idOrder = :idOrder")
                        .bind("status", status)
                        .bind("idOrder", orderId)
                        .execute()
        );
    }

    public boolean deleteOrder(int orderId) {
        return jdbi.withHandle(handle -> {
            // Xóa dữ liệu trong payments trước
            handle.createUpdate("DELETE FROM payments WHERE idOrder = :idOrder")
                    .bind("idOrder", orderId)
                    .execute();

            // Xóa dữ liệu trong order_details
            handle.createUpdate("DELETE FROM orderdetail WHERE idOrder = :idOrder")
                    .bind("idOrder", orderId)
                    .execute();

            // Cuối cùng mới xóa order
            int deleted = handle.createUpdate("DELETE FROM orders WHERE idOrder = :idOrder")
                    .bind("idOrder", orderId)
                    .execute();

            return deleted > 0;
        });
    }



    // Xóa tất cả đơn hàng của người dùng
    public void deleteOrdersByUserId(int userId) {
        jdbi.withHandle(handle -> {
            // Xóa tất cả các bản ghi trong bảng order_details
            handle.createUpdate("DELETE FROM order_details WHERE idOrder IN (SELECT idOrder FROM orders WHERE idUser = :userId)")
                    .bind("userId", userId)
                    .execute();

            // Xóa tất cả các đơn hàng của người dùng
            handle.createUpdate("DELETE FROM orders WHERE idUser = :userId")
                    .bind("userId", userId)
                    .execute();
            return null;
        });
    }

    // Lấy các đơn hàng theo trạng thái
    public List<Order> getOrdersByStatus(String status) {
        String sqlOrdersByStatus = """
        SELECT o.idOrder, o.idUser, o.idAddress, o.idCoupon, o.totalPrice, o.status, o.createAt, o.updateAt,
               u.fullName, u.email,
               a.address, a.ward, a.district, a.province 
        FROM orders o
        LEFT JOIN users u ON o.idUser = u.idUser
        LEFT JOIN address a ON o.idAddress = a.idAddress
        WHERE o.status = :status ORDER BY o.createAt DESC
    """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sqlOrdersByStatus)
                        .bind("status", status)
                        .map(new OrderMapper())  // Sử dụng OrderMapper để ánh xạ
                        .list()
        );
    }

    // Lấy các đơn hàng của người dùng theo userId
    public List<Order> getOrdersByUserId(int userId) {
        String sqlOrdersByUserId = """
        SELECT o.idOrder, o.idUser, o.idAddress, o.idCoupon, o.totalPrice, o.status, o.createAt, o.updateAt,
               u.fullName, u.email,
               a.address, a.ward, a.district, a.province 
        FROM orders o
        LEFT JOIN users u ON o.idUser = u.idUser
        LEFT JOIN address a ON o.idAddress = a.idAddress
        WHERE o.idUser = :userId ORDER BY o.createAt DESC
    """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sqlOrdersByUserId)
                        .bind("userId", userId)
                        .map(new OrderMapper())  // Sử dụng OrderMapper để ánh xạ
                        .list()
        );
    }

}
