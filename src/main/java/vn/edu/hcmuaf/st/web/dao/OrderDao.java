package vn.edu.hcmuaf.st.web.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Update;
import vn.edu.hcmuaf.st.web.dao.db.JDBIConnect;
import vn.edu.hcmuaf.st.web.entity.Order;

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

    public int insertOrder(Order order) {
        String sql = "INSERT INTO orders (idUser, idAddress, idCoupon, totalPrice, status) VALUES (:idUser, :idAddress, :idCoupon, :totalPrice, :status)";
        return jdbi.withHandle(handle -> {
            Update update = handle.createUpdate(sql)
                    .bind("idUser", order.getUser().getIdUser())
                    .bind("idAddress", order.getAddress().getIdAddress())
                    .bind("idCoupon", order.getIdCoupon())
                    .bind("totalPrice", order.getTotalPrice())
                    .bind("status", order.getStatus());

            return update.executeAndReturnGeneratedKeys("idOrder")
                    .mapTo(Integer.class)
                    .findOne()
                    .orElseThrow(() -> new RuntimeException("Failed to insert order"));
        });
    }

    public Order getOrderById(int idOrder) {
        String sql = """
            SELECT o.idOrder, o.idUser, o.idAddress, o.idCoupon, o.totalPrice, o.status, o.createAt, o.updateAt,
                   u.fullName, u.email,
                   a.address, a.ward, a.district, a.province, o.signature_status, o.digital_signature
            FROM orders o
            LEFT JOIN users u ON o.idUser = u.idUser
            LEFT JOIN address a ON o.idAddress = a.idAddress
            WHERE o.idOrder = :idOrder
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("idOrder", idOrder)
                        .map(new OrderMapper())
                        .findOne()
                        .orElse(null)
        );
    }

    public List<Order> getAllOrders() {
        String sql = """
            SELECT o.idOrder, o.totalPrice, o.status, o.createAt, o.updateAt,
                   u.idUser, u.fullName, u.email,
                   a.idAddress, a.address, a.ward, a.district, a.province, o.signature_status, o.digital_signature
            FROM orders o
            LEFT JOIN users u ON o.idUser = u.idUser
            LEFT JOIN address a ON o.idAddress = a.idAddress
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .map(new OrderMapper())
                        .list()
        );
    }

    public void updateOrderStatus(int orderId, String status) {
        jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE orders SET status = :status, updateAt = NOW() WHERE idOrder = :idOrder")
                        .bind("status", status)
                        .bind("idOrder", orderId)
                        .execute()
        );
    }

    public void deleteOrder(int orderId) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM payments WHERE idOrder = :idOrder")
                    .bind("idOrder", orderId)
                    .execute();

            handle.createUpdate("DELETE FROM orderdetail WHERE idOrder = :idOrder")
                    .bind("idOrder", orderId)
                    .execute();

            handle.createUpdate("DELETE FROM orders WHERE idOrder = :idOrder")
                    .bind("idOrder", orderId)
                    .execute();
        });
    }

    public List<Order> getOrdersByStatus(String status) {
        String sql = """
            SELECT o.idOrder, o.idUser, o.idAddress, o.idCoupon, o.totalPrice, o.status, o.createAt, o.updateAt,
                   u.fullName, u.email,
                   a.address, a.ward, a.district, a.province
            FROM orders o
            LEFT JOIN users u ON o.idUser = u.idUser
            LEFT JOIN address a ON o.idAddress = a.idAddress
            WHERE o.status = :status ORDER BY o.createAt DESC
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("status", status)
                        .map(new OrderMapper())
                        .list()
        );
    }

    public List<Order> getOrdersByUserId(int userId) {
        String sql = """
            SELECT o.idOrder, o.idUser, o.idAddress, o.idCoupon, o.totalPrice, o.status, o.createAt, o.updateAt,
                   u.fullName, u.email,
                   a.address, a.ward, a.district, a.province, o.signature_status, o.digital_signature
            FROM orders o
            LEFT JOIN users u ON o.idUser = u.idUser
            LEFT JOIN address a ON o.idAddress = a.idAddress
            WHERE o.idUser = :userId ORDER BY o.createAt DESC
        """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .map(new OrderMapper())
                        .list()
        );
    }

    public boolean updateSignature(int orderId, String signature) {
        String sql = "UPDATE orders SET digital_signature = :signature, signature_status = 'Đã ký' WHERE idOrder = :orderId";

        return jdbi.withHandle(handle ->
                handle.createUpdate(sql)
                        .bind("signature", signature)
                        .bind("orderId", orderId)
                        .execute() > 0
        );
    }
}
