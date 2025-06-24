package vn.edu.hcmuaf.st.web.dao;

import org.jdbi.v3.core.Jdbi;
import vn.edu.hcmuaf.st.web.dao.db.JDBIConnect;
import vn.edu.hcmuaf.st.web.entity.OrderHistory;

import java.util.List;

public class OrderHistoryDao {
    private final Jdbi jdbi;

    public OrderHistoryDao() {
        this.jdbi = JDBIConnect.get();
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

}