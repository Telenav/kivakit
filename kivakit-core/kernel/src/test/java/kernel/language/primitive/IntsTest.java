////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.primitive;

import com.telenav.kivakit.core.kernel.language.primitives.Ints;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

/**
 * @author jonathanl (shibo)
 */
public class IntsTest
{
    @Test
    public void testParse()
    {
        for (int i = -100_000; i < 100_000; i++)
        {
            ensureEqual(Ints.parse("" + i, -1), i);
        }
        ensureEqual(Ints.parse(null, -1), -1);
        ensureEqual(Ints.parse("", -1), -1);
        ensureEqual(Ints.parse("99", -1), 99);
        ensureEqual(Ints.parse("99", -1), 99);
        ensureEqual(Ints.parse("abc", -1), -1);

        ensureEqual(Ints.parseNaturalNumber(null), Ints.INVALID);
        ensureEqual(Ints.parseNaturalNumber(""), Ints.INVALID);
        ensureEqual(Ints.parseNaturalNumber("123"), 123);
        ensureEqual(Ints.parseNaturalNumber("-123"), Ints.INVALID);
    }
}
