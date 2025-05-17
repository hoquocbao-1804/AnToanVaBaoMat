package vn.edu.hcmuaf.st.web.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.st.web.controller.SocialLogin;
import vn.edu.hcmuaf.st.web.dao.AccountRepository;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import vn.edu.hcmuaf.st.web.entity.GoogleAccount;
import vn.edu.hcmuaf.st.web.entity.User;

import java.sql.Date;
import java.util.Properties;
import java.util.Random;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService() {
        this.accountRepository = new AccountRepository();
    }

    // Đăng Nhập
    public boolean login(String username, String password) {
        return accountRepository.validateUser(username, password);
    }
    // Đăng ký tài khoản
    public boolean register(String username, String password, String fullname, String email, String phoneNumber) {
        if (accountRepository.isUsernameExists(username)) {
            System.out.println("Username already exists!");
            return false;
        }
        // Mã hóa mật khẩu bằng BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // Tạo tài khoản mới (truyền các tham số cần thiết)
        return accountRepository.addUser(username, hashedPassword, fullname, email, phoneNumber);
    }
    // Dịch vụ gửi email
    public int generateOTP() {
        Random rand = new Random();
        return 100000 + rand.nextInt(900000);
    }
    // gửi otp
    public void sendOTP(String userEmail, int otpvalue) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("danhv5879@gmail.com", "bvhofdvukcetvrdm");
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("danhv5879@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP is: " + otpvalue);

        Transport.send(message);
    }
    // đổi mk
    public boolean updatePassword(String email, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        return accountRepository.updatePasswordByEmail(email, hashedPassword);
    }
    // hiển thị tên khi đăng nhập thành công
    public User getUserByUsername(String username) {
        return accountRepository.getUserByUsername(username);
    }
    // Lấy thông tin người dùng theo username ( hiển thị trong profile.jsp)
    public User getUserByUsernameAndAddress(String username) {
        return accountRepository.getUserByUsernameAndAddress(username);  // Gọi phương thức từ DAO
    }


    // đăng nhập google
    public GoogleAccount handleGoogleLogin(String code) throws Exception {
        // Lấy thông tin tài khoản Google
        SocialLogin gg = new SocialLogin();
        String accessToken = gg.getToken(code);
        GoogleAccount googleAccount = gg.getUserInfo(accessToken);
        // Thêm mới hoặc cập nhật người dùng
        accountRepository.insertOrUpdateUser(googleAccount);  // Thêm hoặc cập nhật người dùng trong CSDL
        return googleAccount;
    }
    // cập nhật thông tin người dùng
    public boolean updateUserInfo(int idUser, String fullName, String phoneNumber, String email,
                                  String address, String ward, String district, String province,
                                  java.util.Date birthDate) {
        return accountRepository.updateUserInfo(idUser, fullName, phoneNumber, email, address, ward, district, province, birthDate);
    }

    public User insertOrUpdateUserAndReturn(GoogleAccount googleAccount) {
        accountRepository.insertOrUpdateUser(googleAccount);
        return accountRepository.getUserByEmail(googleAccount.getEmail());
    }

    public User getUserByEmail(String email) {
        return accountRepository.getUserByEmail(email);
    }


    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        User user = accountService.getUserByUsername("hatest123");
        System.out.println(user.getIdUser());
    }
}

