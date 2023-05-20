package nl.shashi.playground.wiremock.config;


import com.github.tomakehurst.wiremock.jetty.DefaultMultipartRequestConfigurer;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;


import com.github.tomakehurst.wiremock.http.AdminRequestHandler;
import com.github.tomakehurst.wiremock.http.StubRequestHandler;
import com.github.tomakehurst.wiremock.servlet.MultipartRequestConfigurer;
import com.github.tomakehurst.wiremock.servlet.WireMockHandlerDispatchingServlet;
import com.github.tomakehurst.wiremock.servlet.WireMockWebContextListener;

@Configuration
public class WiremockConfiguration {

    @Value("${wiremock.requestJournalDisabled:true}")
    private String saveRequestReceived;

    @Value("${wiremock.verboseLoggingEnabled:true}")
    private String verboseLogging;

    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> {
            servletContext.setInitParameter("WireMockFileSourceRoot", ("/wiremock"));
            servletContext.setInitParameter("requestJournalDisabled", saveRequestReceived);
            servletContext.setInitParameter("verboseLoggingEnabled", verboseLogging);

            servletContext.setAttribute(MultipartRequestConfigurer.KEY, new DefaultMultipartRequestConfigurer());
        };
    }

    @Bean
    public PathMatchingResourcePatternResolver resolver(ResourceLoader resourceLoader) {
        return new PathMatchingResourcePatternResolver(resourceLoader);
    }

    @Bean
    public WireMockWebContextListener wireMockWebContextListener() {
        return new WireMockWebContextListener();
    }

    @Bean
    public ServletRegistrationBean<WireMockHandlerDispatchingServlet> wiremockServletBean() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("RequestHandlerClass", StubRequestHandler.class.getName());
        parameters.put("mappedUnder", "/stub/mapping");

        ServletRegistrationBean<WireMockHandlerDispatchingServlet> wiremockServlet = new ServletRegistrationBean<>(new WireMockHandlerDispatchingServlet(), "/stub/mapping/*");
        wiremockServlet.setName("Wiremock");
        wiremockServlet.setInitParameters(parameters);
        wiremockServlet.setLoadOnStartup(1);
        wiremockServlet.setMultipartConfig(new MultipartConfigElement(""));
        return wiremockServlet;
    }

    @Bean
    public ServletRegistrationBean<WireMockHandlerDispatchingServlet> adminWiremockServletBean() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("RequestHandlerClass", AdminRequestHandler.class.getName());
        parameters.put("mappedUnder", "/stub/admin");

        ServletRegistrationBean<WireMockHandlerDispatchingServlet> wiremockServlet = new ServletRegistrationBean<>(new WireMockHandlerDispatchingServlet(), "/stub/admin/*");
        wiremockServlet.setName("WiremockAdmin");
        wiremockServlet.setInitParameters(parameters);
        wiremockServlet.setLoadOnStartup(1);
        return wiremockServlet;
    }
}
