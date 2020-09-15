package ch.less.infrastructure.xps.conversion.saxon;

import ch.less.infrastructure.xps.conversion.documents.IPrintObject;
import junit.framework.TestCase;
import net.sf.saxon.trans.XPathException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

/**
 * test cases for proprietary URI Resolver functionality
 */
@RunWith(SpringRunner.class)
public class XpsURIResolverTest extends TestCase {

    // inner class to mock print object
    private class MockPrintObject implements IPrintObject {

        private String locale;

        public void setLocale(String localeString) {
            this.locale = localeString;
        }

        @Override
        public String getLocale() {
            return locale;
        }

        @Override
        public String getLanguage() {
            return null;
        }

        @Override
        public Source getTemplate() {
            return null;
        }

        @Override
        public Source getXmlData() {
            return null;
        }
    };

    // no magic numbers locales - according to legacy
    private static String LOCALE_DE = "de_CH";
    private static String LOCALE_IT = "it_CH";
    private static String LOCALE_FR = "fr_CH";
    private static String LOCALE_EN = "en_US";

    final static String ERROR_TEXT = "error"; // error representatio

    // no magic numbers date in- and outputs
    final static String FIRST_DAY_SHORT_TEXT = "date/short/2020-12-01";
    final static String FIRST_DAY_SHORT_DE = "01.12.20";
    final static String FIRST_DAY_SHORT_IT = "01.12.20";
    final static String FIRST_DAY_SHORT_FR = "01.12.20";
    final static String FIRST_DAY_SHORT_EN = "12/1/20";

    final static String SECOND_DAY_SHORT_TEXT = "date/short/2020-12-11";
    final static String SECOND_DAY_SHORT_DE = "11.12.20";
    final static String SECOND_DAY_SHORT_IT = "11.12.20";
    final static String SECOND_DAY_SHORT_FR = "11.12.20";
    final static String SECOND_DAY_SHORT_EN = "12/11/20";

    final static String FIRST_DAY_MEDIUM_TEXT = "date/medium/2020-12-01";
    final static String FIRST_DAY_MEDIUM_DE = "01.12.2020";
    final static String FIRST_DAY_MEDIUM_IT = "1° dic 2020";
    final static String FIRST_DAY_MEDIUM_FR = "1er déc. 2020";
    final static String FIRST_DAY_MEDIUM_EN = "Dec 1, 2020";

    final static String SECOND_DAY_MEDIUM_TEXT = "date/medium/2020-12-11";
    final static String SECOND_DAY_MEDIUM_DE = "11.12.2020";
    final static String SECOND_DAY_MEDIUM_IT = "11 dic 2020";
    final static String SECOND_DAY_MEDIUM_FR = "11 déc. 2020";
    final static String SECOND_DAY_MEDIUM_EN = "Dec 11, 2020";

    final static String FIRST_DAY_LONG_TEXT = "date/long/2020-12-01";
    final static String FIRST_DAY_LONG_DE = "1. Dezember 2020";
    final static String FIRST_DAY_LONG_IT = "1° dicembre 2020";
    final static String FIRST_DAY_LONG_FR = "1er décembre 2020";
    final static String FIRST_DAY_LONG_EN = "December 1, 2020";

    final static String SECOND_DAY_LONG_TEXT = "date/long/2020-12-11";
    final static String SECOND_DAY_LONG_DE = "11. Dezember 2020";
    final static String SECOND_DAY_LONG_IT = "11 dicembre 2020";
    final static String SECOND_DAY_LONG_FR = "11 décembre 2020";
    final static String SECOND_DAY_LONG_EN = "December 11, 2020";

    final static String MONTH_TEXT = "date/month/2020-12-01";
    final static String MONTH_DE = "Dezember";
    final static String MONTH_IT = "dicembre";
    final static String MONTH_FR = "décembre";
    final static String MONTH_EN = "December";

    final static String MEDIUM_MONTH_TEXT = "date/medium_month/2020-12-01";
    final static String MEDIUM_MONTH_DE = "Dez";
    final static String MEDIUM_MONTH_IT = "dic";
    final static String MEDIUM_MONTH_FR = "déc.";
    final static String MEDIUM_MONTH_EN = "Dec";

    // test mocks and workers
    private final MockPrintObject printObject = new MockPrintObject();
    private final XpsURIResolver resolver = new XpsURIResolver(printObject);


