package ch.less.infrastructure.xps.conf;

import ch.less.infrastructure.xps.spring.FopConfiguration;
import ch.less.infrastructure.xps.spring.TemplateConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * service class to get all path and print settings in the right format
 */
@Service
public class PrintSettingsService {

    @Autowired
    private TemplateConfiguration templateConfiguration;
    @Autowired
    private FopConfiguration fopConfiguration;
    @Autowired
    private ServletContext servletContext;

    private final static String FILE_PATH_SEPARATOR = "/";

    /**
     * URI of the absolute template path
     *
     * @return URI with the absolute template path
     * @throws URISyntaxException in case of configuration errors
     */
    public URI getAbsoluteTemplatePath() throws URISyntaxException {

        final String templatePath = templateConfiguration.getPath();

        // make sure the path ends with a separator slash (/), even if not configured
        final String templatePathWithTrailingSlash = templatePath.endsWith(FILE_PATH_SEPARATOR) ? templatePath : templatePath + FILE_PATH_SEPARATOR;
        final URI templateURI = new URI(templatePathWithTrailingSlash); // relative path from settings

        // URI resolve
        if (templateURI.isAbsolute()) // in case being configured absolute
            return templateURI;
        return getApplicationURI()
                .resolve(templateURI);
    }

    /**
     * returns a template file's absolute Path as a URI from a relative template filename
     *
     * @param templateFileName absolute or relative path to the template file
     * @return URI file path to the template file
     * @throws URISyntaxException in case of configuration errors
     */
    public URI getAbsoluteTemplateFile(final String templateFileName) throws URISyntaxException {

        // transform file path to URI
        final URI templateFileURI = new URI(templateFileName);

        // is it relative or absolute?
        if (!templateFileURI.isAbsolute())
            return this
                    .getAbsoluteTemplatePath()
                    .resolve(templateFileURI); // transform relative to absolute
        return templateFileURI;
    }

    /**
     * URI for apache configuration file
     *
     * @return URI with the absolute path to the file
     * @throws URISyntaxException in case of configuration errors
     */
    public URI getFopConfigurationFilename() throws URISyntaxException {

        final String configurationFilePath = fopConfiguration.getConfiguration();
        if (configurationFilePath == null) // check if the setting exists
            return null;

        final URI configurationFileURI = new URI(configurationFilePath); // relative path from settings
        if (configurationFileURI.isAbsolute()) // in case it was configured absolute
            return configurationFileURI;
        return getApplicationURI()
                .resolve(configurationFileURI);
    }

    /**
     * returns the base path of the application (useful for logs, templates and configuration)
     *
     * @return absolute URI of the application
     */
    public URI getApplicationURI() {
        return new File(servletContext.getRealPath("/")).toURI(); // absolute session path
    }
}
