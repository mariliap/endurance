package research.mpl.backend;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.JavaUtilLog;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.webapp.WebAppContext;
//import SmartResource;
import research.mpl.backend.todo.ToDoResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

//import org.jboss.weld.environment.se.Weld;
//import org.jboss.weld.environment.se.WeldContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Example of using JSP's with embedded jetty and not requiring
 * all of the overhead of a WebAppContext
 */
public class Main
{
    // Resource path pointing to where the WEBROOT is
    private static final String WEBROOT_INDEX = "/webapp/";

    private static final String REST_API_INDEX = "/research/mpl/backend/todo/";

    private static final String SMART_REST_API_INDEX = "/research/mpl/backend/smart/smart/rest";

    public static void main(String[] args) throws Exception
    {
        int port = 12345;
        //LoggingUtil.config();
        Log.setLog(new JavaUtilLog());



        Main main = new Main(port);
        main.start();


//        BrowserSample bs = new BrowserSample();
//        bs.launch();

        displayTrayIcon(port, main.getServerWebContextPath());

        if(Desktop.isDesktopSupported())
        {
            Desktop.getDesktop().browse(new URI("http://localhost:12345/ui"));
        }

        main.waitForInterrupt();
    }

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private int port;
    private Server server;
    private URI serverURI;

    //private Weld weld;
    private CdiContainer cdiContainer;

    public Main(int port)
    {
        this.port = port;
    }

    public URI getServerURI()
    {
        return serverURI;
    }

    public void start() throws Exception
    {

        //https://jersey.java.net/documentation/latest/cdi.support.html
        ////////////////////////////////////////////
        //weld = new Weld();
        //WeldContainer container = weld.initialize();
        ////////////////////////////////////////////
        cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContexts();

        server = new Server();
        ServerConnector connector = connector();
        server.addConnector(connector);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                weld.shutdown();
            }
        }));



        // Set JSP to use Standard JavaC always
        System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

        URI baseUri = getWebRootResourceUri();
        WebAppContext webAppContext = getWebAppContext(baseUri, getScratchDir());

//        server.setHandler(webAppContext);

        URI baseRestUri = getRestApiResourceUri();
        ServletContextHandler restApi = setupRestApiContextHandler(baseRestUri);

//        URI smartRestUri = getSmartRestApiResourceUri();
//        ServletContextHandler smartApi = setupSmartRestApiContextHandler(smartRestUri);

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{restApi, webAppContext});

        server.setHandler(contexts);

//        restApi.addEventListener(new CdiServletRequestListener());

        // Start Server
        server.start();

        // Show server state
        if (LOG.isLoggable(Level.FINE))
        {
            LOG.fine(server.dump());
        }
        this.serverURI = getServerUri(connector);

        LOG.info("REST URI: "
                        + (String.format("%s://%s:%d", "http", "localhost", port))
                        + restApi.getContextPath()
        );

        LOG.info("WEB APP URI: "
                        + (String.format("%s://%s:%d", "http", "localhost", port))
                        + webAppContext.getContextPath()
        );
    }

    private ServerConnector connector()
    {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        return connector;
    }

    private URI getWebRootResourceUri() throws FileNotFoundException, URISyntaxException
    {
        URL indexUri = this.getClass().getResource(WEBROOT_INDEX);
        if (indexUri == null)
        {
            throw new FileNotFoundException("Unable to find resource " + WEBROOT_INDEX);
        }

        return indexUri.toURI();
    }

    private URI getRestApiResourceUri() throws FileNotFoundException, URISyntaxException
    {

        URL indexUri = this.getClass().getResource(REST_API_INDEX);
        if (indexUri == null)
        {
            throw new FileNotFoundException("Unable to find resource " + REST_API_INDEX);
        }

        return indexUri.toURI();
    }

