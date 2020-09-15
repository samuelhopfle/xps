package ch.less.infrastructure.xps.conversion.converter;

import ch.less.infrastructure.xps.conf.PrintSettingsService;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;

/**
 * Testing basic functionality of the XML to PDF Converter
 */
@RunWith(SpringRunner.class)
public class Xml2PdfConverterTest extends TestCase {

    @Mock
    private PrintSettingsService printSettingsServiceWithFopConfig;
    @Mock
    private PrintSettingsService printSettingsServiceWithoutFopConfig;

    private Xml2PdfConverter converterWithFopConfig;
    private Xml2PdfConverter converterWithoutFopConfig;

    // test file paths and settings
    private final static String XML_TEST_FILE_PATH = "src/test/resources/test.xml";
    private final static String TEMPLATE_TEST_FILE_PATH = "src/test/resources/test.fo";
    private final static String CONFIGURATION_TEST_FILE_PATH = "src/test/resources/fop.xconf";
    private final static String TEST_ROOT_PATH = ".";
    private final static String TEST_LOCALE = "de_CH";

    @Before
    public void initConverter() throws URISyntaxException{
        // mock the template and configuration path with FOP config
        when(printSettingsServiceWithFopConfig.getAbsoluteTemplatePath())
                .thenReturn(Paths.get(TEST_ROOT_PATH).toUri());
        when(printSettingsServiceWithFopConfig.getAbsoluteTemplateFile(TEMPLATE_TEST_FILE_PATH))
                .thenReturn(Paths.get(TEMPLATE_TEST_FILE_PATH).toAbsolutePath().toUri());
        when(printSettingsServiceWithFopConfig.getFopConfigurationFilename())
                .thenReturn(Paths.get(CONFIGURATION_TEST_FILE_PATH).toUri());

        converterWithFopConfig = new Xml2PdfConverter(printSettingsServiceWithFopConfig);

        // mock the template and configuration path without FOP config
        when(printSettingsServiceWithoutFopConfig.getAbsoluteTemplatePath())
                .thenReturn(Paths.get(TEST_ROOT_PATH).toUri());
        when(printSettingsServiceWithoutFopConfig.getAbsoluteTemplateFile(TEMPLATE_TEST_FILE_PATH))
                .thenReturn(Paths.get(TEMPLATE_TEST_FILE_PATH).toAbsolutePath().toUri());
        when(printSettingsServiceWithoutFopConfig.getFopConfigurationFilename())
                .thenReturn(null);

        converterWithoutFopConfig = new Xml2PdfConverter(printSettingsServiceWithoutFopConfig);

    }

    // make sure NULLs are not allowed - no defaults are used
    @Test(expected = NullPointerException.class)
    public void testConversionInterfaceXmlNullError() {
        converterWithoutFopConfig.convertXml(null, "", "");
    }

    @Test(expected = NullPointerException.class)
    public void testConversionInterfaceTemplateNullError() {
        converterWithoutFopConfig.convertXml("", null, "");
    }

    @Test(expected = NullPointerException.class)
    public void testConversionInterfaceLocalelNullError() {
        converterWithoutFopConfig.convertXml("", "", null);
    }

    // perform a simple test conversation
    @Test
    public void testConversationWithConf() throws IOException, URISyntaxException {

        // load data and do the transformation
        final String xmlContent = new String(Files.readAllBytes(Paths.get(XML_TEST_FILE_PATH)));
        final byte[] documentAsBytes = converterWithFopConfig.convertXml(xmlContent, TEMPLATE_TEST_FILE_PATH, TEST_LOCALE);

        // we can't read PDF here but make sure there was no error and the file generated has a bigger size than zero
        assertNotSame(0, documentAsBytes.length);
    }

    // perform a simple test conversation
    @Test
    public void testConversationWithoutConf() throws IOException, URISyntaxException {

        // load data and do the transformation
        final String xmlContent = new String(Files.readAllBytes(Paths.get(XML_TEST_FILE_PATH)));
        final byte[] documentAsBytes = converterWithoutFopConfig.convertXml(xmlContent, TEMPLATE_TEST_FILE_PATH, TEST_LOCALE);

        // we can't read PDF here but make sure there was no error and the file generated has a bigger size than zero
        assertNotSame(0, documentAsBytes.length);
    }
}