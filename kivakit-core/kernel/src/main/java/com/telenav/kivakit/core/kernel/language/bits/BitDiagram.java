////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.bits;

import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageBits;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * A {@link BitDiagram} is a character string that visually depicts one or more bit fields of a primitive value such as
 * an int or a long. Given a {@link BitDiagram}, each depicted field can be retrieved through a {@link BitField}
 * accessor object by calling {@link #field(char)}.
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * private static BitDiagram COLOR = new BitDiagram(
 *     &quot;AAAAAAAA RRRRRRRR GGGGGGGG BBBBBBBB&quot;);
 *
 * private static BitField ALPHA = COLOR.field('A');
 * private static BitField RED = COLOR.field('R');
 * private static BitField GREEN = COLOR.field('G');
 * private static BitField BLUE = COLOR.field('B');
 *
 * int rgb = 0xff00ff;
 *
 * System.out.println(&quot;Red = &quot; + RED.extractInt(rgb));
 * System.out.println(&quot;Green = &quot; + GREEN.extractInt(rgb));
 * System.out.println(&quot;Blue = &quot; + BLUE.extractInt(rgb));
 * </pre>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageBits.class)
public class BitDiagram
{
    /**
     * The bit field for a given character in a bit diagram
     */
    public static class BitField
    {
        /** The mask to access the bit field */
        private long mask;

        /** The shift to access the bit field */
        private int shift;

        /** The character in the bit diagram for this bit field */
        private final char diagramCharacter;

        /**
         * @param diagramCharacter The character for this bit field in the parent bit diagram
         */
        public BitField(final char diagramCharacter)
        {
            this.diagramCharacter = diagramCharacter;
        }

        /**
         * @return The width of this bit field
         */
        public int bits()
        {
            return Long.bitCount(mask());
        }

        /**
         * @return A boolean value for this bitfield extracted from the given value
         */
        public boolean extractBoolean(final long value)
        {
            return extractInt(value) == 1;
        }

        /**
         * @return A byte for this bitfield extracted from the given value
         */
        public byte extractByte(final long value)
        {
            return (byte) extractLong(value);
        }

        /**
         * @return An int value for this bitfield extracted from the given value
         */
        public int extractInt(final long value)
        {
            return (int) extractLong(value);
        }

        /**
         * @return A long value for this bitfield extracted from the given value
         */
        public long extractLong(final long value)
        {
            return (value & mask) >>> shift;
        }

        /**
         * @return A short value for this bitfield extracted from the given value
         */
        public short extractShort(final long value)
        {
            return (short) extractLong(value);
        }

        /**
         * @return The mask for this bitfield
         */
        public long mask()
        {
            return mask;
        }

        /**
         * @return The larges value that can be contained in this bitfield
         */
        public int maximumValue()
        {
            return 1 << bits();
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public int set(final int value, final boolean source)
        {
            return set(value, source ? 1 : 0);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public int set(final int value, final int source)
        {
            return (value & ~(int) mask) | (source << shift);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public long set(final long value, final boolean source)
        {
            return set(value, source ? 1 : 0);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public long set(final long value, final long source)
        {
            return (value & ~mask) | (source << shift);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public short set(final short value, final boolean source)
        {
            return set(value, (short) (source ? 1 : 0));
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public short set(final short value, final short source)
        {
            return (short) ((value & ~(short) mask) | (source << shift));
        }

        /**
         * @return The character that this bitfield uses in bit diagrams
         */
        @Override
        public String toString()
        {
            return String.valueOf(diagramCharacter);
        }
    }

    /**
     * The bit diagram as a string
     */
    private final String diagram;

    /**
     * @param diagram The bit diagram
     */
    public BitDiagram(final String diagram)
    {
        // Remove whitespace from diagram
        this.diagram = diagram.replaceAll(" ", "");

        // We can't allow 0 or 1 in a bit diagram because we're going to use those when converting
        // to binary when we create the field below
        if (diagram.contains("1") || diagram.contains("0"))
        {
            Ensure.fail("0 and 1 are not valid bit diagram characters");
        }
    }

    /**
     * @return The {@link BitField} from this bit diagram for the given bit field character. For example, in a bit
     * diagram of "AAA BBB", <i>field("A")</i> would return a bit field accessor for the top three bits, the "A"
     * bitfield.
     */
    public BitField field(final char fieldCharacter)
    {
        final var field = new BitField(fieldCharacter);
        final var bits = diagram.replaceAll("" + fieldCharacter, "1").replaceAll("[^1]", "0");
        field.mask = parseBits(bits);
        for (var shift = 0; shift < 64; shift++)
        {
            final var index = bits.length() - shift - 1;
            if (index < 0)
            {
                break;
            }
            if (bits.charAt(index) == '1')
            {
                field.shift = shift;
                return field;
            }
        }
        return Ensure.fail("Invalid bitfield or diagram");
    }

    @Override
    public String toString()
    {
        final var result = new StringBuilder();
        for (var i = 0; i < diagram.length(); i++)
        {
            result.append(diagram.charAt(i));
            if ((i + 1) % 8 == 0 && i + 1 < diagram.length())
            {
                result.append(" ");
            }
        }
        return result.toString();
    }

    private long parseBits(final String string)
    {
        var value = 0L;
        for (var i = 0; i < string.length(); i++)
        {
            value <<= 1;
            switch (string.charAt(i))
            {
                case '1':
                    value |= 1;
                    break;

                case '0':
                    break;

                default:
                    Ensure.fail(string + " is not a binary number");
                    return -1;
            }
        }
        return value;
    }
}
