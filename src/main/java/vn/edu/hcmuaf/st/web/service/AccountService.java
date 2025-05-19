package vn.edu.hcmuaf.st.web.service;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.st.web.dao.AccountRepository;
import vn.edu.hcmuaf.st.web.entity.User;

import java.util.Random;

public class AccountService {
    private final AccountRepository accountRepository = new AccountRepository();

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

    public User getUserByEmail(String email) {
        return accountRepository.getUserByEmail(email);
    }

    public User getUserByUsernameAndAddress(String username) {
        return accountRepository.getUserByUsernameAndAddress(username);
    }

    public int generateOTP() {
        return new Random().nextInt(900000) + 100000; // 6-digit OTP
    }

    public void sendOTP(String email, int otp) throws Exception {
        // Implement email sending logic (e.g., using JavaMail)
        // For now, assume it works
        System.out.println("Sending OTP " + otp + " to " + email);
    }

    public boolean updatePassword(String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return accountRepository.updatePasswordByEmail(email, hashedPassword);
    }


    public boolean updateUserInfo(int idUser, String fullName, String phoneNumber, String email,
                                  String address, String ward, String district, String province,
                                  java.sql.Date birthDate) {
        return accountRepository.updateUserInfo(idUser, fullName, phoneNumber, email, address, ward, district, province, birthDate);
    }

    public boolean updateUserRole(int userId, int newRole) {
        if (newRole != 1 && newRole != 2) { // Validate role
            return false;
        }
        return accountRepository.updateUserRole(userId, newRole);
    }
}