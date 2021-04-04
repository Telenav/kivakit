////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.vm;

import com.telenav.kivakit.core.kernel.language.vm.OperatingSystem;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

public class OperatingSystemTest
{
    @Test
    public void test()
    {
        final var os = OperatingSystem.get();
        ensure(os.isMac() || os.isUnix() || os.isWindows());
    }
}
