package ch.less.infrastructure.controller;

import ch.less.infrastructure.xps.conversion.converter.Xml2HtmlConverter;
import ch.less.infrastructure.xps.conversion.converter.Xml2PdfConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for generating content (HTML/PDF) with XML
 */
@RestController
@RequestMapping("/")
public final class PrintController {

    /**
     * PDF conversion service -> creates PDF content
     */
    @Autowired
    private Xml2PdfConverter pdfConverter; // Service
    @Autowired
    private Xml2HtmlConverter htmlConverter; // Service
    @Autowired
    private HttpServletRequest request; // Servlet Request

    private final static String TEMPLATE_PARAM_NAME = "template";

    // logging, KISS
    private final static String PDF_FROM_TEMPLATE_TEXT =  "Building PDF from template: ";
    private final static String HTML_FROM_TEMPLATE_TEXT =  "Building HTML from template: ";
    private final static String XML_DIRECT_TEXT = "Returning plain XML";
    private final static String BRACKETS_OPEN =  " (";
    private final static String BRACKETS_CLOSE =  ")";

    // message log
    private final static Logger LOG = LoggerFactory.getLogger(PrintController.class);

    /**
     * REST service to create a PDF file based on XML and XSL
     *
     * @param templateFilename XSL-FO file path (absolute)
     * @param xml              XML file content, accessed directly from HTTP body of the POST requests (in UTF-8)
     * @param locale           locale for the language of the document
     * @return ByteArray to PDF file
     */
    @RequestMapping(value = "/pdf/{locale}", method = RequestMethod.POST, produces = MediaType.APPLICATION_PDF_VALUE)
    private final ResponseEntity<byte[]> createPdf(@RequestParam(TEMPLATE_PARAM_NAME) final String templateFilename,
                                                   @RequestBody final String xml,
                                                   @PathVariable final String locale) {
        try {
            LOG.info(PDF_FROM_TEMPLATE_TEXT + templateFilename + BRACKETS_OPEN + request.getRemoteHost() + BRACKETS_CLOSE);

            // create http headers with proper type
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // generate document
            final byte[] result = pdfConverter.convertXml(xml, templateFilename, locale);
            return new ResponseEntity<byte[]>(result, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            LOG.error(e.getLocalizedMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * REST Service um aus XML und XSL eine HTML Datei zu erstellen
     * REST service to create HTML based on XML and XSL
     *
     * @param templateFilename XSL-FO file path (absolute)
     * @param xml              XML file content, accessed directly from HTTP body of the POST requests (in UTF-8)
     * @param locale           locale for the language of the document
     * @return ByteArray to HTML file
     */
    @RequestMapping(value = "/html/{locale}", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    private final ResponseEntity<byte[]> createHtml(@RequestParam(TEMPLATE_PARAM_NAME) final String templateFilename,
                                                    @RequestBody final String xml,
                                                    @PathVariable final String locale) {
        try {
            LOG.info(HTML_FROM_TEMPLATE_TEXT + templateFilename + BRACKETS_OPEN + request.getRemoteHost() + BRACKETS_CLOSE);

            // create http headers with proper type
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);

            // generate document
            final byte[] result = htmlConverter.convertXml(xml, templateFilename, locale);
            return new ResponseEntity<byte[]>(result, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }

    /**
     * REST service to directly give back the xml content (for debugging view cases)
     *
     * @param xml              XML file content, accessed directly from HTTP body of the POST requests (in UTF-8)
     * @param locale           locale for the language of the document
     * @return ByteArray to XML String
     */
    @RequestMapping(value = "/xml/{locale}", method = RequestMethod.POST, produces = MediaType.APPLICATION_XML_VALUE)
    private final ResponseEntity<String> createXml(@RequestBody final String xml,
                                                   @PathVariable final String locale) {
        try {
            LOG.info(XML_DIRECT_TEXT + BRACKETS_OPEN + request.getRemoteHost() + BRACKETS_CLOSE);

            // create http header for plain XML
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_XML);

            // return input as output with proper headers
            return new ResponseEntity<String>(xml, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
    }
}
