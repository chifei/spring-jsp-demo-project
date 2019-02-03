package app.demo.user.web;

import app.demo.common.web.UserInfo;

import javax.el.ELContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class HasPermissionTag extends SimpleTagSupport {
    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public void doTag() throws JspException, IOException {
        ELContext elContext = getJspContext().getELContext();
        UserInfo user = (UserInfo) elContext.getELResolver().getValue(elContext, null, "user");
        if (user != null && user.hasPermission(permission)) {
            getJspBody().invoke(getJspContext().getOut());
        }
    }
}