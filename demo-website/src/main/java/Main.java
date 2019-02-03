import app.demo.WebConfig;
import app.demo.user.service.Queues;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.deploy.DeploymentManager;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.io.IOException;

public class Main {
    private final Logger logger = LoggerFactory.getLogger(Main.class);
    private final boolean debugEnabled;
    private final int port;
    private Server server;
    private BrokerService broker;

    public Main(boolean debugEnabled, int port) {
        this.debugEnabled = debugEnabled;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        boolean debugEnabled = Boolean.TRUE.toString().equals(System.getProperty("debug"));
        int port = System.getProperty("port") == null ? 8070 : Integer.parseInt(System.getProperty("port"));
        new Main(debugEnabled, port).start();
    }

    private void start() throws Exception {
        logger.info("start server");
        startActiveMQ();
        startJetty();
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }

    private void stop() {
        logger.info("stop app");
        if (broker != null && broker.isStarted()) {
            try {
                broker.stop();
            } catch (Exception e) {
                logger.error("failed to stop broker");
            }
        }

        if (server != null && server.isStarted()) {
            server.setStopAtShutdown(true);
            try {
                server.stop();
            } catch (Exception e) {
                logger.error("failed to stop server", e);
            }
        }
    }

    private void startActiveMQ() throws Exception {
        broker = new BrokerService();
        broker.addConnector("vm://localhost:7777");
        broker.setPersistent(false);
        broker.setDestinations(new ActiveMQDestination[]{new ActiveMQQueue(Queues.QUEUE_USER)});
        broker.start();
    }

    private void startJetty() throws Exception {
        logger.debug("Starting server at port {}", port);
        server = new Server(port);
        ServletContextHandler servletContextHandler = getServletContextHandler(debugEnabled);
        server.setHandler(servletContextHandler);
        server.start();
        logger.info("Server started at port {}", port);
        server.join();
    }

    private ServletContextHandler getServletContextHandler(boolean debugEnabled) throws IOException {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS); // SESSIONS requerido para JSP
        servletContextHandler.setErrorHandler(null);


        if (debugEnabled) {
            DeploymentManager deploymentManager = new DeploymentManager();
            deploymentManager.setContexts(new ContextHandlerCollection(servletContextHandler));
            servletContextHandler.addManaged(deploymentManager);
            servletContextHandler.setResourceBase(new FileSystemResource("demo-website/src/main/webapp").getURI().toString());
        } else {
            String mainClassPath = Thread.currentThread().getContextClassLoader().getResource("Main.class").toExternalForm();
            servletContextHandler.setResourceBase(mainClassPath.substring(0, mainClassPath.length() - "Main.class".length()));
        }
        servletContextHandler.setContextPath("/");
        servletContextHandler.setClassLoader(Thread.currentThread().getContextClassLoader()); // Necesario para cargar JspServlet

        AnnotationConfigWebApplicationContext webAppContext = getWebApplicationContext();
        servletContextHandler.addEventListener(new JettyContextLoaderListener(webAppContext));
        enableJspSupport(servletContextHandler);
        return servletContextHandler;
    }

    private void enableJspSupport(ServletContextHandler servletContextHandler) throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

        if (!scratchDir.exists()) {
            if (!scratchDir.mkdirs()) {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }

        servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);
        servletContextHandler.setAttribute("jetty.deploy.monitoredPath", scratchDir.getAbsolutePath());
        servletContextHandler.setAttribute("jetty.deploy.scanInterval", 1);

        servletContextHandler.addBean(new JspStarter(servletContextHandler));
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.8");
        holderJsp.setInitParameter("compilerSourceVM", "1.8");
        holderJsp.setInitParameter("keepgenerated", "true");
        servletContextHandler.addServlet(holderJsp, "*.jsp");
    }

    private AnnotationConfigWebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        return context;
    }

    static class JettyContextLoaderListener extends ContextLoaderListener {
        private final AnnotationConfigWebApplicationContext webAppContext;

        JettyContextLoaderListener(AnnotationConfigWebApplicationContext webAppContext) {
            super(webAppContext);
            this.webAppContext = webAppContext;
        }

        @Override
        public void contextInitialized(ServletContextEvent event) {
            super.contextInitialized(event);
            ServletContext servletContext = event.getServletContext();
            DispatcherServlet servlet = new DispatcherServlet(webAppContext);
            servlet.setThrowExceptionIfNoHandlerFound(true);
            ServletRegistration.Dynamic dispatcher = servletContext.addServlet(AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME, servlet);
            dispatcher.addMapping("/");
            dispatcher.setLoadOnStartup(1);
        }
    }

    static class JspStarter extends AbstractLifeCycle implements ServletContextHandler.ServletContainerInitializerCaller {
        JettyJasperInitializer sci;
        ServletContextHandler context;

        JspStarter(ServletContextHandler context) {
            this.sci = new JettyJasperInitializer();
            this.context = context;
            StandardJarScanner jarScanner = new StandardJarScanner();
            jarScanner.setJarScanFilter((jarScanType, jarName) -> {
                System.out.println(jarName);
                return jarName.contains("spring") || jarName.contains("taglibs") || jarName.contains("website");
            });
            jarScanner.setScanClassPath(true);
            jarScanner.setScanBootstrapClassPath(true);
            this.context.setAttribute("org.apache.tomcat.JarScanner", jarScanner);
        }

        @Override
        protected void doStart() throws Exception {
            ClassLoader old = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(context.getClassLoader());
            try {
                sci.onStartup(null, context.getServletContext());
                super.doStart();
            } finally {
                Thread.currentThread().setContextClassLoader(old);
            }
        }
    }
}