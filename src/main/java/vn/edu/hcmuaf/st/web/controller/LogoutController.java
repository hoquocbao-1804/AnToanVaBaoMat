package vn.edu.hcmuaf.st.web.controller;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.st.web.entity.Address;
import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.Role;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;
import vn.edu.hcmuaf.st.web.util.PermissionUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
@WebServlet(urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/view/view-account/signin.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}

