package ru.hostco.camel.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "client")
public class ClientConfiguration {
    private String ipAddress;
    private String urlVitakor;
    private String urlKomtek;
}
