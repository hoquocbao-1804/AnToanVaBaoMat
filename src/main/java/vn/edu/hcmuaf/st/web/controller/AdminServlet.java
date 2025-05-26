package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.st.web.entity.Role;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;
import vn.edu.hcmuaf.st.web.util.PermissionUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(urlPatterns = "/admin")
public class AdminServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AdminServlet.class.getName());
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Processing GET request for /admin");
        commonSettings(request, response);
        if (!PermissionUtil.hasRole(request, Role.RoleName.ROLE_ADMIN)) {
            LOGGER.warning("Unauthorized access attempt to /admin");
            request.setAttribute("error", "Bạn không có quyền truy cập trang này.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }
        loadAdminPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.info("Processing POST request for /admin");
        commonSettings(request, response);
        if (!PermissionUtil.hasRole(request, Role.RoleName.ROLE_ADMIN)) {
            LOGGER.warning("Unauthorized action attempt on /admin");
            request.setAttribute("error", "Bạn không có quyền thực hiện hành động này.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            loadAdminPage(request, response);
            return;
        }

        if ("updateRole".equals(action)) {
            try {
                String userIdParam = request.getParameter("userId");
                String roleStr = request.getParameter("newRole");

                if (userIdParam == null || roleStr == null || userIdParam.isEmpty() || roleStr.isEmpty()) {
                    request.setAttribute("error", "Thông tin người dùng hoặc vai trò không hợp lệ.");
                    loadAdminPage(request, response);
                    return;
                }

                int userId = Integer.parseInt(userIdParam);
                Role.RoleName newRole = Role.RoleName.valueOf(roleStr);
                boolean success = accountService.updateUserRole(userId, newRole);

                if (success) {
                    LOGGER.info("Updated role for user ID " + userId + " to " + newRole);
                    request.setAttribute("message", "Cập nhật vai trò thành công.");
                } else {
                    LOGGER.warning("Failed to update role for user ID " + userId);
                    request.setAttribute("error", "Cập nhật vai trò thất bại.");
                }
            } catch (NumberFormatException e) {
                LOGGER.severe("Invalid user ID format: " + e.getMessage());
                request.setAttribute("error", "ID người dùng không hợp lệ.");
            } catch (IllegalArgumentException e) {
                LOGGER.severe("Invalid role: " + e.getMessage());
                request.setAttribute("error", "Vai trò không hợp lệ.");
            } catch (Exception e) {
                LOGGER.severe("Error updating role: " + e.getMessage());
                request.setAttribute("error", "Lỗi hệ thống khi cập nhật vai trò.");
            }
        } else {
            request.setAttribute("error", "Hành động không được hỗ trợ.");
        }

        loadAdminPage(request, response);
    }

    private void loadAdminPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            LOGGER.info("Loading admin page data");
            List<User> users = accountService.getRecentUsers();
            Map<String, Double> revenueData = accountService.getRevenueLastSixMonths();

            // Xử lý trường hợp dữ liệu null
            request.setAttribute("users", users != null ? users : Collections.emptyList());
            request.setAttribute("revenueData", revenueData != null ? revenueData : Collections.emptyMap());

            LOGGER.info("Forwarding to /view/view-account/admin.jsp");
            request.getRequestDispatcher("/view/view-account/admin.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.severe("Error loading admin page: " + e.getMessage());
            request.setAttribute("error", "Không thể tải trang quản trị: " + e.getMessage());
            // Nếu admin.jsp không tồn tại, thử chuyển tới một trang lỗi hoặc thông báo
            try {
                request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            } catch (Exception forwardError) {
                LOGGER.severe("Error forwarding to signin.jsp: " + forwardError.getMessage());
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy trang quản trị.");
            }
        }
    }

    private void commonSettings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }
}