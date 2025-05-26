package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;

import java.io.IOException;

@WebServlet(urlPatterns = {"/sign", "/register", "/forgot-password", "/enter-otp", "/reset-password"})
public class AuthController extends HttpServlet {
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        commonSettings(request, response);
        String action = request.getServletPath();
        switch (action) {
            case "/register":
                request.getRequestDispatcher("/view/view-account/register.jsp").forward(request, response);
                break;
            case "/forgot-password":
                request.getRequestDispatcher("/view/view-account/forgot-password.jsp").forward(request, response);
                break;
            case "/enter-otp":
                request.getRequestDispatcher("/view/view-account/enter-otp.jsp").forward(request, response);
                break;
            case "/reset-password":
                request.getRequestDispatcher("/view/view-account/reset-password.jsp").forward(request, response);
                break;
            case "/sign":
            default:
                request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        commonSettings(request, response);
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
            default:
                response.sendRedirect("/sign");
                break;
        }
    }

    private void commonSettings(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }

        if (accountService.login(username, password)) {
            HttpSession session = request.getSession();
            User user = accountService.getUserByUsername(username);

            if (user != null) {
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullname", user.getFullName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("phoneNumber", user.getPhoneNumber());
                session.setAttribute("birthDate", user.getBirthDate());
                session.setAttribute("image", user.getImage());
                session.setAttribute("user", user);
                System.out.println(">> Logged in user ID = " + user.getIdUser() + ", Role = " + user.getRole());

                // Redirect based on role
                if (user.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            }
        } else {
            request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
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

        if (username == null || password == null || confirmPassword == null ||
                fullname == null || email == null || phoneNumber == null ||
                username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                fullname.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin!");
            request.getRequestDispatcher("/view/view-account/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu xác nhận không trùng khớp!");
            request.getRequestDispatcher("/view/view-account/register.jsp").forward(request, response);
            return;
        }

        boolean isRegistered = accountService.register(username, password, fullname, email, phoneNumber);
        if (isRegistered) {
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Tên người dùng đã tồn tại hoặc lỗi hệ thống!");
            request.getRequestDispatcher("/view/view-account/register.jsp").forward(request, response);
        }
    }

    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userEmail = request.getParameter("email");
        HttpSession session = request.getSession();

        if (userEmail == null || userEmail.trim().isEmpty()) {
            response.sendRedirect("forgot-password.jsp?error=Invalid Email");
            return;
        }

        int otpValue = accountService.generateOTP();
        try {
            accountService.sendOTP(userEmail, otpValue);
            request.setAttribute("message", "OTP đã được gửi tới email của bạn.");
            session.setAttribute("otp", otpValue);
            session.setAttribute("email", userEmail);
            request.getRequestDispatcher("/view/view-account/enter-otp.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi gửi OTP qua email", e);
        }
    }

    private void handleOtpValidation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String otpInput = request.getParameter("otp");
        HttpSession session = request.getSession();
        Integer otp = (Integer) session.getAttribute("otp");

        if (otpInput == null || otp == null) {
            request.setAttribute("message", "OTP không hợp lệ!");
            request.getRequestDispatcher("/view/view-account/enter-otp.jsp").forward(request, response);
            return;
        }

        try {
            int value = Integer.parseInt(otpInput);
            if (value == otp) {
                request.setAttribute("email", session.getAttribute("email"));
                request.setAttribute("status", "success");
                request.getRequestDispatcher("/view/view-account/reset-password.jsp").forward(request, response);
            } else {
                request.setAttribute("message", "OTP không đúng!");
                request.getRequestDispatcher("/view/view-account/enter-otp.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("message", "OTP phải là số!");
            request.getRequestDispatcher("/view/view-account/enter-otp.jsp").forward(request, response);
        }
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confPassword");
        String email = (String) session.getAttribute("email");

        if (password == null || email == null || !password.equals(confPassword)) {
            request.setAttribute("status", "invalidInput");
            request.getRequestDispatcher("/view/view-account/reset-password.jsp").forward(request, response);
            return;
        }

        boolean isUpdated = accountService.updatePassword(email, password);
        if (isUpdated) {
            request.setAttribute("status", "resetSuccess");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
        } else {
            request.setAttribute("status", "resetFailed");
            request.getRequestDispatcher("/view/view-account/reset-password.jsp").forward(request, response);
        }
    }
}