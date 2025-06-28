package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.st.web.service.OrderService;

import java.io.IOException;

@WebServlet(name = "signOrder", urlPatterns = "/sign-order")
public class SignOrderController extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String signature = req.getParameter("signature");

        boolean updated = orderService.updateSignature(orderId, signature);

        if (updated) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"success\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"message\": \"failed\"}");
        }
    }
}