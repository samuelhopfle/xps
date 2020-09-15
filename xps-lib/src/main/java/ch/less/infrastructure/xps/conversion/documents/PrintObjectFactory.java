package ch.less.infrastructure.xps.conversion.documents;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * factory to create print objects
 */
public final class PrintObjectFactory {

    // creating an instance is not allowed
    private PrintObjectFactory() {
    }

    /**
     * creates a new XSL print object instance
     *
     * @param locale      locale with the language for the document
     * @param xsltFileURI URI of the XSLT file
     * @param xml         XML as a data stream
     * @return a print object to be used for XSL transformation
     */
    public final static IPrintObject newInstance(@NotNull final String locale, @NotNull final URI xsltFileURI, @NotNull final String xml)
            throws URISyntaxException, IOException {

        XsltPrintObject instance = new XsltPrintObject();
        instance.setLocale(locale);
        instance.setTemplate(xsltFileURI);
        instance.setXmlData(xml);

        return instance;
    }
}
