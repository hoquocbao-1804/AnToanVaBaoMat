package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.st.web.entity.Role;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;
import vn.edu.hcmuaf.st.web.util.PermissionUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/admin")
public class AdminServlet extends HttpServlet {
    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        commonSettings(request, response);

        if (!PermissionUtil.hasRole(request, Role.RoleName.ROLE_ADMIN)) {
            request.setAttribute("error", "Bạn không có quyền thực hiện hành động này.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");

        // Nếu không có action, hiểu là truy cập trang admin
        if (action == null || action.isEmpty()) {
            loadAdminPage(request, response);
            return;
        }

        // Nếu có action cụ thể
        if ("updateRole".equals(action)) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String roleStr = request.getParameter("newRole");
                Role.RoleName newRole = Role.RoleName.valueOf(roleStr);

                boolean success = accountService.updateUserRole(userId, newRole);

                if (success) {
                    request.setAttribute("message", "Cập nhật vai trò thành công.");
                } else {
                    request.setAttribute("error", "Cập nhật vai trò thất bại.");
                }
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Vai trò không hợp lệ.");
            } catch (Exception e) {
                request.setAttribute("error", "Lỗi hệ thống khi cập nhật vai trò.");
            }
        }

        // Sau khi xử lý, load lại dữ liệu trang admin
        loadAdminPage(request, response);
    }

    private void loadAdminPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<User> users = accountService.getRecentUsers();
            Map<String, Double> revenueData = accountService.getRevenueLastSixMonths();
            request.setAttribute("users", users);
            request.setAttribute("revenueData", revenueData);
            request.getRequestDispatcher("/admin.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Lỗi khi load trang admin: " + e.getMessage());
            request.setAttribute("error", "Không thể tải trang quản trị.");
            request.getRequestDispatcher("/view/view-account/error.jsp").forward(request, response);
        }
    }

    private void commonSettings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }
}
