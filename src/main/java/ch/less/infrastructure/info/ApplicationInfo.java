package ch.less.infrastructure.info;

import ch.less.infrastructure.xps.conf.PrintSettingsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Application information likes paths, versions and build info
 **/

@Service
public class ApplicationInfo {

    /**
     * setting keys
     **/
    private static final String FILE_LOG = "lgging.file.key"; // Logger KEY entry


    @Autowired
    private PrintSettingsService printSettingsService;
    @Autowired
    private Environment environment;
    @Autowired
    private BuildProperties buildProperties;

    /**
     * returns the base path of the application (useful for logs, templates and configuration)
     *
     * @return absolute URI of the application
     */
    public final URI getApplicationURI() {
        return printSettingsService.getApplicationURI(); // absolute session path
    }

    /**
     * returns Log4J2 File Name
     *
     * @return absolute URI of the file
     */
    public final URI getLoggingURI() {

        // logging context and root logger
        final LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        final Logger logger = loggerContext.getRootLogger();

        // file appender
        final RollingFileAppender appender = (RollingFileAppender) logger.getAppenders().get(environment.getProperty(FILE_LOG));
        final String fileName = appender.getFileName();

        // is this path already absolute?
        final File logFile = new File(fileName);
        if (!logFile.isAbsolute()) {
            return getApplicationURI().resolve(logFile.toURI()); // if not create absolute path
        }

        return logFile.toURI();
    }

    /**
     * latest build version according to gradle
     *
     * @return gradle build version for XPS
     */
    public final String getBuildVersion() {
        return buildProperties.getVersion();
    }

    /**
     * returns latest build timestamp of the current version
     *
     * @return date of the current build
     */
    public final LocalDateTime getBuildDate() throws ParseException {
        return LocalDateTime.ofInstant(buildProperties.getTime(), ZoneOffset.systemDefault()); // date
    }
}
