package com.starline.subscriptions.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI customOpenAPI(@Value("${spring.application.name:none}") String appName) {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .version("1.0.0")
                        .description("Resi Service")
                        .contact(new Contact()
                                .name("Hakim Amarullah")
                                .email("hakimamarullah@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(getServer(appName));
    }

    private List<Server> getServer(String defaultInstanceName) {
        return List.of(
                new Server().url("http://localhost:8082/" + defaultInstanceName),
                new Server().url("/")
        );
    }
}
