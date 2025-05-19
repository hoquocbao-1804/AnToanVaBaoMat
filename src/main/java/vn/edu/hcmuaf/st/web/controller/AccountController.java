package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import vn.edu.hcmuaf.st.web.entity.Address;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

@WebServlet(urlPatterns = {"/sign", "/register", "/forgot-password", "/enter-otp", "/login", "/reset-password", "/logout", "/profile", "/admin"})
public class AccountController extends HttpServlet {
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
            case "/sign":
                request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
                break;
            case "/reset-password":
                request.getRequestDispatcher("/view/view-account/reset-password.jsp").forward(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/profile":
                viewProfile(request, response);
                break;
            case "/admin":
                handleAdminDashboard(request, response); // New Admin endpoint
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
            case "/logout":
                handleLogout(request, response);
                break;
            case "/profile":
                handleProfileUpdate(request, response);
                break;
            case "/admin":
                handleAdminActions(request, response); // Handle Admin actions (e.g., update roles)
                break;
            default:
                response.sendRedirect("/sign");
                break;
        }
    }

    private void commonSettings(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
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
                session.setAttribute("idRole", user.getIdRole()); // Store role in session
                session.setAttribute("user", user);
                System.out.println(">> Logged in user ID = " + user.getIdUser() + ", Role = " + user.getIdRole());
                response.sendRedirect(request.getContextPath() + "/home");
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
        HttpSession mySession = request.getSession();

        if (userEmail == null || userEmail.trim().isEmpty()) {
            response.sendRedirect("forgot-password.jsp?error=Invalid Email");
            return;
        }

        int otpvalue = accountService.generateOTP();
        try {
            accountService.sendOTP(userEmail, otpvalue);
            request.setAttribute("message", "OTP đã được gửi tới email của bạn.");
            mySession.setAttribute("otp", otpvalue);
            mySession.setAttribute("email", userEmail);
            request.getRequestDispatcher("/view/view-account/enter-otp.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Lỗi khi gửi OTP qua email", e);
        }
    }

    public void handleOtpValidation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int value = Integer.parseInt(request.getParameter("otp"));
        HttpSession session = request.getSession();
        int otp = (int) session.getAttribute("otp");
        RequestDispatcher dispatcher;

        if (value == otp) {
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("status", "success");
            dispatcher = request.getRequestDispatcher("/view/view-account/reset-password.jsp");
        } else {
            request.setAttribute("message", "wrong otp");
            dispatcher = request.getRequestDispatcher("/view/view-account/enter-otp.jsp");
        }
        dispatcher.forward(request, response);
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confPassword");
        String email = (String) session.getAttribute("email");
        RequestDispatcher dispatcher;

        if (password == null || confPassword == null || email == null || !password.equals(confPassword)) {
            request.setAttribute("status", "invalidInput");
            dispatcher = request.getRequestDispatcher("/view/view-account/reset-password.jsp");
            dispatcher.forward(request, response);
            return;
        }

        boolean isUpdated = accountService.updatePassword(email, password);
        if (isUpdated) {
            request.setAttribute("status", "resetSuccess");
            dispatcher = request.getRequestDispatcher("/view/view-account/signin.jsp");
        } else {
            request.setAttribute("status", "resetFailed");
            dispatcher = request.getRequestDispatcher("/view/view-account/reset-password.jsp");
        }
        dispatcher.forward(request, response);
    }


    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(request.getContextPath() + "/view/view-account/signin.jsp");
    }

    private void handleProfileUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect("/sign");
            return;
        }
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
        if (birthDate == null || address == null || address.trim().isEmpty()) {
            request.setAttribute("error", "Ngày sinh hoặc địa chỉ không hợp lệ.");
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
            return;
        }
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
        if (success) {
            currentUser.setFullName(fullName);
            currentUser.setPhoneNumber(phoneNumber);
            currentUser.setEmail(email);
            currentUser.setBirthDate(String.valueOf(birthDate));
            if (currentUser.getAddress() == null) {
                currentUser.setAddress(new Address());
            }
            Address addr = currentUser.getAddress();
            addr.setAddress(address);
            addr.setWard(ward);
            addr.setDistrict(district);
            addr.setProvince(province);
            session.setAttribute("user", currentUser);
            session.setAttribute("idRole", currentUser.getIdRole()); // Update role in session
            request.setAttribute("user", currentUser);
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
        User user = accountService.getUserByUsernameAndAddress(username);
        if (user != null) {
            session.setAttribute("idRole", user.getIdRole()); // Ensure role is in session
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy người dùng.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
        }
    }

    private void handleAdminDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "Bạn không có quyền truy cập trang này.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }
        // Load admin dashboard (e.g., list of users)
        // For now, forward to a placeholder JSP
        request.getRequestDispatcher("/view/view-account/admin.jsp").forward(request, response);
    }

    private void handleAdminActions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            request.setAttribute("error", "Bạn không có quyền thực hiện hành động này.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
            return;
        }
        String action = request.getParameter("action");
        if ("updateRole".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int newRole = Integer.parseInt(request.getParameter("newRole"));
            boolean success = accountService.updateUserRole(userId, newRole);
            if (success) {
                request.setAttribute("message", "Cập nhật vai trò thành công.");
            } else {
                request.setAttribute("error", "Cập nhật vai trò thất bại.");
            }
        }
        request.getRequestDispatcher("/view/view-account/admin.jsp").forward(request, response);
    }
}