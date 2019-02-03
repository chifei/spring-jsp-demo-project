package app.demo;

import app.demo.common.exception.ApplicationException;
import app.demo.user.domain.Role;
import app.demo.user.domain.RoleStatus;
import app.demo.user.service.Queues;
import app.demo.user.service.RoleService;
import app.demo.user.service.UserService;
import app.demo.user.web.role.CreateRoleRequest;
import app.demo.user.web.user.CreateUserRequest;
import com.google.common.collect.Lists;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author chi
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = WebConfig.class)
@WebAppConfiguration
public class SpringTest {
    private static BrokerService broker;
    protected MockMvc mockMvc;

    @Inject
    WebApplicationContext applicationContext;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
        resetDatabase();
    }

    @BeforeAll
    public static void start() throws Exception {
        broker = new BrokerService();
        broker.addConnector("vm://localhost:7777");
        broker.setPersistent(false);
        broker.setDestinations(new ActiveMQDestination[]{new ActiveMQQueue(Queues.QUEUE_USER)});
        broker.start();
    }

    @AfterAll
    public static void stop() throws Exception {
        if (broker != null && broker.isStarted()) {
            broker.stop();
        }
    }

    private void resetDatabase() {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("TRUNCATE SCHEMA public AND COMMIT")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }

        RoleService roleService = applicationContext.getBean(RoleService.class);
        CreateRoleRequest admin = new CreateRoleRequest();
        admin.name = "admin";
        admin.permissions = Lists.newArrayList("user.read", "user.write", "product.read", "product.write");
        admin.requestBy = "SYS";
        admin.status = RoleStatus.ACTIVE;
        Role adminRole = roleService.create(admin);

        UserService userService = applicationContext.getBean(UserService.class);
        CreateUserRequest createAdminRequest = new CreateUserRequest();
        createAdminRequest.username = "admin";
        createAdminRequest.email = "admin@admin.com";
        createAdminRequest.roleIds = Lists.newArrayList(adminRole.id);
        createAdminRequest.password = "admin";
        createAdminRequest.requestBy = "SYS";
        userService.create(createAdminRequest);
    }
}
