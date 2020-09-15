package ch.less.infrastructure.xps.conversion.documents;

import javax.xml.transform.Source;

/**
 * interface for representing a print object intended to be used with XSL transformation
 */
public interface IPrintObject {

    /**
     * returns the locale of a print object
     *
     * @return (ISO conform) locale as a String
     */
    public String getLocale();

    /**
     * returns the language of a print object
     *
     * @return (ISO conform) language as a String
     */
    public String getLanguage();

    /**
     * returns the path and the filename of a xsl template as a source
     *
     * @return path filename of the XSL template as a source
     */
    public Source getTemplate();

    /**
     * returns XML data to be transformed
     *
     * @return xml data as a source
     */
    public Source getXmlData();
}
