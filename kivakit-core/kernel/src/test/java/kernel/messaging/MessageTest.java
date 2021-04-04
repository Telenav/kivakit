////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.messaging;

import com.telenav.kivakit.core.kernel.messaging.messages.status.Warning;
import org.junit.Assert;
import org.junit.Test;

public class MessageTest
{
    @Test
    public void test()
    {
        Assert.assertEquals(new Warning("${debug}", 5).toString(), "5");
        Assert.assertEquals(new Warning(" ${debug}", 5).toString(), " 5");
        Assert.assertEquals(new Warning("${debug} ", 5).toString(), "5 ");
        Assert.assertEquals(new Warning(" ${debug} ", 5).toString(), " 5 ");
        Assert.assertEquals(new Warning(" ${debug} ${debug} ", 5, 7).toString(), " 5 7 ");
    }
}