//    private URI getSmartRestApiResourceUri() throws FileNotFoundException, URISyntaxException
//    {
//
//        URL indexUri = this.getClass().getResource(SMART_REST_API_INDEX);
//        if (indexUri == null)
//        {
//            throw new FileNotFoundException("Unable to find resource " + SMART_REST_API_INDEX);
//        }
//
//        return indexUri.toURI();
//    }

    /**
     * Establish Scratch directory for the servlet context (used by JSP compilation)
     */
    private File getScratchDir() throws IOException
    {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "scratch-dir-mpl-endurance");

        if (!scratchDir.exists())
        {
            if (!scratchDir.mkdirs())
            {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        return scratchDir;
    }

    /**
     * Setup the basic application "context" for this application at "/"
     * This is also known as the handler tree (in jetty speak)
     */
    private WebAppContext getWebAppContext(URI baseUri, File scratchDir)
    {
        WebAppContext context = new WebAppContext();
        context.setContextPath("/ui");
        context.setAttribute("javax.servlet.context.tempdir", scratchDir);
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
        context.setResourceBase(baseUri.toASCIIString());
        context.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
        context.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
        context.addBean(new ServletContainerInitializersStarter(context), true);
        context.setClassLoader(getUrlClassLoader());
        context.setParentLoaderPriority(true);

        //context.addServlet(jspServletHolder(), "*.jsp");

        //context.addServlet(defaultServletHolder(baseUri), "/");

        //context.addServlet(jerseyServletHolder(), "/resources/*");

        return context;
    }

    private ServletContextHandler setupRestApiContextHandlerOld( URI baseRestUri) {

//
//        RuntimeDelegate delegate = RuntimeDelegate.getInstance();
//        JAXRSServerFactoryBean bean = delegate.createEndpoint(new CustomApplication(), JAXRSServerFactoryBean.class);
        ResourceConfig config = ResourceConfig.forApplicationClass(RestConfiguration.class);
        config.packages(RestConfiguration.class.getPackage().getName());

        config.register(JsonMoxyConfigurationContextResolver.class);
        config.register(ToDoResource.class);
        final ServletHolder cxfServletHolder = new ServletHolder(new ServletContainer(config));
//        cxfServletHolder.setInitParameter("javax.ws.rs.Application", RestConfiguration.class.getCanonicalName());
//        cxfServletHolder.setName("rest");
//        cxfServletHolder.setForcedPath("rest");
        cxfServletHolder.setInitOrder(1);


        final ServletContextHandler cxfContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //cxfContext.setSessionHandler(new SessionHandler());
        cxfContext.setContextPath("/api1");
        cxfContext.setResourceBase(baseRestUri.toASCIIString());

//        cxfContext.addEventListener(new BeanManagerResourceBindingListener());
        cxfContext.addEventListener(new CdiServletRequestListener());
        cxfContext.addServlet(cxfServletHolder, "/*");







        //        cxfContext.addFilter(new FilterHolder(CorsFilter.class), "/*",
//                EnumSet.allOf(DispatcherType.class));

        //http://myHostName/contextPath/servletURI/resourceURI
//        LOG.info("REST URI: "
//                        + (String.format("%s://%s:%d/", "http", "localhost", port)) + "/"
//                        + cxfContext.getContextPath() + "/"
//                        + cxfContext.getServletHandler().getServlets()[0].getContextPath()
//        );
//        LOG.info(uriInfo.getAbsolutePath().toASCIIString());

        return cxfContext;
    }

    private ServletContextHandler setupRestApiContextHandler(URI baseRestUri) {

        ResourceConfig config = ResourceConfig.forApplicationClass(RestConfiguration.class);
        config.packages(RestConfiguration.class.getPackage().getName());

        config.register(JsonMoxyConfigurationContextResolver.class);
//        config.register(SmartResource.class);
        config.register(ToDoResource.class);
        final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));
        servletHolder.setInitOrder(1);


        final ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/rest");
        context.setResourceBase(baseRestUri.toASCIIString());

