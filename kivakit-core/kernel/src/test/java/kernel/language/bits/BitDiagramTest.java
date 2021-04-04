////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package kernel.language.bits;

import com.telenav.kivakit.core.kernel.language.bits.BitDiagram;
import com.telenav.kivakit.core.kernel.language.bits.BitDiagram.BitField;
import org.junit.Test;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensureEqual;

public class BitDiagramTest
{
    private final BitDiagram DIAGRAM = new BitDiagram("AAAABBBBC");

    private final BitField A = DIAGRAM.field('A');

    private final BitField B = DIAGRAM.field('B');

    private final BitField C = DIAGRAM.field('C');

    @Test
    public void testExtract()
    {
        final var value = binary("100110011");

        ensureEqual(9, A.extractInt(value));
        ensureEqual(9, B.extractInt(value));
        ensureEqual(true, C.extractBoolean(value));
    }

    @Test
    public void testSet()
    {
        final var value = binary("000010010");
        final var field = binary("0110");
        final var result = binary("000001100");

        ensureEqual(result, B.set(value, field));
    }

    private int binary(final String value)
    {
        return Integer.parseInt(value, 2);
    }
}
