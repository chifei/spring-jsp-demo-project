package app.demo.user.web;


import app.demo.common.web.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class LogoutController {
    @Inject
    UserInfo userInfo;

    @RequestMapping(value = "/admin/logout", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public void login(HttpServletResponse response) throws IOException {
        userInfo.logout();
        response.sendRedirect("/");
    }
}
