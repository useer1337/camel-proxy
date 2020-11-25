package ru.hostco.camel.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "clients")
public class ApplicationConfiguration {
    private List<ClientConfiguration> client;
}
