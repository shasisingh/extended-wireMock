package nl.shashi.playground.wiremock.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Configuration
public class CustomContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {


    @Value("${container.file.root}")
    private String containerName;

    @Value("${container.jar.root}")
    private String containerJar;

    private final PathMatchingResourcePatternResolver resolver;

    public CustomContainer(PathMatchingResourcePatternResolver resolver) {
        this.resolver = resolver;
    }


    public void customize(ConfigurableServletWebServerFactory factory) {

        try {
            var containerFileRoot = new File(containerName);
            if (!containerFileRoot.exists()) {
                containerFileRoot.mkdirs();
            }
            copyResourcesToDirectory(containerJar, containerFileRoot);
            factory.setDocumentRoot(containerFileRoot);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void copyResourcesToDirectory(String resourcePath, File target) throws IOException {
        Resource containerJarRoot = new ClassPathResource(resourcePath);
        Resource[] resources = resolver.getResources(resourcePath + "/**/*.*");
        for (Resource resource : resources) {
            var relativePath = resource.getURL().getPath().substring(containerJarRoot.getURL().getPath().length());
            var targetPath = new File(target, relativePath).toPath();
            if (!targetPath.getParent().toFile().exists()) {
                targetPath.getParent().toFile().mkdirs();
            }
            Files.copy(resource.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
