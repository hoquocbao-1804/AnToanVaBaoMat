package vn.edu.hcmuaf.st.web.entity;

import java.sql.Date;

public class User {
    private int idUser;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String image;
    private Role.RoleName role;
    private String socialId;
    private String authProvider;
    private Address address;

    public User() {}

    public User(String fullName, String password, String username, String email) {
        this.fullName = fullName;
        this.password = password;
        this.username = username;
        this.email = email;
        this.role = Role.RoleName.ROLE_USER;
    }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Role.RoleName getRole() { return role; }
    public void setRole(Role.RoleName role) { this.role = role; }

    public String getSocialId() { return socialId; }
    public void setSocialId(String socialId) { this.socialId = socialId; }

    public String getAuthProvider() { return authProvider; }
    public void setAuthProvider(String authProvider) { this.authProvider = authProvider; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public boolean isAdmin() {
        return role == Role.RoleName.ROLE_ADMIN;
    }

    public boolean isUser() {
        return role == Role.RoleName.ROLE_USER;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", role=" + role +
                ", address=" + address +
                '}';
    }
}