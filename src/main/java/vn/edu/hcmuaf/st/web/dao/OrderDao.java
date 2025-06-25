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

            return update.executeAndReturnGeneratedKeys("idOrder")
                    .mapTo(Integer.class)
                    .findOne()
                    .orElseThrow(() -> new RuntimeException("Failed to insert order"));
        });
    }

    public Order getOrderById(int idOrder) {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                        SELECT 
                            o.idOrder, o.totalPrice, o.status, o.createAt,
                            u.idUser, u.fullName, u.email,
                            a.idAddress, a.address, a.ward, a.district, a.province
                        FROM orders o
                        LEFT JOIN users u ON o.idUser = u.idUser
                        LEFT JOIN address a ON o.idAddress = a.idAddress
                        WHERE o.idOrder = :idOrder
                        """)
                        .bind("idOrder", idOrder)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            order.setIdOrder(rs.getInt("idOrder"));
                            order.setTotalPrice(rs.getDouble("totalPrice"));
                            order.setStatus(rs.getString("status"));
                            order.setCreatedAt(rs.getTimestamp("createAt") != null ? rs.getTimestamp("createAt").toLocalDateTime() : null);

                            User user = new User();
                            user.setIdUser(rs.getInt("idUser"));
                            user.setFullName(rs.getString("fullName"));
                            user.setEmail(rs.getString("email"));
                            order.setUser(user);

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

    public List<Order> getAllOrders() {
        return jdbi.withHandle(handle ->
                handle.createQuery("""
                        SELECT 
                            o.idOrder, o.totalPrice, o.status, o.createAt,
                            u.idUser, u.fullName, u.email,
                            a.idAddress, a.address, a.ward, a.district, a.province
                        FROM orders o
                        LEFT JOIN users u ON o.idUser = u.idUser
                        LEFT JOIN address a ON o.idAddress = a.idAddress
                        """)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            order.setIdOrder(rs.getInt("idOrder"));
                            order.setTotalPrice(rs.getDouble("totalPrice"));
                            order.setStatus(rs.getString("status"));
                            order.setCreatedAt(rs.getTimestamp("createAt") != null ? rs.getTimestamp("createAt").toLocalDateTime() : null);

                            User user = new User();
                            user.setIdUser(rs.getInt("idUser"));
                            user.setFullName(rs.getString("fullName"));
                            user.setEmail(rs.getString("email"));
                            order.setUser(user);

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

    public void updateOrderStatus(int orderId, String status) {
        jdbi.withHandle(handle ->
                handle.createUpdate("UPDATE orders SET status = :status, updatedAt = NOW() WHERE idOrder = :idOrder")
                        .bind("status", status)
                        .bind("idOrder", orderId)
                        .execute()
        );
    }

    public boolean deleteOrder(int orderId) {
        try {
            return jdbi.withHandle(handle -> {
                int detailRows = handle.createUpdate("DELETE FROM order_details WHERE idOrder = :idOrder")
                        .bind("idOrder", orderId)
                        .execute();

                int orderRows = handle.createUpdate("DELETE FROM orders WHERE idOrder = :idOrder")
                        .bind("idOrder", orderId)
                        .execute();

                System.out.println("Xoá chi tiết đơn hàng: " + detailRows + ", Xoá đơn hàng: " + orderRows);
                return orderRows > 0;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        String sql = """
                SELECT 
                    o.idOrder, o.totalPrice, o.status, o.createAt,
                    u.idUser, u.fullName, u.email
                FROM orders o
                JOIN users u ON o.idUser = u.idUser
                WHERE o.idUser = :userId
                ORDER BY o.createAt DESC
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .map((rs, ctx) -> {
                            Order order = new Order();
                            order.setIdOrder(rs.getInt("idOrder"));
                            order.setTotalPrice(rs.getDouble("totalPrice"));
                            order.setStatus(rs.getString("status"));
                            order.setCreatedAt(rs.getTimestamp("createAt") != null ? rs.getTimestamp("createAt").toLocalDateTime() : null);

                            User user = new User();
                            user.setIdUser(rs.getInt("idUser"));
                            user.setFullName(rs.getString("fullName"));
                            user.setEmail(rs.getString("email"));
                            order.setUser(user);

                            return order;
                        }).list()
        );
    }
}
