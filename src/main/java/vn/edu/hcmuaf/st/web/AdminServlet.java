package vn.edu.hcmuaf.st.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.st.web.dao.OrderDao;
import vn.edu.hcmuaf.st.web.entity.Order;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "adminServlet", value = "/admin")
public class AdminServlet extends HttpServlet {
    private final OrderDao orderDao = new OrderDao(); // Khởi tạo orderDao

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Lấy tất cả các đơn hàng
        List<Order> orders = orderDao.getAllOrders();

        // Kiểm tra xem có đơn hàng nào được lấy từ cơ sở dữ liệu không
        if (orders == null || orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào được lấy từ cơ sở dữ liệu!");
            req.setAttribute("message", "Không có đơn hàng nào!");
        } else {
            System.out.println("Danh sách đơn hàng: " + orders);
            req.setAttribute("orders", orders);
        }

        // Chuyển tiếp đến trang admin.jsp để hiển thị danh sách đơn hàng
        req.getRequestDispatcher("/view-admin/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("updateStatus".equals(action)) {
            // Cập nhật trạng thái đơn hàng
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String newStatus = req.getParameter("status");

            // Cập nhật trạng thái đơn hàng trong database
            orderDao.updateOrderStatus(orderId, newStatus);

            // Chuyển hướng về trang quản lý đơn hàng sau khi cập nhật trạng thái
            resp.sendRedirect(req.getContextPath() + "/admin");  // Điều hướng lại đến trang admin
        } else if ("deleteOrder".equals(action)) {
            // Xóa đơn hàng
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            orderDao.deleteOrder(orderId);

            // Chuyển hướng về trang quản lý đơn hàng sau khi xóa
            resp.sendRedirect(req.getContextPath() + "/admin");  // Điều hướng lại đến trang admin
        }
    }
}