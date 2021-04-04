////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.scalars;

import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class BytesTest
{
    @Test
    public void test()
    {
        ensureEqual(0L, Bytes._0.asBytes());
        ensureEqual(10L, Bytes.bytes(10).asBytes());
        ensureEqual(Bytes.kilobytes(1), Bytes.bytes(1024));
        ensureEqual(Bytes.megabytes(1), Bytes.bytes(1024 * 1024));
        ensureEqual(Bytes.gigabytes(1), Bytes.bytes(1024 * 1024 * 1024));
        ensureEqual(Bytes.terabytes(1), Bytes.bytes(1024L * 1024L * 1024L * 1024L));
        ensureEqual(Bytes.kilobytes(0.5), Bytes.bytes(1024 / 2));
        ensureEqual(Bytes.megabytes(0.5), Bytes.bytes((1024 * 1024) / 2));
        ensureEqual(Bytes.gigabytes(0.5), Bytes.bytes((1024 * 1024 * 1024) / 2));
        ensureEqual(Bytes.terabytes(0.5), Bytes.bytes((1024L * 1024L * 1024L * 1024L) / 2));
        ensureEqual(Bytes.bytes(0), Bytes.kilobytes(1).percent(Percent._0));
        ensureEqual(Bytes.bytes(512), Bytes.kilobytes(1).percent(Percent._50));
        ensureEqual(Bytes.bytes(1024), Bytes.kilobytes(1).percent(Percent._100));
        ensureEqual(1024L, Bytes.kilobytes(1).asBytes());
        ensureEqual(1024.0, Bytes.megabytes(1).asKilobytes());
        ensureEqual(1024.0, Bytes.gigabytes(1).asMegabytes());
        ensureEqual(1024.0, Bytes.terabytes(1).asGigabytes());
    }
}
