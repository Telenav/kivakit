////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.messaging;

import com.telenav.kivakit.core.kernel.messaging.messages.Severity;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

public class SeverityTest
{
    @Test
    public void test()
    {
        ensure(Severity.CRITICAL.isGreaterThan(Severity.HIGH));
        ensure(Severity.HIGH.isGreaterThan(Severity.MEDIUM_HIGH));
        ensure(Severity.MEDIUM_HIGH.isGreaterThan(Severity.MEDIUM));
        ensure(Severity.MEDIUM.isGreaterThan(Severity.LOW));
        ensure(Severity.LOW.isGreaterThan(Severity.NONE));

        ensure(Severity.HIGH.isLessThan(Severity.CRITICAL));
        ensure(Severity.MEDIUM_HIGH.isLessThan(Severity.HIGH));
        ensure(Severity.MEDIUM.isLessThan(Severity.MEDIUM_HIGH));
        ensure(Severity.LOW.isLessThan(Severity.MEDIUM));
        ensure(Severity.NONE.isLessThan(Severity.LOW));
    }
}
