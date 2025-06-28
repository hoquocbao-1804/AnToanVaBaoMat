package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import vn.edu.hcmuaf.st.web.dao.OrderDao;
import vn.edu.hcmuaf.st.web.dao.OrderDetailDao;
import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.OrderDetail;
import vn.edu.hcmuaf.st.web.entity.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/order-history")
public class HistoryServlet extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/view/view-account/signin.jsp");
            return;
        }

        // Lấy danh sách đơn hàng của người dùng từ database
        List<Order> orderList = orderDao.getOrdersByUserId(user.getIdUser());
        System.out.println("Đơn hàng lấy được: " + orderList.size());// Sửa đây
        // Đặt danh sách đơn hàng vào request
        req.setAttribute("orderList", orderList);

        // Chuyển tiếp đến trang JSP để hiển thị dữ liệu
        req.getRequestDispatcher("/view/view-order/order-history.jsp").forward(req, resp);
    }
}