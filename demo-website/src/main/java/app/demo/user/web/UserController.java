package app.demo.user.web;

import app.demo.common.util.JSONBinder;
import app.demo.common.util.Messages;
import app.demo.common.web.MessagesScriptBuilder;
import app.demo.common.web.UserPreference;
import app.demo.common.web.interceptor.LoginRequired;
import app.demo.common.web.interceptor.PermissionRequired;
import app.demo.user.domain.Role;
import app.demo.user.domain.User;
import app.demo.user.service.RolePermissionService;
import app.demo.user.service.RoleService;
import app.demo.user.service.UserRoleService;
import app.demo.user.service.UserService;
import app.demo.user.web.role.RoleBean;
import app.demo.user.web.user.UserBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jonathan.Guo
 */
@Controller
public class UserController {
    @Inject
    Messages messages;
    @Inject
    UserPreference userPreference;
    @Inject
    UserService userService;
    @Inject
    UserRoleService userRoleService;
    @Inject
    RoleService roleService;
    @Inject
    RolePermissionService rolePermissionService;


    @RequestMapping(value = "/admin/user/user", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String userList(ModelMap model) {
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/users/list.jsp";
    }

    @RequestMapping(value = "/admin/user/user/create", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String userCreate(ModelMap model) {
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/users/create.jsp";
    }

    @RequestMapping(value = "/admin/user/user/{id}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String userUpdate(ModelMap model, @PathVariable("id") String id) {
        User user = userService.get(id);
        UserBean response = response(user);

        model.put("user", response);
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/users/update.jsp";
    }

    @RequestMapping(value = "/admin/user/user/{id}/view", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String userView(ModelMap model, @PathVariable("id") String id) {
        User user = userService.get(id);
        UserBean response = response(user);

        model.put("user", response);
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/users/view.jsp";
    }

    @RequestMapping(value = "/admin/user/role", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String roleList(ModelMap model) {
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/roles/list.jsp";
    }

    @RequestMapping(value = "/admin/user/role/create", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String roleCreate(ModelMap model) {
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/roles/create.jsp";
    }

    @RequestMapping(value = "/admin/user/role/{id}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String roleUpdate(ModelMap model, @PathVariable("id") String id) {
        Role role = roleService.get(id);
        RoleBean response = response(role);

        model.put("role", response);
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/roles/update.jsp";
    }

    @RequestMapping(value = "/admin/user/role/{id}/view", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @LoginRequired
    @PermissionRequired("user.read")
    public String roleView(ModelMap model, @PathVariable("id") String id) {
        Role role = roleService.get(id);
        RoleBean response = response(role);

        model.put("role", response);
        Map<String, String> messages = this.messages.getMessages(userPreference.locale());
        model.put("messages", "window.messages=" + JSONBinder.toJSON(new MessagesScriptBuilder(messages).build()));
        return "/roles/view.jsp";
    }

    private UserBean response(User user) {
        UserBean response = new UserBean();
        response.id = user.id;
        response.username = user.username;
        response.email = user.email;
        response.roleIds = userRoleService.getRoleIds(user.id);
        response.roleNames = response.roleIds.stream().map(roleId -> roleService.get(roleId).name).collect(Collectors.toList());
        response.status = user.status;
        return response;
    }

    private RoleBean response(Role role) {
        RoleBean response = new RoleBean();
        response.id = role.id;
        response.name = role.name;
        response.permissions = rolePermissionService.getPermissionNames(role.id);
        response.status = role.status;
        return response;
    }
}
