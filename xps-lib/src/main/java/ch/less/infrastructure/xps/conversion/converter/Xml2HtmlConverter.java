package ch.less.infrastructure.xps.conversion.converter;

import ch.less.infrastructure.xps.conf.PrintSettingsService;
import ch.less.infrastructure.xps.conversion.saxon.XsltPrinter;
import ch.less.infrastructure.xps.conversion.documents.IPrintObject;
import ch.less.infrastructure.xps.conversion.documents.PrintObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * converter using XSLT to create HTML and XML
 */
@Service
public class Xml2HtmlConverter implements IXsltConverter {

    private final static Logger LOG = LoggerFactory.getLogger(Xml2HtmlConverter.class); //message log

    @Autowired
    private PrintSettingsService printSettingsService;

    // logging, KISS
    private final static String CANT_CREATE_HTML_TEXT = "Can't create HTML file - ";
    private final static String WRONG_URI_SYNTAX_TEXT = "Wrong URI Syntax - ";
    private final static String CANT_OPEN_TEXT = "Can't open file - ";
    private final static String CANT_CLOSE_OUTPUT_TEXT = "Can't close output stream - ";
    private final static String CANT_CONVERT_TEXT = "Can't convert XML to HTML";

    /**
     * creates a HTML document
     *
     * @param xml              XML content
     * @param templateFileName file name of the XSLT stylesheet
     * @param locale           locale for presentation and translation of the document
     * @return ByteArray with the document created
     */
    @Override
    public final byte[] convertXml(final String xml, final String templateFileName, final String locale) {

        // data stream for output
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            // get absolute template path
            final URI absoluteTemplateFileURI = printSettingsService.getAbsoluteTemplateFile(templateFileName);

            // prepare HTML data
            final IPrintObject printObject = PrintObjectFactory.newInstance(locale, absoluteTemplateFileURI, xml);
            final XsltPrinter printer = new XsltPrinter(printObject, new StreamResult(out));

            // generate a document
            printer.generateDocument();

            return out.toByteArray();

        } catch (TransformerException e) {  // possible exceptions
            throw new RuntimeException(CANT_CREATE_HTML_TEXT + e.getLocalizedMessage());
        } catch (URISyntaxException e) {
            LOG.error(WRONG_URI_SYNTAX_TEXT + e.getLocalizedMessage());
        } catch (IOException e) {
            LOG.error(CANT_OPEN_TEXT + e.getLocalizedMessage());
        } finally {
            // clean up output stream in any case
            try {
                out.close();
            } catch (IOException e) {
                LOG.error(CANT_CLOSE_OUTPUT_TEXT + e.getLocalizedMessage());  // just log, no error
            }
        }
        throw new RuntimeException(CANT_CONVERT_TEXT);
    }
}
