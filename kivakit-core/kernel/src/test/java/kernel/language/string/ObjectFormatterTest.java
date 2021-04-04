////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.string;

import com.telenav.kivakit.core.kernel.language.reflection.property.filters.KivaKitIncludeProperty;
import com.telenav.kivakit.core.kernel.language.strings.formatting.ObjectFormatter;
import org.junit.Assert;
import org.junit.Test;

public class ObjectFormatterTest
{
    public static class Bean
    {
        @KivaKitIncludeProperty
        final int y = 7;

        public int getY()
        {
            return y;
        }

        @Override
        public String toString()
        {
            return new ObjectFormatter(this).toString();
        }
    }

    final Bean bean = new Bean();

    @KivaKitIncludeProperty
    public Bean bean()
    {
        return bean;
    }

    @Test
    public void test()
    {
        final var beanString = "[ObjectFormatterTest.Bean y = 7]";
        Assert.assertEquals(beanString, new ObjectFormatter(bean).toString());
        Assert.assertEquals("[ObjectFormatterTest bean = " + beanString + "]", new ObjectFormatter(this).toString());
    }
}
