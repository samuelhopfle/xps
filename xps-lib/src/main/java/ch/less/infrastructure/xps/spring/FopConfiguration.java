package ch.less.infrastructure.xps.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * configuration class for Apache FO processing
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "xps.fop")
@Data
public class FopConfiguration {

    /**
     * FOP configuration file name
     */
    @NotNull
    private String configuration;
}
