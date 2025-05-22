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
//
@WebServlet(urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!PermissionUtil.isAuthenticated(request)) {
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        User user = accountService.getUserByUsernameAndAddress(username);
        if (user != null) {
            session.setAttribute("user", user);
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy người dùng.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ... (code từ handleProfileUpdate)
    }
}
