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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Thiết lập mã hóa và loại nội dung
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        String action = req.getParameter("action");

        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số 'action'");
            return;
        }

        try {
            switch (action) {
                case "deleteOrder":
                    handleDeleteOrder(req, resp);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi server: " + e.getMessage());
        }
    }

    private void handleDeleteOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderIdStr = req.getParameter("orderId");

        System.out.println("[AdminServlet] Nhận yêu cầu xóa đơn hàng với orderId = " + orderIdStr);

        if (orderIdStr == null || orderIdStr.isEmpty()) {
            System.out.println("[AdminServlet] orderId null hoặc rỗng.");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu 'orderId'");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            boolean deleted = orderDao.deleteOrder(orderId);

            if (deleted) {
                System.out.println("[AdminServlet] Xóa đơn hàng thành công: ID = " + orderId);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\":\"Xóa thành công!\"}");
            } else {
                System.out.println("[AdminServlet] Xóa thất bại hoặc đơn hàng không tồn tại: ID = " + orderId);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Xóa thất bại hoặc đơn hàng không tồn tại");
            }
        } catch (NumberFormatException e) {
            System.out.println("[AdminServlet] orderId không hợp lệ: " + orderIdStr);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "orderId không hợp lệ");
        }
    }


}