//        context.addEventListener(new BeanManagerResourceBindingListener());
        context.addEventListener(new CdiServletRequestListener());
        context.addServlet(servletHolder, "/*");
        return context;
    }

    /**
     * Ensure the jsp engine is initialized correctly
     */
    private List<ContainerInitializer> jspInitializers()
    {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
        initializers.add(initializer);
        return initializers;
    }

    /**
     * Set Classloader of Context to be sane (needed for JSTL)
     * JSP requires a non-System classloader, this simply wraps the
     * embedded System classloader in a way that makes it suitable
     * for JSP to use
     */
    private ClassLoader getUrlClassLoader()
    {
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        return jspClassLoader;
    }

    /**
     * Create JSP Servlet (must be named "jsp")
     */
    private ServletHolder jspServletHolder()
    {
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.8");
        holderJsp.setInitParameter("compilerSourceVM", "1.8");
        holderJsp.setInitParameter("keepgenerated", "true");
        return holderJsp;
    }

    /**
     * Create Default Servlet (must be named "default")
     */
    private ServletHolder defaultServletHolder(URI baseUri)
    {
        ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
        LOG.info("Base URI: " + baseUri);
        holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
        holderDefault.setInitParameter("dirAllowed", "true");
        return holderDefault;
    }

    /**
     * Create Jersey Servlet
     */
    private ServletHolder jerseyServletHolder()
    {
        ServletHolder jerseyServletHolder = new ServletHolder(new ServletContainer());

        jerseyServletHolder.setInitOrder(1);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServletHolder.setInitParameter(
                "jersey.config.server.provider.classnames",
                RestConfiguration.class.getCanonicalName());

//        jerseyServletHolder.setInitParameter("resourceBase",  "/resources/*");
//        jerseyServletHolder.setInitParameter("dirAllowed", "true");

        return jerseyServletHolder;
    }

    /**
     * Establish the Server URI
     */
    private URI getServerUri(ServerConnector connector) throws URISyntaxException
    {
        String scheme = "http";
        for (ConnectionFactory connectFactory : connector.getConnectionFactories())
        {
            if (connectFactory.getProtocol().equals("SSL-http"))
            {
                scheme = "https";
            }
        }
        String host = connector.getHost();
        if (host == null)
        {
            host = "localhost";
        }
        int port = connector.getLocalPort();
        serverURI = new URI(String.format("%s://%s:%d/", scheme, host, port));
        LOG.info("Server URI: " + serverURI);
        return serverURI;
    }

    public String getServerWebContextPath() throws InterruptedException
    {
        Handler[] handlers = server.getChildHandlersByClass(WebAppContext.class);
        if(handlers.length != 0) {
            WebAppContext handler = (WebAppContext) handlers[0];
            return  handler.getContextPath();
        }

        return "";
    }

    public void stop() throws Exception
    {
        server.stop();
        cdiContainer.shutdown();
    }

    /**
     * Cause server to keep running until it receives a Interrupt.
     * <p>
     * Interrupt Signal, or SIGINT (Unix Signal),
     * is typically seen as a result of a kill -TERM {pid} or Ctrl+C
     * @throws InterruptedException if interrupted
     */
    public void waitForInterrupt() throws InterruptedException
    {
        server.join();
    }

    private static void displayTrayIcon(final int port, final String webAppContextPath) throws Exception {

        if (!GraphicsEnvironment.isHeadless()) {
            final TrayIcon trayIcon =
                    new TrayIcon(new ImageIcon(Main.class.getResource("/pf.png")).getImage());
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ev) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://localhost:" + port));
                    } catch (Exception e) {
                    }
                }
            });
            PopupMenu popup = new PopupMenu();
            MenuItem browseAction = new MenuItem("Browse");
            browseAction.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://localhost:" + port + webAppContextPath));
                    } catch (Exception ex) {
                    }
                }
            });
            MenuItem quitAction = new MenuItem("Quit");
            quitAction.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            popup.add(browseAction);
            popup.add(quitAction);
            trayIcon.setPopupMenu(popup);
            SystemTray.getSystemTray().add(trayIcon);
            trayIcon.displayMessage(
                    "Jetty Embedded Server (http://localhost:" + port + ")",
                    "Click this icon to open the browser.", TrayIcon.MessageType.INFO);
        }
    }


}
