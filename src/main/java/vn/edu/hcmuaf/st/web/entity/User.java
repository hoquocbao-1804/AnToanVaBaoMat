package vn.edu.hcmuaf.st.web.entity;

public class User {
    private int idUser;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String password; // Thêm để khớp với AccountRepository
    private String birthDate; // Thêm để khớp với AccountRepository
    private Address address; // Thêm để khớp với AccountRepository
    private String role; // Vai trò kiểu String (admin hoặc USER)

    // Constructors
    public User() {}

    public User(String fullName, String password, String username, String email) {
        this.fullName = fullName;
        this.password = password;
        this.username = username;
        this.email = email;
    }

    // Getters và Setters
    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getRole() {
        return role != null ? role.toLowerCase() : null;
    }

    public void setRole(String role) {
        this.role = role != null ? role.toLowerCase() : null;
    }
}