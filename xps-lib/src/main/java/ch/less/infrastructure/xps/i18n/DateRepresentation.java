package ch.less.infrastructure.xps.i18n;

/**
 * enumeration of alle types of date formatting that are offered for XSL Templates transformed with XPS
 */
public enum DateRepresentation {

    SHORT("short"), // /date/short/
    LONG("long"), // /date/long/
    MEDIUM("medium"), // /date/medium/
    MONTH("month"), // /date/month/
    MEDIUM_MONTH("medium_month"); // /date/medium_month/

    private String format;

    /**
     * create a value from an assigned String value
     *
     * @param uriPart representation as use in XSL template (see above)
     * @return a DateRepresentation to select from
     */
    public final static DateRepresentation fromURIPart(String uriPart) {
        return DateRepresentation.valueOf(uriPart.toUpperCase());
    }

    DateRepresentation(final String format) {
        this.format = format;
    }

    public final String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }
}
