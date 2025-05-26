package vn.edu.hcmuaf.st.web.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.st.web.dao.AccountRepository;
import vn.edu.hcmuaf.st.web.entity.Order;
import vn.edu.hcmuaf.st.web.entity.Role;
import vn.edu.hcmuaf.st.web.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class AccountService {
    private final AccountRepository accountRepository;
    public AccountService() {
        this.accountRepository = new AccountRepository();
    }
    public boolean login(String username, String password) {
        return accountRepository.validateUser(username, password);
    }
    public boolean register(String username, String password, String fullname, String email, String phoneNumber) {
        if (accountRepository.isUsernameExists(username)) {
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return accountRepository.addUser(username, hashedPassword, fullname, email, phoneNumber);
    }
    public User getUserByUsername(String username) {
        return accountRepository.getUserByUsername(username);
    }
    public User getUserByUsernameAndAddress(String username) {
        return accountRepository.getUserByUsernameAndAddress(username);
    }

    public boolean updatePassword(String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return accountRepository.updatePasswordByEmail(email, hashedPassword);
    }
    public boolean updateUserInfo(int idUser, String fullName, String phoneNumber, String email,
                                  String address, String ward, String district, String province,
                                  java.util.Date birthDate) {
        return accountRepository.updateUserInfo(idUser, fullName, phoneNumber, email, address, ward, district, province, birthDate);
    }
    public boolean updateUserRole(int userId, Role.RoleName role) {
        return accountRepository.updateUserRole(userId, role);
    }
    public int generateOTP() {
        return new Random().nextInt(900000) + 100000;
    }
    public void sendOTP(String email, int otp) {
        System.out.println("Gửi OTP " + otp + " đến " + email);
    }
    public List<User> getRecentUsers() {
        return accountRepository.getRecentUsers();
    }

    public Map<String, Double> getRevenueLastSixMonths() {
        return accountRepository.getRevenueLastSixMonths();
    }
}