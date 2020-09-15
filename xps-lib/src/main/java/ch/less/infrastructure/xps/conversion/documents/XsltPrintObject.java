package ch.less.infrastructure.xps.conversion.documents;

import ch.less.infrastructure.xps.share.XpsURIHelper;
import lombok.Data;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.net.URI;

/**
 * represents all data to be use with a XSLT transformation as for example PDF or HTML documents
 */
@Data
public final class XsltPrintObject implements IPrintObject {

    // local keeping of interface data
    private String locale;
    private Source template, xmlData;


    @Override
    public String getLanguage() {
        // change language from saved locale information
        if (locale != null)
            return locale.substring(0, 2); // de_CH -> de
        return null;
    }

    /**
     * set template based on a file URI - open the file and read the conent
     *
     * @param templateFileURI file path of the XSL template file
     */
    public final void setTemplate(final URI templateFileURI) {
        this.template = XpsURIHelper.openSource(templateFileURI);
    }

    /**
     * set content based on a String, keep it as a Stream Source
     *
     * @param xmlData XML context as a String
     */
    public final void setXmlData(final String xmlData) {
        this.xmlData = new StreamSource(new StringReader(xmlData));
    }
}
