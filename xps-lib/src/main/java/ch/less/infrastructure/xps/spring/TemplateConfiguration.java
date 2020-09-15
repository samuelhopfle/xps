package ch.less.infrastructure.xps.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * configuratino class for the management of XPS templates
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "xps.template")
@Data
public class TemplateConfiguration {

    /**
     * relative directory to XPS Templates
     */
    @NotNull
    private String path;

}
