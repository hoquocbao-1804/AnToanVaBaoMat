package vn.edu.hcmuaf.st.web.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.st.web.dao.db.JDBIConnect;
import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.OrderHistory;
import vn.edu.hcmuaf.st.web.mapper.OrderMapper;

import java.sql.PreparedStatement;
import java.util.List;

public class OrderHistoryDao {
    private final Jdbi jdbi;

    public OrderHistoryDao() {
        this.jdbi = JDBIConnect.get();
    }

    public List<Order> getOrdersByUserId(int userId) {
        String sql = """
                SELECT idOrder, idUser, idAddress, idCoupon, totalPrice, status, createAt FROM orders WHERE idUser = :userId ORDER BY createAt DESC
                """;
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .map(new OrderMapper())
                        .list()
        );
    }




    public void insertHistory(OrderHistory h) {
        String sql = """
            INSERT INTO order_history(order_id, user_id, status, changed_at, note)
            VALUES (:orderId, :userId, :status, :changedAt, :note)
        """;
        jdbi.useHandle(handle ->
                handle.createUpdate(sql)
                        .bind("orderId", h.getOrderId())
                        .bind("userId", h.getUserId())
                        .bind("status", h.getStatus())
                        .bind("changedAt", h.getChangedAt())
                        .bind("note", h.getNote())
                        .execute()
        );
    }

//    public List<OrderHistory> getHistoryByUserId(int userId) {
//        String sql = """
//            SELECT history_id, order_id, user_id, status, changed_at, note
//              FROM order_history
//             WHERE user_id = :userId
//          ORDER BY changed_at DESC
//        """;
//        return jdbi.withHandle(handle ->
//                handle.createQuery(sql)
//                        .bind("userId", userId)
//                        .mapToBean(OrderHistory.class)
//                        .list()
//        );
//    }
}
