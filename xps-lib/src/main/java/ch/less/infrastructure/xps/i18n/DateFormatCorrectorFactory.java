package ch.less.infrastructure.xps.i18n;

/**
 * factory to create the format correctors needed
 */
public final class DateFormatCorrectorFactory {

    // no instance of this class
    private DateFormatCorrectorFactory() {
    }

    /**
     * creates to proper corrector for the current Java Version
     *
     * @return
     */
    public final static IDateFormatCorrector newInstance() {

        // Java 1.8
        if (System.getProperty("java.version").startsWith("1.8")) {
            return new Java8DateFormatCorrector();
        }
        // Java 11 (LTS) +
        return new DateFormatCorrector();
    }
}
