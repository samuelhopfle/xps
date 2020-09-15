package ch.less.infrastructure.xps.i18n;

import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * correct internationalization errors in java version 1.8 (this is an LTS)
 */
public class Java8DateFormatCorrector implements IDateFormatCorrector {

    // no magic numbers
    private final static String IS_NUMBERING_IN_FR_IT = ".";
    private final static String SHOULD_NUMBERING_IN_FR_IT = "";
    private final static char IS_SEPARATOR_MEDIUM_IT = '-';
    private final static char SHOULD_SEPARATOR_MEDIUM_IT = ' ';
    private final static String FIRST_DAY_FROM_STD = "1 ";
    private final static String FIRST_DAY_FR = "1er ";
    private final static String FIRST_DAY_IT = "1° ";

    @Override
    public final String correct(@NotNull final String dateString, @NotNull final DateRepresentation dateRepresentation,
                                @NotNull final Locale locale) {

        final String dateWithCorrectNumbering = correctNumbering(dateString, dateRepresentation, locale);
        final String dateWithCorrectSeparators = correctSeparators(dateWithCorrectNumbering, dateRepresentation, locale);
        final String dateWithCorrectFirstDay = correctFirstDay(dateWithCorrectSeparators, dateRepresentation, locale);
        final String dateWithCorrectCase = correctCase(dateWithCorrectFirstDay, dateRepresentation, locale);

        return dateWithCorrectCase;

    }

    // helping method to get rid of dashes in certain situations
    private final String correctSeparators(@NotNull final String dateString, @NotNull final DateRepresentation dateRepresentation,
                                           @NotNull final Locale locale) {
        // italian medium format has dashes as separators instead of spaces
        if (locale.getLanguage().equals(Locale.ITALIAN.getLanguage()) &&
                dateRepresentation.equals(DateRepresentation.MEDIUM))
            return dateString.replace(IS_SEPARATOR_MEDIUM_IT, SHOULD_SEPARATOR_MEDIUM_IT);
        return dateString;
    }

    // helping method to get rid of the points in certain situations
    private final String correctNumbering(@NotNull final String dateString, @NotNull final DateRepresentation dateRepresentation,
                                          @NotNull final Locale locale) {
        // no points in italian and french
        if (locale.getLanguage().equals(Locale.ITALIAN.getLanguage()) || // italian
                locale.getLanguage().equals(Locale.FRENCH.getLanguage())) { // french
            // only in medium and long formats
            if (dateRepresentation.equals(DateRepresentation.MEDIUM)
                    || dateRepresentation.equals(DateRepresentation.LONG))
                // only first day with makes at max 3 characters
                return dateString.substring(0, 3).replace(IS_NUMBERING_IN_FR_IT, SHOULD_NUMBERING_IN_FR_IT)
                        + dateString.substring(3);
        }
        return dateString;
    }

    // helping method for standard adjustment on first day - redundant to std date formatter
    private final String correctFirstDay(@NotNull final String dateString, @NotNull final DateRepresentation dateRepresentation,
                                         @NotNull final Locale locale) {
        if (dateString.startsWith(FIRST_DAY_FROM_STD)) {
            // special cases are the first for italian and french
            if (locale.getLanguage().equals(Locale.ITALIAN.getLanguage())) // italian
                return dateString.replace(FIRST_DAY_FROM_STD, FIRST_DAY_IT);
            else if (locale.getLanguage().equals(Locale.FRENCH.getLanguage())) // french
                return dateString.replace(FIRST_DAY_FROM_STD, FIRST_DAY_FR);
        }
        return dateString;
    }

    // helping method for correct upper / lower case in certain situations
    private final String correctCase(@NotNull final String dateString, @NotNull final DateRepresentation dateRepresentation,
                                     @NotNull final Locale locale) {
        // italian month formatting small
        if (locale.getLanguage().equals(Locale.ITALIAN.getLanguage()) && dateRepresentation.equals(DateRepresentation.MONTH))
            return dateString.toLowerCase();

        return dateString;
    }
}
