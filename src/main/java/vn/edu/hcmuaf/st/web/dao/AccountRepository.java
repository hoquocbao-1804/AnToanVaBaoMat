package vn.edu.hcmuaf.st.web.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.mindrot.jbcrypt.BCrypt;
import vn.edu.hcmuaf.st.web.dao.db.JDBIConnect;
import vn.edu.hcmuaf.st.web.entity.Address;
import vn.edu.hcmuaf.st.web.entity.GoogleAccount;
import vn.edu.hcmuaf.st.web.entity.User;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class AccountRepository {
    private static final Logger LOGGER = Logger.getLogger(AccountRepository.class.getName());
    private final Jdbi jdbi;

    public AccountRepository() {
        this.jdbi = JDBIConnect.get();
    }

    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        return jdbi.withHandle(handle -> {
            Query q = handle.createQuery(query).bind(0, username);
            return q.mapTo(Integer.class).one() > 0;
        });
    }
    public boolean addUser(String username, String password, String fullname, String email, String phoneNumber) {
        String query = "INSERT INTO users (username, password, fullName, email, phoneNumber, active, birthDate, role, idRole) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return jdbi.withHandle(handle -> {
            int rowsInserted = handle.createUpdate(query)
                    .bind(0, username)
                    .bind(1, password)
                    .bind(2, fullname)
                    .bind(3, email)
                    .bind(4, phoneNumber)
                    .bind(5, true)
                    .bindNull(6, java.sql.Types.DATE)
                    .bind(7, "USER")       // role = USER
                    .bind(8, 2)            // idRole = 2 (ví dụ)
                    .execute();
            return rowsInserted > 0;
        });
    }


    public boolean validateUser(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        return jdbi.withHandle(handle -> {
            Query q = handle.createQuery(query).bind(0, username);
            String hashedPassword = q.mapTo(String.class).findOnly();
            if (hashedPassword == null) {
                return false;
            }
            return BCrypt.checkpw(password, hashedPassword);
        });
    }

    public boolean updatePasswordByEmail(String email, String hashedPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        return jdbi.withHandle(handle -> {
            int rowsUpdated = handle.createUpdate(sql)
                    .bind(0, hashedPassword)
                    .bind(1, email)
                    .execute();
            return rowsUpdated > 0;
        });
    }

    public String getFullNameByUsername(String username) {
        String query = "SELECT fullName FROM users WHERE username = ?";
        return jdbi.withHandle(handle -> {
            String fullName = handle.createQuery(query)
                    .bind(0, username)
                    .mapTo(String.class)
                    .findOnly();
            return fullName;
        });
    }

    public User getUserByUsernameAndAddress(String username) {
        String query = """
                SELECT 
                    u.idUser AS u_idUser, u.fullName AS u_fullName, u.password AS u_password, 
                    u.username AS u_username, u.email AS u_email, u.phoneNumber AS u_phoneNumber, 
                    u.birthDate AS u_birthDate, u.role AS u_role,
                    a.idAddress AS a_idAddress, a.address AS a_address, a.ward AS a_ward, 
                    a.district AS a_district, a.province AS a_province, a.isDefault AS a_isDefault
                FROM users u
                JOIN address a ON u.idUser = a.idUser
                WHERE u.username = ?
                """;

        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .bind(0, username)
                        .map((rs, ctx) -> {
                            User user = new User();
                            user.setIdUser(rs.getInt("u_idUser"));
                            user.setFullName(rs.getString("u_fullName"));
                            user.setPassword(rs.getString("u_password"));
                            user.setUsername(rs.getString("u_username"));
                            user.setEmail(rs.getString("u_email"));
                            user.setPhoneNumber(rs.getString("u_phoneNumber"));
                            user.setRole(rs.getString("u_role"));

                            Date birthDate = rs.getDate("u_birthDate");
                            if (birthDate != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                user.setBirthDate(sdf.format(birthDate));
                            }

                            Address address = new Address(
                                    rs.getInt("a_idAddress"),
                                    user,
                                    rs.getString("a_address"),
                                    rs.getString("a_ward"),
                                    rs.getString("a_district"),
                                    rs.getString("a_province"),
                                    rs.getBoolean("a_isDefault")
                            );

                            user.setAddress(address);
                            return user;
                        }).findOne().orElse(null)
        );
    }

    public User getUserByUsername(String username) {
        String query = "SELECT idUser, fullName, password, username, email, phoneNumber, role FROM users WHERE username = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .bind(0, username)
                        .map((rs, ctx) -> {
                            User user = new User();
                            user.setIdUser(rs.getInt("idUser"));
                            LOGGER.info("RS CHECK idUser = " + rs.getInt("idUser"));
                            user.setFullName(rs.getString("fullName"));
                            user.setPassword(rs.getString("password"));
                            user.setUsername(rs.getString("username"));
                            user.setEmail(rs.getString("email"));
                            user.setPhoneNumber(rs.getString("phoneNumber"));
                            user.setRole(rs.getString("role"));
                            LOGGER.info("AFTER SET => user.getIdUser() = " + user.getIdUser() + ", Role=" + user.getRole());
                            return user;
                        }).findOne().orElse(null)
        );
    }

    public User insertOrUpdateUser(GoogleAccount googleAccount) {
        String query = """
                INSERT INTO users (username, password, fullName, email, role, image, socialId, phoneNumber)
                VALUES (:username, :password, :fullName, :email, :role, :image, :socialId, :phoneNumber)
                ON DUPLICATE KEY UPDATE 
                    fullName = :fullName, 
                    image = :image, 
                    username = :username,
                    password = :password,
                    phoneNumber = :phoneNumber
                """;
        LOGGER.info("Executing query: " + query);
        LOGGER.info("Parameters: Username=" + googleAccount.getUsername() +
                ", Password=" + googleAccount.getPassword() +
                ", FullName=" + googleAccount.getFullName() +
                ", Email=" + googleAccount.getEmail() +
                ", Role=USER" +
                ", Image=" + googleAccount.getImage() +
                ", SocialID=" + googleAccount.getId());

        try {
            jdbi.useHandle(handle ->
                    handle.createUpdate(query)
                            .bind("username", googleAccount.getUsername())
                            .bind("password", googleAccount.getPassword())
                            .bind("fullName", googleAccount.getFullName())
                            .bind("email", googleAccount.getEmail())
                            .bind("role", "USER")
                            .bind("image", googleAccount.getImage())
                            .bind("socialId", googleAccount.getId())
                            .bind("phoneNumber", googleAccount.getPhoneNumber())
                            .execute()
            );

            User user = new User();
            user.setUsername(googleAccount.getUsername());
            user.setFullName(googleAccount.getFullName());
            user.setEmail(googleAccount.getEmail());
            user.setRole("USER");
            return user;
        } catch (Exception e) {
            LOGGER.severe("Error inserting/updating Google user: " + e.getMessage());
            return null;
        }
    }

    public boolean updateUserInfo(int idUser, String fullName, String phoneNumber, String email,
                                  String address, String ward, String district, String province,
                                  java.util.Date birthDate) {
        String updateUserSql = """
                UPDATE users SET 
                    fullName = :fullName,
                    phoneNumber = :phoneNumber,
                    email = :email,
                    birthDate = :birthDate
                WHERE idUser = :idUser
                """;

        String updateAddressSql = """
                UPDATE address SET 
                    address = :address,
                    ward = :ward,
                    district = :district,
                    province = :province
                WHERE idUser = :idUser
                """;

        try {
            return jdbi.withHandle(handle -> {
                int userRows = handle.createUpdate(updateUserSql)
                        .bind("idUser", idUser)
                        .bind("fullName", fullName)
                        .bind("phoneNumber", phoneNumber)
                        .bind("email", email)
                        .bind("birthDate", new java.sql.Date(birthDate.getTime()))
                        .execute();

                int addressRows = handle.createUpdate(updateAddressSql)
                        .bind("idUser", idUser)
                        .bind("address", address)
                        .bind("ward", ward)
                        .bind("district", district)
                        .bind("province", province)
                        .execute();

                return userRows > 0 && addressRows > 0;
            });
        } catch (Exception e) {
            LOGGER.severe("Error updating user info: " + e.getMessage());
            return false;
        }
    }

    public User getUserByEmail(String email) {
        String query = "SELECT idUser, fullName, password, username, email, phoneNumber, role FROM users WHERE email = ?";
        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .bind(0, email)
                        .map((rs, ctx) -> {
                            User user = new User();
                            user.setIdUser(rs.getInt("idUser"));
                            user.setFullName(rs.getString("fullName"));
                            user.setPassword(rs.getString("password"));
                            user.setUsername(rs.getString("username"));
                            user.setEmail(rs.getString("email"));
                            user.setPhoneNumber(rs.getString("phoneNumber"));
                            user.setRole(rs.getString("role"));
                            return user;
                        })
                        .findOne()
                        .orElse(null)
        );
    }

    public static void main(String[] args) {
        AccountRepository repo = new AccountRepository();
        String username = "danh";
        User user = repo.getUserByUsernameAndAddress(username);

        if (user != null) {
            System.out.println("===== THÔNG TIN NGƯỜI DÙNG =====");
            System.out.println("ID: " + user.getIdUser());
            System.out.println("Họ tên: " + user.getFullName());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Số điện thoại: " + user.getPhoneNumber());
            System.out.println("Ngày sinh: " + user.getBirthDate());
            System.out.println("Địa chỉ: " + user.getAddress());
        } else {
            System.out.println("Không tìm thấy người dùng với username = " + username);
        }
    }
}