package ch.less.infrastructure.controller;

import ch.less.infrastructure.configuration.InfoPageConfiguration;
import ch.less.infrastructure.xps.conf.PrintSettingsService;
import ch.less.infrastructure.info.ApplicationInfo;
import org.apache.catalina.util.ServerInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import static j2html.TagCreator.*;

/**
 * REST controller for information on the print service (germon only yet)
 */
@RestController
@RequestMapping("/")
public class InfoController {

    private final PrintSettingsService printSettingsService;
    private final ApplicationInfo applicationInfo;
    private final Environment environment;
    private final ResourceBundle messagesBundle;

    public InfoController(@Qualifier(value = "printSettingsService") PrintSettingsService printSettingsService,
                          @Qualifier(value = "applicationInfo") ApplicationInfo applicationInfo,
                          @Qualifier(value = "environment") Environment environment,
                          @Qualifier(value = "infoPageConfiguration") InfoPageConfiguration infoConfiguration) {
        this.printSettingsService = printSettingsService;
        this.applicationInfo = applicationInfo;
        this.environment = environment;

        messagesBundle = ResourceBundle.getBundle( // as it is fixed anyways I put it here
                MESSAGE_BUNDLE_NAME, new Locale(infoConfiguration.getLocale()));
    }

    // HTML attribute values
    private final static String CACHE_CONTROL_KEY = "Cache-Control";
    private final static String CACHE_CONTROL_VALUE = "no-cache,no-store,must-revalidate";
    private final static String TABLE_PARAM_CLASS = "param";

    // i18n
    private final static String MESSAGE_BUNDLE_NAME = "MessagesBundle";

    @RequestMapping(value = "/")
    public final ResponseEntity<String> xpsInformation() throws Exception {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        headers.set(CACHE_CONTROL_KEY, CACHE_CONTROL_VALUE);

        final String response = html(
                head(
                        title(messagesBundle.getString("info.app_title")),
                        meta().withCharset(StandardCharsets.UTF_8.toString()),
                        meta().attr("http-equiv", "Content-Type").withContent("text/html; charset=UTF-8"),
                        meta().attr("http-equiv", "X-UA-Compatible").withContent("IE=Edge"),
                        link().withRel("stylesheet").withType("text/css").withHref("css/xps.css"),
                        style("body { font-size: 1em } " +
                                "table { width: 100%; max-width: 80em; } " +
                                "td.param { width: 15em; } ")
                ),
                body(
                        h1(messagesBundle.getString("info.app_title")),
                        table(
                                tr(
                                        td(messagesBundle.getString("info.build_version")).withClass(TABLE_PARAM_CLASS),
                                        td(applicationInfo.getBuildVersion())
                                ),
                                tr(
                                        td(messagesBundle.getString("info.build_date")).withClass(TABLE_PARAM_CLASS),
                                        td(getBuildDateTimeText())
                                ),
                                tr(
                                        td(messagesBundle.getString("info.app_path")).withClass(TABLE_PARAM_CLASS),
                                        td(applicationInfo.getApplicationURI().toString())
                                )
                        ),
                        h2(messagesBundle.getString("info.third_party_sw")),
                        table(
                                tr(
                                        td(messagesBundle.getString("info.java_version")).withClass(TABLE_PARAM_CLASS),
                                        td(System.getProperty("java.version"))
                                ),
                                tr(
                                        td(messagesBundle.getString("info.java_home")).withClass(TABLE_PARAM_CLASS),
                                        td(System.getProperty("java.home"))
                                ),
                                tr(
                                        td(messagesBundle.getString("info.java_container")).withClass(TABLE_PARAM_CLASS),
                                        td(ServerInfo.getServerInfo())
                                ),
                                tr(
                                        td(messagesBundle.getString("info.system_user")).withClass(TABLE_PARAM_CLASS),
                                        td(System.getProperty("user.name"))
                                )
                        ),
                        h2(messagesBundle.getString("info.settings")),
                        table(
                                tr(
                                        td(messagesBundle.getString("info.log_file")).withClass(TABLE_PARAM_CLASS),
                                        td(applicationInfo.getLoggingURI().toString())
                                ),
                                tr(
                                        td(messagesBundle.getString("info.fop_config")).withClass(TABLE_PARAM_CLASS),
                                        td(getTopConfigurationFilenameRepresentation())
                                ),
                                tr(
                                        td(messagesBundle.getString("info.template_path")).withClass(TABLE_PARAM_CLASS),
                                        td(getAbsoluteTemplatePathRepresentation())
                                )
                        )
                )
        ).renderFormatted();

        return new ResponseEntity<String>(response, headers, HttpStatus.OK);
    }

    // helping method - get readable absolute template path and check for existence - if not, include in the text
    private final String getAbsoluteTemplatePathRepresentation() {
        try {
            final URI templateURI = printSettingsService.getAbsoluteTemplatePath();
            final File templatePath = new File(templateURI);

            // does the path exist
            if (templatePath != null && templatePath.exists() && templatePath.isDirectory())
                return templateURI.toString();

            // in case it can not be found or any other error
            final Object[] messageArgs = {templateURI};
            final MessageFormat formatter = new MessageFormat(messagesBundle.getString("message.path_not_found"), messagesBundle.getLocale());
            return formatter.format(messageArgs);

        } catch (URISyntaxException e) {
            final Object[] messageArgs = {e.getLocalizedMessage()};
            final MessageFormat formatter = new MessageFormat(messagesBundle.getString("message.config_error"), messagesBundle.getLocale());
            return formatter.format(messageArgs);
        }

    }

    // helping method - get readable FOP configuration file path and check for existence - if not, include in the text
    private final String getTopConfigurationFilenameRepresentation() {

        try {
            final URI fopConfigurationURI = printSettingsService.getFopConfigurationFilename();
            final File fopConfigurationFile = new File(fopConfigurationURI);

            // Existiert die Datei?
            if (fopConfigurationFile != null && fopConfigurationFile.exists())
                return fopConfigurationURI.toString();

            // in case it can not be found or any other error
            final Object[] messageArgs = {fopConfigurationURI.toString()};
            final MessageFormat formatter = new MessageFormat(messagesBundle.getString("message.path_not_found"), messagesBundle.getLocale());
            return formatter.format(messageArgs);

        } catch (URISyntaxException e) {
            final Object[] messageArgs = {e.getLocalizedMessage()};
            final MessageFormat formatter = new MessageFormat(messagesBundle.getString("message.config_error"), messagesBundle.getLocale());
            return formatter.format(messageArgs);
        }
    }

    // helping method - get readable build date
    private final String getBuildDateTimeText() {
        // i18n conform date formatting
        DateTimeFormatter dateFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).localizedBy(messagesBundle.getLocale());

        try {
            return dateFormat.format(applicationInfo.getBuildDate());
            // in case it can not be found or any other error
        } catch (ParseException e) {
            final Object[] messageArgs = {e.getLocalizedMessage()};
            final MessageFormat formatter = new MessageFormat(messagesBundle.getString("message.general_error"), messagesBundle.getLocale());
            return formatter.format(messageArgs);
        }
    }
}
