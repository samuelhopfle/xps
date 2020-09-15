package ch.less.infrastructure.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * configuration class for service info page
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app.info")
@Data
public class InfoPageConfiguration {

    /**
     * Info Page i18n localce
     */
    @NotNull
    private String locale;
}
