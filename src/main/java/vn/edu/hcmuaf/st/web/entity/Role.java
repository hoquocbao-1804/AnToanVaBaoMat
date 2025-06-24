package vn.edu.hcmuaf.st.web.entity;

public class Role {
    private int idRole;
    private RoleName nameRole;

    public enum RoleName {
        ROLE_ADMIN, ROLE_USER
    }

    public Role() {}

    public Role(int idRole, RoleName nameRole) {
        this.idRole = idRole;
        this.nameRole = nameRole;
    }

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public RoleName getNameRole() {
        return nameRole;
    }

    public void setNameRole(RoleName nameRole) {
        this.nameRole = nameRole;
    }

    @Override
    public String toString() {
        return "Role{" +
                "idRole=" + idRole +
                ", nameRole=" + nameRole +
                '}';
    }
}
