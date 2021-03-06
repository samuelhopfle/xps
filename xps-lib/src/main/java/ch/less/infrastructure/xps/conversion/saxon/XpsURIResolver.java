package ch.less.infrastructure.xps.conversion.saxon;

import ch.less.infrastructure.xps.conversion.documents.IPrintObject;
import ch.less.infrastructure.xps.i18n.DateFormatter;
import ch.less.infrastructure.xps.i18n.DateRepresentation;
import net.sf.saxon.lib.StandardURIResolver;
import net.sf.saxon.trans.XPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * URIResolver for XPS, some of the data can be generated by the string boot instnace
 * this class is used by saxon XSL transformer
 *
 * purpose:
 * - additional data for documents
 * - localized date formatting
 * - locale query
 * - language query
 * - ...
 */
public final class XpsURIResolver extends StandardURIResolver {

    // print object for further information
    private IPrintObject printObject;

    // logging
    private final static Logger LOG = LoggerFactory.getLogger(XpsURIResolver.class); //Message Log

    // keys for URI parsing
    private static final String DATE_KEY = "date";
    private static final String LOCALE_KEY = "locale";
    private static final String LANGUAGE_KEY = "language";
    private static final String XPS_INTERFACE_DATE_FORMAT = "yyyy-MM-dd";
    private static final int URI_FORMAT_POSITION = 1;
    private static final int URI_DATE_POSITION = 2;
    private static final String URI_DELIMITER = "/";

    /**
     * solely allow resolvers with a print object template
     *
     * @param printObject object template for printing
     */
    public XpsURIResolver(final IPrintObject printObject) {
        this.printObject = printObject;
    }

    // no standard constructor
    private XpsURIResolver() {
    }

    /**
     * resolve method of standard URI resolver interface
     *
     * @param href URI asked for
     * @param base base - here it's the URL of the file
     * @return Source object with data in XML form
     * @throws TransformerException error if not transformable
     */
    @Override
    public final Source resolve(final String href, final String base) throws XPathException {

        // parse locale and other document information - proprietary functionality
        if (href.equals(LOCALE_KEY)) { // locale
            return new DOMSource(createSingleValueSource(LOCALE_KEY, printObject.getLocale()));
        } else if (href.equals(LANGUAGE_KEY)) { // ISO language code
            return new DOMSource(createSingleValueSource(LANGUAGE_KEY, printObject.getLanguage()));
        } else if (href.startsWith(DATE_KEY)) { // date formatting/internationalization
            try {
                return new DOMSource(createSingleValueSource(DATE_KEY, parseAndFormatDate(href, printObject.getLocale())));
            } catch (ParseException e) { // in case of error ignore and log
                LOG.error(e.getLocalizedMessage());
                return super.resolve("", base); // fallback
            }
        } else { // fallback - standard processing
            return super.resolve(href, base);
        }
    }

    // helping method to parse info from href and create localized formatted date text
    private final String parseAndFormatDate(final String href, final String localeIdentifier) throws ParseException {

        final String[] dateParts = href.split(URI_DELIMITER); // it's always like this date/format/yyyy-mm-dd
        final Date date = (new SimpleDateFormat(XPS_INTERFACE_DATE_FORMAT)).parse(dateParts[URI_DATE_POSITION]);
        final Locale locale = Locale.forLanguageTag(localeIdentifier.replace('_', '-'));

        DateFormatter dateFormatter = new DateFormatter(
                DateRepresentation.fromURIPart(dateParts[URI_FORMAT_POSITION]), locale);
        return dateFormatter.format(date);
    }

    // helping method to create xml representation for data
    private final Document createSingleValueSource(final String key, final String value) {
        try {
            final XpsURIResolverDocWriter writer = new XpsURIResolverDocWriter(key);
            return writer.createDocument(value);
        } catch (ParserConfigurationException e) {
            LOG.error("Cannot write doc():" + e.getLocalizedMessage());
        }
        return null;
    }
}
