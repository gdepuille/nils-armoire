package org.picotteland.sales.contexts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;

/**
 * Created by gdepuille on 27/09/14.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan("org.picotteland.sales")
public class MainContext {

    @Bean
    public ObjectMapper jsonMapper() {
        return new ObjectMapper();
    }

    @Bean
    public File imagesDir() throws IOException {
        PathMatchingResourcePatternResolver rr = new PathMatchingResourcePatternResolver();
        Resource resource = rr.getResource("file:./src/main/resources/images/");
        if (resource.exists()) {
            return resource.getFile();
        } else {
            throw new IOException("Le répertoire n'existe pas");
        }
    }
}
