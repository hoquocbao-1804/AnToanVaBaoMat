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
        List<Order> orders = orderDao.getAllOrders();
        System.out.println("Danh sách đơn hàng: " + orders);
        if (orders == null || orders.isEmpty()) {
            System.out.println("Không có đơn hàng nào được lấy từ cơ sở dữ liệu!");
        }
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/view-admin/admin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("updateStatus".equals(action)) {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String newStatus = req.getParameter("status");

            // Cập nhật trạng thái đơn hàng trong database
            orderDao.updateOrderStatus(orderId, newStatus);

            // Trả về phản hồi thành công
            resp.setStatus(HttpServletResponse.SC_OK);
        } else if ("deleteOrder".equals(action)) {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            orderDao.deleteOrder(orderId);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}