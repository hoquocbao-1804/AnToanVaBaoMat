package vn.edu.hcmuaf.st.web.dao;

import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.entity.Address;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {
    @Override

    public Order map(ResultSet rs, StatementContext ctx) throws SQLException {
        Order order = new Order();

        order.setIdOrder(rs.getInt("idOrder"));
        order.setTotalPrice(rs.getDouble("totalPrice"));
        order.setStatus(rs.getString("status"));
        order.setCreatedAt(rs.getTimestamp("createAt").toLocalDateTime());

        // Kiểm tra xem có cột updateAt không
        if (rs.getTimestamp("updateAt") != null) {
            order.setUpdatedAt(rs.getTimestamp("updateAt").toLocalDateTime());
        }

        // Ánh xạ thông tin người dùng
        User user = new User();
        user.setIdUser(rs.getInt("idUser"));
        user.setFullName(rs.getString("fullName"));
        user.setEmail(rs.getString("email"));
        order.setUser(user);

        // Ánh xạ thông tin địa chỉ
        Address address = new Address();
        address.setAddress(rs.getString("address"));
        address.setWard(rs.getString("ward"));
        address.setDistrict(rs.getString("district"));
        address.setProvince(rs.getString("province"));
        order.setAddress(address);
        order.setSignatureStatus(rs.getString("signature_status"));
        order.setDigitalSignature(rs.getString("digital_signature"));
        return order;
    }

}