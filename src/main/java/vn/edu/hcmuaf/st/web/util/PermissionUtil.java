package vn.edu.hcmuaf.st.web.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.st.web.entity.Role;
import vn.edu.hcmuaf.st.web.entity.User;

public class PermissionUtil {
    public static boolean hasRole(HttpServletRequest request, Role.RoleName requiredRole) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            System.out.println("Session is null");
            return false;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("User is null in session");
            return false;
        }
        System.out.println("User role: " + user.getRole());
        return user.getRole() == requiredRole;
    }

    public static boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }
}