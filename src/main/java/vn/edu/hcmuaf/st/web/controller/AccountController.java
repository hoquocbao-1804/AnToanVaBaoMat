package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.st.web.entity.Address;
import vn.edu.hcmuaf.st.web.entity.GoogleAccount;
import vn.edu.hcmuaf.st.web.entity.Role;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {
        "/sign", "/register", "/forgot-password", "/enter-otp",
        "/login", "/reset-password", "/logout", "/profile"
})
public class AccountController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AccountController.class.getName());
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUTF8(request, response);
        String action = request.getServletPath();

        switch (action) {
            case "/register":
                forward(request, response, "/view/view-account/register.jsp");
                break;
            case "/forgot-password":
                forward(request, response, "/view/view-account/forgot-password.jsp");
                break;
            case "/enter-otp":
                forward(request, response, "/view/view-account/enter-otp.jsp");
                break;
            case "/sign":
                forward(request, response, "/view/view-account/signin.jsp");
                break;
            case "/login":
                try {
                    handleGoogleLogin(request, response);
                } catch (Exception e) {
                    LOGGER.severe("Google login error: " + e.getMessage());
                    throw new ServletException("Lỗi đăng nhập Google", e);
                }
                break;
            case "/reset-password":
                forward(request, response, "/view/view-account/reset-password.jsp");
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/profile":
                viewProfile(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setUTF8(request, response);
        String action = request.getServletPath();

        switch (action) {
            case "/register":
                handleRegister(request, response);
                break;
            case "/sign":
                handleLogin(request, response);
                break;
            case "/forgot-password":
                handleForgotPassword(request, response);
                break;
            case "/enter-otp":
                handleOtpValidation(request, response);
                break;
            case "/reset-password":
                handleResetPassword(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/profile":
                handleProfileUpdate(request, response);
                break;
            default:
                response.sendRedirect("/sign");
        }
    }

    private void setUTF8(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (isEmpty(username) || isEmpty(password)) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            forward(request, response, "/view/view-account/signin.jsp");
            return;
        }

        if (accountService.login(username, password)) {
            User user = accountService.getUserByUsername(username);
            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);

                // Log user details for debugging
                LOGGER.info("User logged in: ID=" + user.getIdUser() + ", Username=" + user.getUsername() +
                        ", Role=" + (user.getRole() != null ? user.getRole() : "null"));

                // Check role for redirection
                String role = user.getRole();
                if (role == null) {
                    LOGGER.warning("Role is null for user ID: " + user.getIdUser() + ". Defaulting to USER role.");
                    role = "USER";
                }
                boolean isAdmin = "admin".equals(role);
                if (isAdmin) {
                    LOGGER.info("Redirecting to admin page for user ID: " + user.getIdUser());
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else {
                    LOGGER.info("Redirecting to home page for user ID: " + user.getIdUser());
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                LOGGER.warning("User not found after successful login: " + username);
                request.setAttribute("error", "Tài khoản không tồn tại!");
                forward(request, response, "/view/view-account/signin.jsp");
            }
        } else {
            LOGGER.warning("Login failed for username: " + username);
            request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
            forward(request, response, "/view/view-account/signin.jsp");
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        if (isEmpty(username, password, confirmPassword, fullname, email, phoneNumber)) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            forward(request, response, "/view/view-account/register.jsp");
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không trùng khớp!");
            forward(request, response, "/view/view-account/register.jsp");
            return;
        }

        boolean isRegistered = accountService.register(username, password, fullname, email, phoneNumber);
        if (isRegistered) {
            LOGGER.info("User registered successfully: " + username);
            forward(request, response, "/view/view-account/signin.jsp");
        } else {
            LOGGER.warning("Registration failed for username: " + username);
            request.setAttribute("error", "Tên người dùng đã tồn tại hoặc lỗi hệ thống!");
            forward(request, response, "/view/view-account/register.jsp");
        }
    }

    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userEmail = request.getParameter("email");

        if (isEmpty(userEmail)) {
            response.sendRedirect("forgot-password.jsp?error=Invalid Email");
            return;
        }

        int otp = accountService.generateOTP();
        try {
            accountService.sendOTP(userEmail, otp);
            HttpSession session = request.getSession();
            session.setAttribute("otp", otp);
            session.setAttribute("email", userEmail);
            request.setAttribute("message", "OTP đã được gửi tới email của bạn.");
            forward(request, response, "/view/view-account/enter-otp.jsp");
        } catch (Exception e) {
            LOGGER.severe("Error sending OTP: " + e.getMessage());
            throw new ServletException("Lỗi khi gửi OTP", e);
        }
    }

    private void handleOtpValidation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int enteredOtp;
        try {
            enteredOtp = Integer.parseInt(request.getParameter("otp"));
        } catch (NumberFormatException e) {
            request.setAttribute("message", "Mã OTP không hợp lệ.");
            forward(request, response, "/view/view-account/enter-otp.jsp");
            return;
        }

        HttpSession session = request.getSession();
        Integer storedOtp = (Integer) session.getAttribute("otp");

        if (storedOtp != null && enteredOtp == storedOtp) {
            request.setAttribute("status", "success");
            forward(request, response, "/view/view-account/reset-password.jsp");
        } else {
            LOGGER.warning("OTP validation failed for email: " + session.getAttribute("email"));
            request.setAttribute("message", "Mã OTP không chính xác.");
            forward(request, response, "/view/view-account/enter-otp.jsp");
        }
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confPassword");
        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");

        if (isEmpty(password, confPassword, email) || !password.equals(confPassword)) {
            request.setAttribute("status", "invalidInput");
            forward(request, response, "/view/view-account/reset-password.jsp");
            return;
        }

        boolean updated = accountService.updatePassword(email, password);
        if (updated) {
            LOGGER.info("Password reset successful for email: " + email);
            request.setAttribute("status", "resetSuccess");
            forward(request, response, "/view/view-account/signin.jsp");
        } else {
            LOGGER.warning("Password reset failed for email: " + email);
            request.setAttribute("status", "resetFailed");
            forward(request, response, "/view/view-account/reset-password.jsp");
        }
    }

    private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String code = request.getParameter("code");

        if (isEmpty(code)) {
            LOGGER.warning("Google login failed: missing code");
            response.sendRedirect(request.getContextPath() + "/view/view-account/signin.jsp?error=missing_code");
            return;
        }

        GoogleAccount googleAccount = accountService.handleGoogleLogin(code);
        User user = accountService.getUserByEmail(googleAccount.getEmail());

        if (user == null) {
            user = new User();
            user.setUsername(googleAccount.getUsername());
            user.setEmail(googleAccount.getEmail());
            user.setFullName(googleAccount.getFullName());
            user.setRole("USER");
            accountService.saveGoogleUser(user);
            LOGGER.info("New Google user saved: " + user.getEmail());
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
        LOGGER.info("Google login successful for user: " + user.getEmail());
        response.sendRedirect(request.getContextPath() + "/home");
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            LOGGER.info("User logged out: " + (session.getAttribute("user") != null ? ((User) session.getAttribute("user")).getUsername() : "unknown"));
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/sign");
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("/sign");
            return;
        }
        // Lấy thông tin từ form
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String ward = request.getParameter("ward");
        String district = request.getParameter("district");
        String province = request.getParameter("province");
        String birthDateStr = request.getParameter("birthDate");
        java.sql.Date birthDate = null;
        if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
            try {
                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthDateStr);
                birthDate = new java.sql.Date(utilDate.getTime());
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        // Kiểm tra dữ liệu bắt buộc
        if (birthDate == null || address == null || address.trim().isEmpty()) {
            request.setAttribute("error", "Ngày sinh hoặc địa chỉ không hợp lệ.");
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
            return;
        }
        // Cập nhật thông tin người dùng trong cơ sở dữ liệu
        AccountService service = new AccountService();
        boolean success = service.updateUserInfo(
                currentUser.getIdUser(),
                fullName,
                phoneNumber,
                email,
                address,
                ward,
                district,
                province,
                birthDate
        );
        // Nếu cập nhật thành công, cập nhật lại thông tin trong session
        if (success) {
            currentUser.setFullName(fullName);
            currentUser.setPhoneNumber(phoneNumber);
            currentUser.setEmail(email);
            currentUser.setBirthDate(String.valueOf(birthDate));
            // Cập nhật thông tin địa chỉ vào đối tượng Address nếu có
            if (currentUser.getAddress() == null) {
                currentUser.setAddress(new Address());
            }
            Address addr = currentUser.getAddress();
            addr.setAddress(address);
            addr.setWard(ward);
            addr.setDistrict(district);
            addr.setProvince(province);
            session.setAttribute("user", currentUser);
            request.setAttribute("user", currentUser); // để JSP đọc lại dữ liệu
            request.setAttribute("message", "Cập nhật thông tin thành công!");
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Cập nhật thất bại. Vui lòng thử lại.");
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
        }
    }
    private void viewProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }
        User user = accountService.getUserByUsername(username);

        if (user != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy người dùng.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
        }
    }
    private boolean isEmpty(String... values) {
        for (String val : values) {
            if (val == null || val.trim().isEmpty()) return true;
        }
        return false;
    }
}