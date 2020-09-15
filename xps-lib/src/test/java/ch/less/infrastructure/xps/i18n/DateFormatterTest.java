package ch.less.infrastructure.xps.i18n;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

/**
 * testing proprietary date i18n functionality
 */
@RunWith(SpringRunner.class)
public class DateFormatterTest extends TestCase {

    // no magic numbers - testcases
    final static LocalDate localDateFirst = LocalDate.of(2020, 12, 1);
    final static LocalDate localDateSecond = LocalDate.of(2020, 12, 11);

    final static String MEDIUM_FIRST_DAY_IT = "1° dic 2020";
    final static String MEDIUM_SECOND_DAY_IT = "11 dic 2020";
    final static String LONG_FIRST_DAY_IT = "1° dicembre 2020";
    final static String LONG_SECOND_DAY_IT = "11 dicembre 2020";
    final static String MEDIUM_FIRST_DAY_FR = "1er déc. 2020";
    final static String MEDIUM_SECOND_DAY_FR = "11 déc. 2020";
    final static String LONG_FIRST_DAY_FR = "1er décembre 2020";
    final static String LONG_SECOND_DAY_FR = "11 décembre 2020";
    final static String MONTH_IT = "dicembre";

    final static Locale LOCALE_IT_CH = new Locale("it", "ch");
    final static Locale LOCALE_FR_CH = new Locale("fr", "ch");

    // test variables
    final Date dateFirst = Date.from(localDateFirst.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    final Date dateSecond = Date.from(localDateSecond.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

    // italian
    @Test
    public void testMediumItDateFirst() {
        Assert.assertEquals(MEDIUM_FIRST_DAY_IT,
                (new DateFormatter(DateRepresentation.MEDIUM, LOCALE_IT_CH)).format(dateFirst));
    }

    @Test
    public void testMediumItDateSecond() {
        Assert.assertEquals(MEDIUM_SECOND_DAY_IT,
                (new DateFormatter(DateRepresentation.MEDIUM, LOCALE_IT_CH)).format(dateSecond));
    }

    @Test
    public void testLongItDateFirst() {
        Assert.assertEquals(LONG_FIRST_DAY_IT,
                (new DateFormatter(DateRepresentation.LONG, LOCALE_IT_CH)).format(dateFirst));
    }

    @Test
    public void testLongItDateSecond() {
        Assert.assertEquals(LONG_SECOND_DAY_IT,
                (new DateFormatter(DateRepresentation.LONG, LOCALE_IT_CH)).format(dateSecond));
    }

    // french
    @Test
    public void testMediumFrDateFirst() {
        Assert.assertEquals(MEDIUM_FIRST_DAY_FR,
                (new DateFormatter(DateRepresentation.MEDIUM, LOCALE_FR_CH)).format(dateFirst));
    }

    @Test
    public void testMediumFrDateSecond() {
        Assert.assertEquals(MEDIUM_SECOND_DAY_FR,
                (new DateFormatter(DateRepresentation.MEDIUM, LOCALE_FR_CH)).format(dateSecond));
    }

    @Test
    public void testLongFrDateFirst() {
        Assert.assertEquals(LONG_FIRST_DAY_FR,
                (new DateFormatter(DateRepresentation.LONG, LOCALE_FR_CH)).format(dateFirst));
    }

    @Test
    public void testLongFrDateSecond() {
        Assert.assertEquals(LONG_SECOND_DAY_FR,
                (new DateFormatter(DateRepresentation.LONG, LOCALE_FR_CH)).format(dateSecond));
    }

    @Test
    public void testMonthIt(){
        Assert.assertEquals(MONTH_IT,
                (new DateFormatter(DateRepresentation.MONTH, LOCALE_IT_CH)).format(dateFirst));
    }
}