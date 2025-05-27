package vn.edu.hcmuaf.st.web.mapper;

import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.entity.Address;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class OrderMapper implements RowMapper<Order> {
    @Override
    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {
        Order order = new Order();

        // map đơn giản
        order.setIdOrder(rs.getInt("idOrder"));
        order.setTotalPrice(rs.getDouble("totalPrice"));
        order.setStatus(rs.getString("status"));

        // map thời gian
        order.setCreatedAt(rs.getTimestamp("createAt").toLocalDateTime());
        if (hasColumn(rs, "updateAt") && rs.getTimestamp("updateAt") != null) {
            order.setUpdatedAt(rs.getTimestamp("updateAt").toLocalDateTime());
        }


        // map user
        User user = new User();
        user.setIdUser(rs.getInt("idUser"));
        order.setUser(user);

        // map address
        Address address = new Address();
        address.setIdAddress(rs.getInt("idAddress"));
        order.setAddress(address);

        // coupon có thể null
        int couponId = rs.getInt("idCoupon");
        if (!rs.wasNull()) {
            order.setIdCoupon(couponId);
        }

        return order;
    }
    private boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