    // german
    @Test
    public void testShortDeDateFirst() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(FIRST_DAY_SHORT_DE, getValueFromResolving(FIRST_DAY_SHORT_TEXT));
    }

    @Test
    public void testShortDeDateSecond() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(SECOND_DAY_SHORT_DE, getValueFromResolving(SECOND_DAY_SHORT_TEXT));
    }

    @Test
    public void testMediumDeDateFirst() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(FIRST_DAY_MEDIUM_DE, getValueFromResolving(FIRST_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testMediumDeDateSecond() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(SECOND_DAY_MEDIUM_DE, getValueFromResolving(SECOND_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testLongDeDateFirst() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(FIRST_DAY_LONG_DE, getValueFromResolving(FIRST_DAY_LONG_TEXT));
    }

    @Test
    public void testLongDeDateSecond() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(SECOND_DAY_LONG_DE, getValueFromResolving(SECOND_DAY_LONG_TEXT));
    }

    @Test
    public void testMonthDe() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(MONTH_DE, getValueFromResolving(MONTH_TEXT));
    }

    @Test
    public void testMediumMonthDe() {
        printObject.setLocale(LOCALE_DE);
        Assert.assertEquals(MEDIUM_MONTH_DE, getValueFromResolving(MEDIUM_MONTH_TEXT));
    }

    // italian
    @Test
    public void testShortItDateFirst() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(FIRST_DAY_SHORT_IT, getValueFromResolving(FIRST_DAY_SHORT_TEXT));
    }

    @Test
    public void testShortItDateSecond() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(SECOND_DAY_SHORT_IT, getValueFromResolving(SECOND_DAY_SHORT_TEXT));
    }

    @Test
    public void testMediumItDateFirst() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(FIRST_DAY_MEDIUM_IT, getValueFromResolving(FIRST_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testMediumItDateSecond() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(SECOND_DAY_MEDIUM_IT, getValueFromResolving(SECOND_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testLongItDateFirst() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(FIRST_DAY_LONG_IT, getValueFromResolving(FIRST_DAY_LONG_TEXT));
    }

    @Test
    public void testLongItDateSecond() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(SECOND_DAY_LONG_IT, getValueFromResolving(SECOND_DAY_LONG_TEXT));
    }

    @Test
    public void testMonthIt() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(MONTH_IT, getValueFromResolving(MONTH_TEXT));
    }

    @Test
    public void testMediumMonthIt() {
        printObject.setLocale(LOCALE_IT);
        Assert.assertEquals(MEDIUM_MONTH_IT, getValueFromResolving(MEDIUM_MONTH_TEXT));
    }

    // french
    @Test
    public void testShortFrDateFirst() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(FIRST_DAY_SHORT_FR, getValueFromResolving(FIRST_DAY_SHORT_TEXT));
    }

    @Test
    public void testShortFrDateSecond() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(SECOND_DAY_SHORT_FR, getValueFromResolving(SECOND_DAY_SHORT_TEXT));
    }

    @Test
    public void testMediumFrDateFirst() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(FIRST_DAY_MEDIUM_FR, getValueFromResolving(FIRST_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testMediumFrDateSecond() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(SECOND_DAY_MEDIUM_FR, getValueFromResolving(SECOND_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testLongFrDateFirst() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(FIRST_DAY_LONG_FR, getValueFromResolving(FIRST_DAY_LONG_TEXT));
    }

    @Test
    public void testLongFrDateSecond() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(SECOND_DAY_LONG_FR, getValueFromResolving(SECOND_DAY_LONG_TEXT));
    }

    @Test
    public void testMonthFr() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(MONTH_FR, getValueFromResolving(MONTH_TEXT));
    }

    @Test
    public void testMediumMonthFr() {
        printObject.setLocale(LOCALE_FR);
        Assert.assertEquals(MEDIUM_MONTH_FR, getValueFromResolving(MEDIUM_MONTH_TEXT));
    }

    // english
    @Test
    public void testShortEnDateFirst() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(FIRST_DAY_SHORT_EN, getValueFromResolving(FIRST_DAY_SHORT_TEXT));
    }

    @Test
    public void testShortEnDateSecond() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(SECOND_DAY_SHORT_EN, getValueFromResolving(SECOND_DAY_SHORT_TEXT));
    }

    @Test
    public void testMediumEnDateFirst() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(FIRST_DAY_MEDIUM_EN, getValueFromResolving(FIRST_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testMediumEnDateSecond() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(SECOND_DAY_MEDIUM_EN, getValueFromResolving(SECOND_DAY_MEDIUM_TEXT));
    }

    @Test
    public void testLongEnDateFirst() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(FIRST_DAY_LONG_EN, getValueFromResolving(FIRST_DAY_LONG_TEXT));
    }

    @Test
    public void testLongEnDateSecond() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(SECOND_DAY_LONG_EN, getValueFromResolving(SECOND_DAY_LONG_TEXT));
    }

    @Test
    public void testMonthEn() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(MONTH_EN, getValueFromResolving(MONTH_TEXT));
    }

    @Test
    public void testMediumMonthEn() {
        printObject.setLocale(LOCALE_EN);
        Assert.assertEquals(MEDIUM_MONTH_EN, getValueFromResolving(MEDIUM_MONTH_TEXT));
    }

    // helping method to resolve DOM fast and easy-to-read
    private String getValueFromResolving(String href) {
        try {
            final DOMSource source = (DOMSource) resolver.resolve(href, "");
            return source.getNode().getChildNodes().item(0).getTextContent();
        } catch (XPathException e) {
            return ERROR_TEXT; // good enough for error detection
        }
    }

}