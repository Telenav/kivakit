////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.messaging;

import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.core.kernel.messaging.broadcasters.Multicaster;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class BroadcasterTest
{
    @Test
    public void test()
    {
        final var broadcaster = new Multicaster("test", getClass());
        final var count = new MutableCount();
        broadcaster.addListener(message -> count.increment());
        ensureEqual(0L, count.asLong());
        broadcaster.information("Test");
        ensureEqual(1L, count.asLong());
        broadcaster.information("Test");
        ensureEqual(2L, count.asLong());
    }
}
