////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.messaging;

import com.telenav.kivakit.core.kernel.language.values.count.MutableCount;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class RepeaterTest
{
    @Test
    public void test()
    {
        final Repeater repeater = new BaseRepeater(getClass());
        final var count = new MutableCount();
        repeater.addListener(message -> count.increment());
        ensureEqual(0L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(1L, count.asLong());
        repeater.receive(new Information("Test"));
        ensureEqual(2L, count.asLong());
    }
}
