package vn.edu.hcmuaf.st.web.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;


import vn.edu.hcmuaf.st.web.entity.Address;
import vn.edu.hcmuaf.st.web.entity.GoogleAccount;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.text.SimpleDateFormat;

@WebServlet(urlPatterns = {"/sign", "/register", "/forgot-password", "/enter-otp", "/login", "/reset-password", "/logout", "/profile"})
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
            case "/login":
                handleGoogleLogin(request, response);
                break;
            case "/reset-password":
                request.getRequestDispatcher("/view/view-account/reset-password.jsp").forward(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/profile":
                viewProfile(request, response);  // Hiển thị thông tin người dùng
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
            default:
                response.sendRedirect("/sign");
                break;
        }
    }

    // UTF8
    private void commonSettings(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
    }

    // Đăng Nhập
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

            // Lấy toàn bộ thông tin user từ database
            User user = accountService.getUserByUsername(username);

            if (user != null) {
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullname", user.getFullName());
                session.setAttribute("email", user.getEmail());
                session.setAttribute("password", user.getPassword());
                session.setAttribute("phoneNumber", user.getPhoneNumber());// Chú ý: Không nên lưu mật khẩu vào session!
                session.setAttribute("birthDate", user.getBirthDate());
                session.setAttribute("image", user.getImage());
            }

            session.setAttribute("user", user);

            //check
            System.out.println(">> Logged in user ID = " + user.getIdUser());

            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            request.setAttribute("error", "Tài khoản hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
        }
    }


    // Đăng Ký
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

    // Quên MK
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

    // Xử lý xác thực OTP
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

    // Tạo Mk mới
    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String password = request.getParameter("password");
        String confPassword = request.getParameter("confPassword");
        String email = (String) session.getAttribute("email");
        RequestDispatcher dispatcher;

        // Kiểm tra các trường mật khẩu và xác nhận mật khẩu
        if (password == null || confPassword == null || email == null || !password.equals(confPassword)) {
            request.setAttribute("status", "invalidInput");  // Hiển thị lỗi nếu không hợp lệ
            dispatcher = request.getRequestDispatcher("/view/view-account/reset-password.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Cập nhật mật khẩu vào cơ sở dữ liệu
        boolean isUpdated = accountService.updatePassword(email, password);

        // Nếu cập nhật thành công
        if (isUpdated) {
            request.setAttribute("status", "resetSuccess");
            dispatcher = request.getRequestDispatcher("/view/view-account/signin.jsp");
        } else {
            request.setAttribute("status", "resetFailed");
            dispatcher = request.getRequestDispatcher("/view/view-account/reset-password.jsp");
        }
        dispatcher.forward(request, response);
    }

    // Đăng nhập Bằng gg
    private void handleGoogleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            GoogleAccount googleAccount = (GoogleAccount) session.getAttribute("googleAccount");

            // Nếu đã đăng nhập, chuyển hướng về trang home thay vì đăng nhập lại
            if (googleAccount != null) {
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            // Lấy mã code từ request
            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/view/view-account/signin.jsp?error=missing_code");
                return;
            }

            // Gọi service để xử lý đăng nhập Google
            //googleAccount = accountService.handleGoogleLogin(code);

            // Lưu vào session
            //session.setAttribute("googleAccount", googleAccount);

            // Chuyển hướng đến trang home sau khi đăng nhập thành công
            //response.sendRedirect(request.getContextPath() + "/home");


            //-----------------BINH-----------------------
            googleAccount = accountService.handleGoogleLogin(code);

            // Kiểm tra xem user đã tồn tại trong DB chưa
            User user = accountService.getUserByEmail(googleAccount.getEmail());
            if (user == null) {
                // Nếu chưa có → tạo mới User trong DB
                user = new User();
                user.setIdUser(Integer.parseInt(googleAccount.getId()));
                user.setUsername(googleAccount.getUsername());
                user.setEmail(googleAccount.getEmail());
                user.setFullName(googleAccount.getFullName());
                user.setPhoneNumber(googleAccount.getPhoneNumber());
                user.setAuthProvider("google");
                user.setSocialId(googleAccount.getId());
                user.setImage(googleAccount.getImage());
                user.setIdRole(googleAccount.getIdRole());

                // Tạo user trong DB (bạn cần viết hàm createUser nếu chưa có)
                user = accountService.insertOrUpdateUserAndReturn(googleAccount);
            }

            // Gán user vào session như bình thường
            session.setAttribute("user", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("fullname", user.getFullName());
            session.setAttribute("email", user.getEmail());
            session.setAttribute("password", user.getPassword());
            session.setAttribute("phoneNumber", user.getPhoneNumber());// Chú ý: Không nên lưu mật khẩu vào session!
            session.setAttribute("birthDate", user.getBirthDate());
            session.setAttribute("image", user.getImage());
            response.sendRedirect(request.getContextPath() + "/home");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/view/view-account/signin.jsp?error=true");
        }
    }

    // Đăng Xuất Bằng Tk
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // Xóa session khi đăng xuất
        }
        // Đảm bảo response chưa bị committed trước khi gửi redirect
        response.setStatus(HttpServletResponse.SC_OK);  // Đảm bảo không có lỗi status
        // Sau khi logout, chuyển hướng về trang đăng nhập
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
        User user = accountService.getUserByUsernameAndAddress(username);
        if (user != null) {
            request.setAttribute("user", user);
            request.getRequestDispatcher("/view/view-account/profile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy người dùng.");
            request.getRequestDispatcher("/view/view-account/signin.jsp").forward(request, response);
        }
    }


}