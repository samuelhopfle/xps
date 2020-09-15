package ch.less.infrastructure.xps.conversion.converter;

import static org.mockito.Mockito.when;

import ch.less.infrastructure.xps.conf.PrintSettingsService;
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Testing basic functionality of the XML to HTML Converter
 */
@RunWith(MockitoJUnitRunner.class)
public class Xml2HtmlConverterTest extends TestCase {

    @InjectMocks
    private Xml2HtmlConverter converter;
    @Mock
    private PrintSettingsService printSettingsService;

    // test file paths and settings
    private final static String XML_TEST_FILE_PATH = "src/test/resources/test.xml";
    private final static String TEMPLATE_TEST_FILE_PATH = "src/test/resources/test.xslt";
    private final static String TEST_ROOT_PATH = ".";
    private final static String TEST_LOCALE = "de_CH";

    // make sure NULLs are not allowed - no defaults are used
    @Test(expected = NullPointerException.class)
    public void testConversionInterfaceXmlNullError() {
        converter.convertXml(null, "", "");
    }

    @Test(expected = NullPointerException.class)
    public void testConversionInterfaceTemplateNullError() {
        converter.convertXml("", null, "");
    }

    @Test(expected = NullPointerException.class)
    public void testConversionInterfaceLocalelNullError() {
        converter.convertXml("", "", null);
    }

    // perform a simple test conversation
    @Test
    public void testConversation() throws IOException, URISyntaxException {

        // mock the template path
        when(printSettingsService.getAbsoluteTemplateFile(TEMPLATE_TEST_FILE_PATH))
                .thenReturn(Paths.get(TEMPLATE_TEST_FILE_PATH).toAbsolutePath().toUri());

        // load data and do the transformation
        final String xmlContent = new String(Files.readAllBytes(Paths.get(XML_TEST_FILE_PATH)));
        final byte[] documentAsBytes = converter.convertXml(xmlContent, TEMPLATE_TEST_FILE_PATH, TEST_LOCALE);

        // as simple as possible yet showing that it worked
        assertTrue((new String(documentAsBytes)).contains("<h1>test</h1>"));
    }
}
