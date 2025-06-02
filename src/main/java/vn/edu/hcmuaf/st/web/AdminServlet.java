package vn.edu.hcmuaf.st.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.st.web.entity.Cart;
import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.entity.Address;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "adminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    // Danh sách tạm thời để lưu đơn hàng (thay thế bằng cơ sở dữ liệu trong thực tế)
    private static List<Order> orders = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Truyền danh sách đơn hàng vào JSP để hiển thị
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/view-admin/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Giả sử bạn lấy được Cart từ session hoặc tham số
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if (cart != null && !cart.getCartItems().isEmpty()) {
            // Lấy thông tin từ request (giả sử bạn gửi thông tin user và address từ form)
            String username = req.getParameter("username");
            String addressDetails = req.getParameter("address");

            // Tạo đối tượng User và Address (giả lập, bạn cần thay bằng dữ liệu thực tế)
            User user = new User();
            user.setUsername(username); // Cần có setter trong class User

            Address address = new Address();
            address.setDetails(addressDetails); // Cần có setter trong class Address

            // Tạo Order từ Cart
            Order order = new Order(
                    orders.size() + 1, // ID đơn hàng (tạm thời, thay bằng ID từ database nếu có)
                    user,
                    address,
                    cart.getFinalTotal(), // Tổng tiền cuối cùng từ Cart
                    "Chờ xử lý" // Trạng thái ban đầu
            );
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            // Thêm đơn hàng vào danh sách
            orders.add(order);

            // Xóa giỏ hàng sau khi đặt hàng thành công (tùy chọn)
            req.getSession().removeAttribute("cart");
        }

        // Chuyển hướng lại trang admin để hiển thị danh sách đơn hàng
        resp.sendRedirect(req.getContextPath() + "/admin");
    }
}