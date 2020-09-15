package ch.less.infrastructure.xps.conversion.saxon;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Optional;

/**
 * helper class to create XML documents for XSLT doc() and document() functions
 */
final class XpsURIResolverDocWriter {

    // root element, name of the document
    private String rootName;

    /**
     * creates a XML document with the desired root tag
     *
     * @param rootName base name for access
     */
    XpsURIResolverDocWriter(String rootName) {
        this.rootName = rootName;
    }

    // no instances allowed
    private XpsURIResolverDocWriter() {
    }

    /**
     * creates an xml document for further processing
     *
     * @return XML DOM document
     * @throws ParserConfigurationException in case of an unvalid configuration
     */
    final Document createDocument() throws ParserConfigurationException {

        // create document
        final Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .newDocument();

        // create elements and fill values
        final Element rootElement = document.createElement(rootName);
        document.appendChild(rootElement);

        return document;
    }

    /**
     * creates an xml document for further processing with exactly one value in it
     *
     * @param singleValue value in the document
     * @return XML DOM document with the value as 'docValue'
     * @throws ParserConfigurationException in case of an unvalid configuration
     */
    final Document createDocument(final String singleValue) throws ParserConfigurationException {
        // create document with one value
        final Optional<Document> document = Optional.of(createDocument());
        document.ifPresent(value -> value.getDocumentElement().setTextContent(singleValue));

        return document.get();
    }
}