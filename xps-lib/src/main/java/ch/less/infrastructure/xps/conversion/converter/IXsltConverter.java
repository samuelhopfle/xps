package ch.less.infrastructure.xps.conversion.converter;

/**
 * XSLT converter interface to implement your own XSL based converters
 */
public interface IXsltConverter {

    /**
     * creates a document of any type (ByteArray can be a PDF, HTML or whatever) based on XML, an XSLT stylesheet and a locale
     *
     * @param xml              XML content
     * @param templateFileName file name of the XSLT stylesheet
     * @param locale           locale for presentation and translation of the document
     * @return ByteArray with the document created
     */
    byte[] convertXml(final String xml, final String templateFileName, final String locale);

}
