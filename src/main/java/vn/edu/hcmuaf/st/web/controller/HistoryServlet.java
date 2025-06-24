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
    private final OrderDetailDao detailDao = new OrderDetailDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/view/view-account/signin.jsp");
            return;
        }
//        Xử lý sự kiện history , hiện đơn hàng


//        // 1. Lấy danh sách đơn hàng
//        List<Order> orders = orderDao.getOrdersByUserId(user.getIdUser());
//        // 2. Cho từng đơn order, lấy chi tiết
//        Map<Integer, List<OrderDetail>> detailsMap = new HashMap<>();
//        for (Order o : orders) {
//            detailsMap.put(o.getIdOrder(), detailDao.getDetailsByOrderId(o.getIdOrder()));
//        }
//
//        // 3. Đặt vào request
//        req.setAttribute("orders", orders);
//        req.setAttribute("detailsMap", detailsMap);

        // 4. Forward xuống JSP
        req.getRequestDispatcher("/view/view-order/order-history.jsp").forward(req, resp);
    }
}