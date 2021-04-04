////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.primitive;

import com.telenav.kivakit.core.kernel.language.primitives.Booleans;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

public class BooleansTest
{
    @Test
    public void test()
    {
        ensure(Booleans.isTrue("true"));
        ensure(Booleans.isTrue("yes"));
        ensure(Booleans.isTrue("enabled"));
        ensure(Booleans.isTrue("True"));
        ensure(Booleans.isTrue("Yes"));
        ensure(Booleans.isTrue("Enabled"));
        ensureFalse(Booleans.isTrue("1"));
        ensureFalse(Booleans.isTrue(""));
        ensureFalse(Booleans.isTrue(null));
    }
}
