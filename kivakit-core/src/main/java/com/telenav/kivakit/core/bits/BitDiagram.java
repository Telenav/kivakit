////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.bits;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramBits;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_INSUFFICIENT;

/**
 * A {@link BitDiagram} is a character string that visually depicts one or more bit fields of a primitive value such as
 * an int or a long. Given a {@link BitDiagram}, each depicted field can be retrieved through a {@link BitField}
 * accessor object by calling {@link #field(char)}. For bit positions that are not associated with any bit field, the
 * character '?' can be used.
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 * // <b>Define a BitDiagram for color values specified as Alpha, Red, Green, and Blue.</b>
 * BitDiagram COLOR = new BitDiagram(&quot;AAAAAAAA RRRRRRRR GGGGGGGG BBBBBBBB&quot;);
 *
 * // <b>Define a BitField for each field in the bit diagram</b>
 * BitField ALPHA = COLOR.field('A');
 * BitField RED = COLOR.field('R');
 * BitField GREEN = COLOR.field('G');
 * BitField BLUE = COLOR.field('B');
 *
 * // <b>Start with an initial <i>int</i> ARGB value</b>
 * int rgb = 0xffff80ff;
 *
 * // <b>Show ALPHA, RED, GREEN, and BLUE values from the <i>int</i> variable <i>rgb</i></b>
 * System.out.println(&quot;Alpha = &quot; + ALPHA.getInt(rgb));   // 255
 * System.out.println(&quot;Red = &quot; + RED.getInt(rgb));       // 255
 * System.out.println(&quot;Green = &quot; + GREEN.getInt(rgb));   // 128
 * System.out.println(&quot;Blue = &quot; + BLUE.getInt(rgb));     // 255
 *
 * // <b>Set new ALPHA, RED, GREEN, and BLUE values into the <i>int</i> variable <i>rgb</i></b>
 * rgb = ALPHA.setInt(rgb, 0x80);
 * rgb = RED.setInt(rgb, 0x80);
 * rgb = GREEN.setInt(rgb, 0x80);
 * rgb = BLUE.setInt(rgb, 0x80);
 *
 * // <b>Show ALPHA, RED, GREEN, and BLUE values from the <i>int</i> variable <i>rgb</i></b>
 * System.out.println(&quot;Alpha = &quot; + ALPHA.getInt(rgb));   // 128
 * System.out.println(&quot;Red = &quot; + RED.getInt(rgb));       // 128
 * System.out.println(&quot;Green = &quot; + GREEN.getInt(rgb));   // 128
 * System.out.println(&quot;Blue = &quot; + BLUE.getInt(rgb));     // 128 </pre>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramBits.class)
@CodeQuality(stability = CODE_STABLE,
             testing = TESTING_INSUFFICIENT,
             documentation = DOCUMENTATION_COMPLETE)
public class BitDiagram
{
    /**
     * The bit field for a given character in a bit diagram
     */
    @SuppressWarnings("unused")
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
        public BitField(char diagramCharacter)
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
        public boolean getBoolean(long value)
        {
            return getInt(value) == 1;
        }

        /**
         * @return A byte for this bitfield extracted from the given value
         */
        public byte getByte(long value)
        {
            return (byte) getLong(value);
        }

        /**
         * @return An int value for this bitfield extracted from the given value
         */
        public int getInt(long value)
        {
            return (int) getLong(value);
        }

        /**
         * @return A long value for this bitfield extracted from the given value
         */
        public long getLong(long value)
        {
            return (value & mask) >>> shift;
        }

        /**
         * @return A short value for this bitfield extracted from the given value
         */
        public short getShort(long value)
        {
            return (short) getLong(value);
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
        public int set(int value, boolean source)
        {
            return set(value, source ? 1 : 0);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public int set(int value, int source)
        {
            return (value & ~(int) mask) | (source << shift);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public long set(long value, boolean source)
        {
            return set(value, source ? 1 : 0);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public long set(long value, long source)
        {
            return (value & ~mask) | (source << shift);
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public short set(short value, boolean source)
        {
            return set(value, (short) (source ? 1 : 0));
        }

        /**
         * @return The given value with this bit field set to the given source value
         */
        public short set(short value, short source)
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
    public BitDiagram(String diagram)
    {
        // Remove whitespace from diagram
        this.diagram = diagram.replaceAll(" ", "");

        // We can't allow 0 or 1 in a bit diagram because we're going to use those when converting
        // to binary when we create the field below
        if (diagram.contains("1") || diagram.contains("0"))
        {
            throw new IllegalStateException("0 and 1 are not valid bit diagram characters");
        }
    }

    /**
     * @return The {@link BitField} from this bit diagram for the given bit field character. For example, in a bit
     * diagram of "AAA BBB", <i>field("A")</i> would return a bit field accessor for the top three bits, the "A"
     * bitfield.
     */
    public BitField field(char fieldCharacter)
    {
        var field = new BitField(fieldCharacter);
        var bits = diagram.replaceAll("" + fieldCharacter, "1").replaceAll("[^1]", "0");
        field.mask = parseBits(bits);
        for (var shift = 0; shift < 64; shift++)
        {
            var index = bits.length() - shift - 1;
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
        throw new IllegalStateException("Invalid bitfield or diagram");
    }

    @Override
    public String toString()
    {
        var result = new StringBuilder();
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

    private long parseBits(String string)
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
                    throw new IllegalStateException(string + " is not a binary number");
            }
        }
        return value;
    }
}
