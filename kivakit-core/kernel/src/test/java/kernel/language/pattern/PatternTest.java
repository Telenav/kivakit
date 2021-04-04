////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.pattern;

import com.telenav.kivakit.core.kernel.language.patterns.Pattern;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureFalse;

public class PatternTest
{
    @Test
    public void testFloatingPoint()
    {
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("99"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("99.0"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("0.9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("-.9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("-9"));
        ensure(Pattern.FLOATING_POINT_NUMBER.matches("-45.0"));
        ensureFalse(Pattern.FLOATING_POINT_NUMBER.matches("-9.45.0"));
        ensureFalse(Pattern.FLOATING_POINT_NUMBER.matches("+9.0"));
    }
}
