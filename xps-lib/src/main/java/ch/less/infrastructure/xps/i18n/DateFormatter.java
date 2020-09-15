package ch.less.infrastructure.xps.i18n;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * helper class to format dates according to xps syntax and worldwide language standards
 */
public class DateFormatter {

    // month formatting - no magic numbers
    private static final String MONTH_FORMAT = "MMMM";
    private static final String MED_MONTH_FORMAT = "MMM";

    private DateRepresentation dateRepresentation;
    private Locale locale;

    public DateFormatter(final DateRepresentation dateRepresentation, final Locale locale) {
        this.dateRepresentation = dateRepresentation;
        this.locale = locale;
    }

    /**
     * @param date date format according to XPS guidelines
     * @return formated date
     */
    public final String format(final Date date) {

        final DateFormat dateFormat;

        // switch representations
        switch (dateRepresentation) {
            case MEDIUM:
                dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
                break;
            case LONG:
                dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
                break;
            case MONTH:
                dateFormat = new SimpleDateFormat(MONTH_FORMAT, locale);
                break;
            case MEDIUM_MONTH:
                dateFormat = new SimpleDateFormat(MED_MONTH_FORMAT, locale);
                break;
            default: // case SHORT: der Rest ist unsupported
                dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                break;
        }
        // standard formatting
        final String formattedDateText =  dateFormat.format(date);

        // proprietary corrections
        final IDateFormatCorrector corrector = DateFormatCorrectorFactory.newInstance();
        return corrector.correct(formattedDateText, dateRepresentation, locale);
    }

}
