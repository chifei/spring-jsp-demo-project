package app.demo.common.web;

import app.demo.user.service.CachedUserInfo;
import app.demo.user.service.UserInfoCacheService;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Component
public class UserInfo {
    public static final String SESSION_USER_ID = "user-id";

    @Inject
    HttpServletRequest request;

    @Inject
    UserInfoCacheService userInfoCacheService;

    public boolean isUserLogin() {
        return !Strings.isNullOrEmpty(userId());
    }

    public String username() {
        String id = userId();
        if (Strings.isNullOrEmpty(id)) {
            return null;
        }
        return userInfoCacheService.load(id).username;
    }

    private String userId() {
        return (String) request.getSession(true).getAttribute(SESSION_USER_ID);
    }

    public void setUserId(String userId) {
        request.getSession(true).setAttribute(SESSION_USER_ID, userId);
    }

    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            String userId = userId();
            CachedUserInfo user = userInfoCacheService.load(userId);
            if (!user.permissions.contains(permission)) {
                return false;
            }
        }
        return true;
    }

    public List<String> permissions() {
        String userId = userId();
        if (Strings.isNullOrEmpty(userId)) {
            return ImmutableList.of();
        }
        return userInfoCacheService.load(userId).permissions;
    }

    public void logout() {
        HttpSession session = request.getSession();
        if (session != null) {
            session.setAttribute(SESSION_USER_ID, null);
            session.invalidate();
        }
    }
}
