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

package com.telenav.kivakit.core.time;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.language.primitive.Ints.intIsBetweenInclusive;

/**
 * Anti-meridiem (AM) or post-meridiem (PM) in the English system of keeping time, or military time for the rest of the
 * world.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
@UmlClassDiagram(diagram = DiagramTime.class)
@CodeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public enum Meridiem
{
    NO_MERIDIEM,

    /** Anti-meridiem */
    AM,

    /** Post-meridiem */
    PM;

    /**
     * Returns the meridiem hour for the given military hour
     */
    public static int asMeridiemHour(int militaryHour)
    {
        ensure(intIsBetweenInclusive(militaryHour, 0, 23));

        if (militaryHour == 0 || militaryHour == 12)
        {
            return 12;
        }

        return militaryHour < 12
                ? militaryHour
                : militaryHour - 12;
    }

    /**
     * Returns the Meridiem for the given military (0-23) hour.
     */
    public static Meridiem meridiem(int militaryHour)
    {
        ensure(intIsBetweenInclusive(militaryHour, 0, 23));

        return militaryHour < 12 ? AM : PM;
    }

    /**
     * Returns the given meridiem hour as a military hour
     */
    public int asMilitaryHour(int meridiemHour)
    {
        ensure(intIsBetweenInclusive(meridiemHour, 1, 12), "Invalid meridiem hour: $", meridiemHour);

        switch (this)
        {
            case PM:
                if (meridiemHour == 12)
                {
                    return 12;
                }
                return meridiemHour + 12;

            case AM:
                if (meridiemHour == 12)
                {
                    return 0;
                }
                return meridiemHour;

            case NO_MERIDIEM:
                return meridiemHour;

            default:
                return unsupported();
        }
    }
}
