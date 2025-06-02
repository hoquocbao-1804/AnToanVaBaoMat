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
        this.jdbi = JDBIConnect.get();
    }

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

    public Order getOrderById(int idOrder) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                                    SELECT 
                                        o.idOrder, o.totalPrice, o.status, o.createdAt,
                                        u.idUser, u.fullName, u.email,
                                        a.idAddress, a.address, a.ward, a.district, a.province
                                    FROM orders o
                                    JOIN users u ON o.idUser = u.idUser
                                    JOIN address a ON o.idAddress = a.idAddress
                                    WHERE o.idOrder = :idOrder
                                """)
                        .bind("idOrder", idOrder)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            order.setIdOrder(rs.getInt("idOrder"));
                            order.setTotalPrice(rs.getDouble("totalPrice"));
                            order.setStatus(rs.getString("status"));
                            order.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

                            // User
                            User user = new User();
                            user.setIdUser(rs.getInt("idUser"));
                            user.setFullName(rs.getString("fullName"));
                            user.setEmail(rs.getString("email"));
                            order.setUser(user);

                            // Address
                            Address address = new Address();
                            address.setIdAddress(rs.getInt("idAddress"));
                            address.setAddress(rs.getString("address"));
                            address.setWard(rs.getString("ward"));
                            address.setDistrict(rs.getString("district"));
                            address.setProvince(rs.getString("province"));
                            order.setAddress(address);

                            return order;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    // Thêm phương thức lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                                    SELECT 
                                        o.idOrder, o.totalPrice, o.status, o.createdAt,
                                        u.idUser, u.fullName, u.email,
                                        a.idAddress, a.address, a.ward, a.district, a.province
                                    FROM orders o
                                    JOIN users u ON o.idUser = u.idUser
                                    JOIN address a ON o.idAddress = a.idAddress
                                """)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            order.setIdOrder(rs.getInt("idOrder"));
                            order.setTotalPrice(rs.getDouble("totalPrice"));
                            order.setStatus(rs.getString("status"));
                            order.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());

                            // User
                            User user = new User();
                            user.setIdUser(rs.getInt("idUser"));
                            user.setFullName(rs.getString("fullName"));
                            user.setEmail(rs.getString("email"));
                            order.setUser(user);

                            // Address
                            Address address = new Address();
                            address.setIdAddress(rs.getInt("idAddress"));
                            address.setAddress(rs.getString("address"));
                            address.setWard(rs.getString("ward"));
                            address.setDistrict(rs.getString("district"));
                            address.setProvince(rs.getString("province"));
                            order.setAddress(address);

                            return order;
                        })
                        .list()
        );
    }

    // Thêm phương thức cập nhật trạng thái đơn hàng
    public void updateOrderStatus(int orderId, String status) {
        jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE orders SET status = :status, updatedAt = NOW() WHERE idOrder = :idOrder")
                        .bind("status", status)
                        .bind("idOrder", orderId)
                        .execute()
        );
    }
}