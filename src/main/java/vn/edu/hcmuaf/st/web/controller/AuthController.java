package vn.edu.hcmuaf.st.web.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmuaf.st.web.entity.User;
import vn.edu.hcmuaf.st.web.service.AccountService;
import vn.edu.hcmuaf.st.web.util.PermissionUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
@WebServlet(urlPatterns = {"/sign", "/register", "/forgot-password", "/enter-otp", "/reset-password"})
public class AuthController extends HttpServlet {
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        String action = request.getServletPath();
        switch (action) {
            case "/register":
                new RegisterHandler(accountService).handle(request, response);
                break;
            case "/sign":
                new LoginHandler(accountService).handle(request, response);
                break;
            case "/forgot-password":
                new ForgotPasswordHandler(accountService).handle(request, response);
                break;
            case "/enter-otp":
                new OtpHandler().handle(request, response);
                break;
            case "/reset-password":
                new ResetPasswordHandler(accountService).handle(request, response);
                break;
        }
    }


}
