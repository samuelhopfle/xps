package ch.less.infrastructure.xps.i18n;

import javax.validation.constraints.NotNull;
import java.util.Locale;

/**
 * interface for different date format correctors
 */
public interface IDateFormatCorrector {

    /**
     * method corrects a date given on basis of a locale and the desired representation
     *
     * @param dateString date to be corrected as a String representation
     * @param dateRepresentation representation / format chosen for the date
     * @param locale locale to be use
     * @return a formatted and corrected date
     */
    public String correct(@NotNull final String dateString, @NotNull final DateRepresentation dateRepresentation,
                          @NotNull final Locale locale);
}
