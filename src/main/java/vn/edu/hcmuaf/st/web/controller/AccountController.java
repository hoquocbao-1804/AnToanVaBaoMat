package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmuaf.st.web.entity.GoogleAccount;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;

import java.io.IOException;

@WebServlet(urlPatterns = {
        "/sign", "/register", "/forgot-password", "/enter-otp",
        "/login", "/reset-password", "/logout", "/profile"
})
public class AccountController extends HttpServlet {
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
                    throw new RuntimeException(e);
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
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                if (user.getIdRole() == 1) {
                    response.sendRedirect(request.getContextPath() + "/view-admin/admin.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                request.setAttribute("error", "Tài khoản không tồn tại!");
                forward(request, response, "/view/view-account/signin.jsp");
            }
        } else {
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
            forward(request, response, "/view/view-account/signin.jsp");
        } else {
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
            throw new ServletException("Lỗi khi gửi OTP", e);
        }
    }

    private void handleOtpValidation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int enteredOtp = Integer.parseInt(request.getParameter("otp"));
        HttpSession session = request.getSession();
        int storedOtp = (int) session.getAttribute("otp");

        if (enteredOtp == storedOtp) {
            request.setAttribute("status", "success");
            forward(request, response, "/view/view-account/reset-password.jsp");
        } else {
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
            request.setAttribute("status", "resetSuccess");
            forward(request, response, "/view/view-account/signin.jsp");
        } else {
            request.setAttribute("status", "resetFailed");
            forward(request, response, "/view/view-account/reset-password.jsp");
        }
    }

    private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String code = request.getParameter("code");

        if (isEmpty(code)) {
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
            accountService.saveGoogleUser(user);
        }

        request.getSession().setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/home");
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        response.sendRedirect(request.getContextPath() + "/sign");
    }

    private void viewProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        forward(request, response, "/view/view-account/profile.jsp");
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Cập nhật thông tin hồ sơ người dùng
        forward(request, response, "/view/view-account/profile.jsp");
    }

    private boolean isEmpty(String... values) {
        for (String val : values) {
            if (val == null || val.trim().isEmpty()) return true;
        }
        return false;
    }
}
