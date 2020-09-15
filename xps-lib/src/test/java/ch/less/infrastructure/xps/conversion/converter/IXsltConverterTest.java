package ch.less.infrastructure.xps.conversion.converter;

import static org.mockito.Mockito.*;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * System tests to make sure, the interface is not being touched (as it's used by different applications)
 */
@RunWith(MockitoJUnitRunner.class)
public class IXsltConverterTest extends TestCase{

    @Mock
    private IXsltConverter converter;

    // make sure the interface works the way it does
    @Test
    public void testConversionInterface(){
        when(converter.convertXml(any(String.class), any(String.class), any(String.class))).thenReturn(new byte[0]);
        Assert.assertEquals(converter.convertXml("", "", "").length, 0);
    }


}