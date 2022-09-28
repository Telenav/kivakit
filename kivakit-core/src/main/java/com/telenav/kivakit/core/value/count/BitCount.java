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

package com.telenav.kivakit.core.value.count;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramCount;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * A number of bits and various associated methods.
 * <p>
 * The largest value represented by this number of bits can be retrieved with {@link #maximumUnsigned()} or
 * {@link #maximumSigned()} and the smallest by {@link #minimumSigned()}. A mask for the given number of bits (where a
 * {@link BitCount} of 3 would return the mask 111) is given by {@link #mask()}. Various mathematical operations and
 * conversions can be performed on bit counts, including all such methods in the superclass {@link Count}.
 *
 * @author jonathanl
 * @see Count
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramCount.class)
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class BitCount extends BaseCount<BitCount>
{
    public static final BitCount _0 = new BitCount(0);

    public static final BitCount _1 = new BitCount(1);

    public static final BitCount _2 = new BitCount(2);

    public static final BitCount _3 = new BitCount(3);

    public static final BitCount _4 = new BitCount(4);

    public static final BitCount _5 = new BitCount(5);

    public static final BitCount _6 = new BitCount(6);

    public static final BitCount _7 = new BitCount(7);

    public static final BitCount _8 = new BitCount(8);

    public static final BitCount _9 = new BitCount(9);

    public static final BitCount _10 = new BitCount(10);

    public static final BitCount _11 = new BitCount(11);

    public static final BitCount _12 = new BitCount(12);

    public static final BitCount _13 = new BitCount(13);

    public static final BitCount _14 = new BitCount(14);

    public static final BitCount _15 = new BitCount(15);

    public static final BitCount _16 = new BitCount(16);

    public static final BitCount _17 = new BitCount(17);

    public static final BitCount _18 = new BitCount(18);

    public static final BitCount _19 = new BitCount(19);

    public static final BitCount _20 = new BitCount(20);

    public static final BitCount _21 = new BitCount(21);

    public static final BitCount _22 = new BitCount(22);

    public static final BitCount _23 = new BitCount(23);

    public static final BitCount _24 = new BitCount(24);

    public static final BitCount _25 = new BitCount(25);

    public static final BitCount _26 = new BitCount(26);

    public static final BitCount _27 = new BitCount(27);

    public static final BitCount _28 = new BitCount(28);

    public static final BitCount _29 = new BitCount(29);

    public static final BitCount _30 = new BitCount(30);

    public static final BitCount _31 = new BitCount(31);

    public static final BitCount _32 = new BitCount(32);

    public static final BitCount _33 = new BitCount(33);

    public static final BitCount _34 = new BitCount(34);

    public static final BitCount _35 = new BitCount(35);

    public static final BitCount _36 = new BitCount(36);

    public static final BitCount _37 = new BitCount(37);

    public static final BitCount _38 = new BitCount(38);

    public static final BitCount _39 = new BitCount(39);

    public static final BitCount _40 = new BitCount(40);

    public static final BitCount _41 = new BitCount(41);

    public static final BitCount _42 = new BitCount(42);

    public static final BitCount _43 = new BitCount(43);

    public static final BitCount _44 = new BitCount(44);

    public static final BitCount _45 = new BitCount(45);

    public static final BitCount _46 = new BitCount(46);

    public static final BitCount _47 = new BitCount(47);

    public static final BitCount _48 = new BitCount(48);

    public static final BitCount _49 = new BitCount(49);

    public static final BitCount _50 = new BitCount(50);

    public static final BitCount _51 = new BitCount(51);

    public static final BitCount _52 = new BitCount(52);

    public static final BitCount _53 = new BitCount(53);

    public static final BitCount _54 = new BitCount(54);

    public static final BitCount _55 = new BitCount(55);

    public static final BitCount _56 = new BitCount(56);

    public static final BitCount _57 = new BitCount(57);

    public static final BitCount _58 = new BitCount(58);

    public static final BitCount _59 = new BitCount(59);

    public static final BitCount _60 = new BitCount(60);

    public static final BitCount _61 = new BitCount(61);

    public static final BitCount _62 = new BitCount(62);

    public static final BitCount _63 = new BitCount(63);

    public static final BitCount _64 = new BitCount(64);

    public static final BitCount MAXIMUM = new BitCount(Long.MAX_VALUE);

    private static final int CACHE_SIZE = 64;

    private static BitCount[] cached;

    /**
     * Creates the given bit count
     */
    public static BitCount bits(long count)
    {
        assert count >= 0;
        assert count <= Integer.MAX_VALUE;

        if (count < CACHE_SIZE)
        {
            if (cached == null)
            {
                var values = new BitCount[CACHE_SIZE];
                for (var i = 0; i < CACHE_SIZE; i++)
                {
                    values[i] = new BitCount(i);
                }
                cached = values;
            }
            return cached[(int) count];
        }
        return new BitCount(count);
    }

    /**
     * Returns the number of bits per byte
     */
    public static BitCount bitsPerByte()
    {
        return bits(Byte.SIZE);
    }

    /**
     * Returns the number of bits per character
     */
    public static BitCount bitsPerChar()
    {
        return bits(Character.SIZE);
    }

    /**
     * Returns the number of bits per character
     */
    public static BitCount bitsPerInt()
    {
        return bits(Integer.SIZE);
    }

    /**
     * Returns the number of bits per long value
     */
    public static BitCount bitsPerLong()
    {
        return bits(Long.SIZE);
    }

    /**
     * Returns the number of bits per short value
     */
    public static BitCount bitsPerShort()
    {
        return bits(Short.SIZE);
    }

    /**
     * Returns the number of bits required to represent the given value
     */
    public static BitCount bitsToRepresent(long value)
    {
        return BitCount.bits(Long.SIZE - Long.numberOfLeadingZeros(value)).maximize(BitCount._1);
    }

    /**
     * Returns the number of bits required to represent the given value
     */
    public static BitCount bitsToRepresent(int value)
    {
        return BitCount.bits(Integer.SIZE - Integer.numberOfLeadingZeros(value));
    }

    protected BitCount(long bits)
    {
        super(bits);
    }

    protected BitCount()
    {
        super(0);
    }

    /**
     * @return A mask for the values this number of bits can take on
     */
    public long mask()
    {
        return values() - 1;
    }

    /**
     * Returns the maximum signed value with this number of bits
     */
    public long maximumSigned()
    {
        return asInt() == 64 ? Long.MAX_VALUE : (1L << (asInt() - 1)) - 1;
    }

    /**
     * Returns the maximum unsigned value with this number of bits
     */
    public long maximumUnsigned()
    {
        return asInt() == 64 ? Long.MAX_VALUE : (1L << asInt()) - 1;
    }

    /**
     * Returns the minimum signed value with this number of bits
     */
    public long minimumSigned()
    {
        return asInt() == 64 ? Long.MIN_VALUE : -maximumSigned() - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitCount onNewInstance(long count)
    {
        return bits(count);
    }

    /**
     * @return The number of values this bit count can take on (2^n)
     */
    public long values()
    {
        return 1L << get();
    }
}
