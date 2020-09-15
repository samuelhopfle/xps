package ch.less.infrastructure.xps.conversion.converter;

import ch.less.infrastructure.xps.conf.PrintSettingsService;
import ch.less.infrastructure.xps.conversion.saxon.XsltPrinter;
import ch.less.infrastructure.xps.conversion.documents.IPrintObject;
import ch.less.infrastructure.xps.conversion.documents.PrintObjectFactory;

import org.apache.fop.apps.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * converter using XSLT to create PDF with XML
 */
@Service
public class Xml2PdfConverter implements IXsltConverter {

    private final static Logger LOG = LoggerFactory.getLogger(Xml2PdfConverter.class); // message log

    private PrintSettingsService printSettingsService;
    private FopFactory fopFactory;

    @Autowired
    public Xml2PdfConverter(final PrintSettingsService printSettingsService){
        this.printSettingsService = printSettingsService;
        try { // at the moment it's a set the fop factory based on configuration
            fopFactory = createFopFactoryBasedOnConfig(printSettingsService.getFopConfigurationFilename());
        } catch (URISyntaxException | SAXException | IOException e){
            LOG.error(CANT_CREATE_FOP_FACTORY_TEXT);
        }
    }

    // logging, KISS
    private final static String CANT_CREATE_PDF_TEXT = "Can't create PDF file - ";
    private final static String WRONG_URI_SYNTAX_TEXT = "Wrong URI Syntax - ";
    private final static String CANT_CLOSE_OUTPUT_TEXT = "Can't close output stream - ";
    private final static String CANT_CONVERT_TEXT = "Can't convert XML to PDF";
    private final static String CANT_CREATE_FOP_FACTORY_TEXT = "Can't create FOP factory";
    private final static String NO_FOP_CONFIGURATION_FILE_SET_NEXT = "No FOP configuration set, using defaults";
    private final static String FILE_ROOT = "."; // no magic number ;)

    /**
     * creates a PDF document
     *
     * @param xml              XML content
     * @param templateFileName file name of the XSLT stylesheet
     * @param locale           locale for presentation and translation of the document
     * @return ByteArray with the document created
     */
    public final byte[] convertXml(final String xml, final String templateFileName, final String locale) {

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        // create pdf and transform to a ByteArray stream
        try {

            // https://xmlgraphics.apache.org/fop/0.95/embedding.html
            // https://xmlgraphics.apache.org/fop/2.3/upgrading.html

            // change template path to absolute
            final URI absoluteTemplateFileURI = printSettingsService.getAbsoluteTemplateFile(templateFileName);
            final FOUserAgent userAgent = fopFactory.newFOUserAgent();
            final Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, out);

            // prepare PDF Data
            final IPrintObject printObject = PrintObjectFactory.newInstance(locale, absoluteTemplateFileURI, xml);
            final XsltPrinter printer = new XsltPrinter(printObject, new SAXResult(fop.getDefaultHandler()));

            // clean up output stream in any case
            printer.generateDocument();
            return out.toByteArray();

        } catch (SAXException | TransformerException | IOException e) {
            LOG.error(CANT_CREATE_PDF_TEXT + e.getLocalizedMessage());
        } catch (URISyntaxException e) {
            LOG.error(WRONG_URI_SYNTAX_TEXT + e.getLocalizedMessage());
        } finally {
            // clean up output stream
            try {
                out.close();
            } catch (IOException e) {
                LOG.error(CANT_CLOSE_OUTPUT_TEXT + e.getLocalizedMessage());  // just logging, no error
            }
        }
        throw new RuntimeException(CANT_CONVERT_TEXT);
    }

    // create factory based on the configuration, if available
    private final FopFactory createFopFactoryBasedOnConfig(final URI fopConfigFilePath)
            throws IOException, SAXException {
        if (fopConfigFilePath != null){ // check file name
            final File fopConfigurationFile = new File(fopConfigFilePath);
            if (fopConfigurationFile.exists())  // is a file there?
                return FopFactory.newInstance(fopConfigurationFile);
        }
        // if not
        LOG.info(NO_FOP_CONFIGURATION_FILE_SET_NEXT);
        return FopFactory.newInstance(new File(FILE_ROOT).toURI());
    }
}
