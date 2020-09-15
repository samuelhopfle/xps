package ch.less.infrastructure.xps.conversion.saxon;

import ch.less.infrastructure.xps.share.XpsURIHelper;
import ch.less.infrastructure.xps.conversion.documents.IPrintObject;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ResourceCollection;
import net.sf.saxon.resource.StandardCollectionFinder;
import net.sf.saxon.trans.XPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * customized XSL collection finder class to handle network paths
 * this class is used by saxon XSL transformer
 */
public final class XpsCollectionFinder extends StandardCollectionFinder {

    private final static Logger LOG = LoggerFactory.getLogger(XpsCollectionFinder.class); // message Log

    // print object for additional information
    private IPrintObject printObject;

    /**
     * Nur Finder mit Objekt-Hülle zulassen
     * just accept finders implementing the object interface
     *
     * @param printObject Objekt-Hülle für den Druck
     */
    public XpsCollectionFinder(final IPrintObject printObject) {
        this.printObject = printObject;
    }

    // don't create instances here
    private XpsCollectionFinder() {
    }

    @Override
    public ResourceCollection findCollection(final XPathContext context, final String collectionURI) throws XPathException {
        return super.findCollection(context, XpsURIHelper.extendURIForNetzwork(collectionURI));
    }
}
