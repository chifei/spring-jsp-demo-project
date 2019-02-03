package app.demo.user.web.role;

import app.demo.user.domain.RoleStatus;

import java.util.List;

/**
 * @author Jonathan.Guo
 */
public class RoleBean {
    public String id;
    public String name;
    public List<String> permissions;
    public RoleStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public RoleStatus getStatus() {
        return status;
    }

    public void setStatus(RoleStatus status) {
        this.status = status;
    }
}
