package ch.less.infrastructure.xps.i18n;

import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * corrector for Java date formatting for the first day of any month in italian and french
 * for java 11 (LTS) plus, that's all you need
 * tested with java 11,12,13,14
 */
public class DateFormatCorrector implements IDateFormatCorrector {

    // no magic numbers
    private final static String FIRST_DAY_FROM_STD = "1 ";
    private final static String FIRST_DAY_FRENCH = "1er ";
    private final static String FIRST_DAY_ITALIAN = "1° ";

    @Override
    public final String correct(@NotNull final String dateString, @NotNull final DateRepresentation dateRepresentation, @NotNull final Locale locale) {

        if (dateString.startsWith(FIRST_DAY_FROM_STD)) {
            // special cases are the first for italian and french
            if (locale.getLanguage().equals(Locale.ITALIAN.getLanguage())) // italian
                return dateString.replace(FIRST_DAY_FROM_STD, FIRST_DAY_ITALIAN);
            else if (locale.getLanguage().equals(Locale.FRENCH.getLanguage())) // french
                return dateString.replace(FIRST_DAY_FROM_STD, FIRST_DAY_FRENCH);
        }
        return dateString;
    }
}
