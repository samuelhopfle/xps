package ch.less.infrastructure.xps.conversion.saxon;

import ch.less.infrastructure.xps.conversion.documents.IPrintObject;
import net.sf.saxon.Configuration;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

/**
 * class to do XSL transformation with Saxon
 */
public final class XsltPrinter {

    private IPrintObject printObject; // print object with template and so on
    private Result dataOutput; // result of the transformation

    private final static String XSLT_TRANSFORMER_IMPLEMENTATION_CLASS = "net.sf.saxon.TransformerFactoryImpl";

    // no instance without values
    private XsltPrinter() {
    }

    /**
     * "Standard" constructor
     *
     * @param printObject document to be created
     * @param dataOutput  for XSLT -> the result of the transformation is being written on dataOutput
     */
    public XsltPrinter(IPrintObject printObject, Result dataOutput) {
        this.printObject = printObject;
        this.dataOutput = dataOutput;
    }

    /**
     * creates a document with data given
     *
     * @throws TransformerException if the document can not be created
     */
    public final void generateDocument() throws TransformerException {

        // XSLT transformer - Saxon is used because XSLT 2.0 support is needed
        final TransformerFactory transformerFactory = TransformerFactory
                .newInstance(XSLT_TRANSFORMER_IMPLEMENTATION_CLASS, null);  // XSLT / JAXP

        // resolver for external documents
        final XpsURIResolver resolver = new XpsURIResolver(printObject);
        final XpsCollectionFinder collectionFinder = new XpsCollectionFinder(printObject);

        // Saxon configuration of customized CollectionFinder implementation
        final Configuration xpsConfiguration = new Configuration();
        xpsConfiguration.setCollectionFinder(collectionFinder);

        // create and configure the transformer
        transformerFactory.setAttribute(net.sf.saxon.lib.FeatureKeys.CONFIGURATION, xpsConfiguration);
        final Transformer transformer = transformerFactory.newTransformer(printObject.getTemplate());

        // customized URIResolver to access special values that are needed inside XSL templates (f.e. the language
        // that is a service call parameter)
        transformer.setURIResolver(resolver); // attach URI resolver

        // XSLT transformation and FOP/HTML processing
        transformer.transform(printObject.getXmlData(), dataOutput);
    }
}
