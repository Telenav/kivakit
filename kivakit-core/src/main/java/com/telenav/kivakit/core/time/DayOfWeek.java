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

import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.lexakai.DiagramTime;
import com.telenav.kivakit.core.value.count.BaseCount;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensureBetweenInclusive;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.ISO;
import static com.telenav.kivakit.core.time.DayOfWeek.Standard.JAVA;
import static com.telenav.kivakit.interfaces.string.Stringable.Format.USER_LABEL;

/**
 * Typesafe value for day of week. The value stored in the {@link BaseCount} superclass is the ISO day of the week
 * ordinal, where MONDAY is 0 and SUNDAY is 6. The Java day of the week (see java.time.{@link java.time.DayOfWeek}),
 * where MONDAY is 1 and SUNDAY is 7 also supported.
 *
 * @author jonathanl (shibo)
 * @author matthieun
 * @see java.time.DayOfWeek
 */
@UmlClassDiagram(diagram = DiagramTime.class)
@LexakaiJavadoc(complete = true)
public class DayOfWeek extends BaseCount<DayOfWeek>
{
    public static final DayOfWeek MONDAY = isoDayOfWeek(0);

    public static final DayOfWeek TUESDAY = isoDayOfWeek(1);

    public static final DayOfWeek WEDNESDAY = isoDayOfWeek(2);

    public static final DayOfWeek THURSDAY = isoDayOfWeek(3);

    public static final DayOfWeek FRIDAY = isoDayOfWeek(4);

    public static final DayOfWeek SATURDAY = isoDayOfWeek(5);

    public static final DayOfWeek SUNDAY = isoDayOfWeek(6);

    /**
     * Returns the day of the week under the given standard
     *
     * @param dayOfWeek The day of the week
     * @param standard The standard, either ISO or JAVA
     * @return The day of the week
     */
    public static DayOfWeek dayOfWeek(int dayOfWeek, Standard standard)
    {
        return new DayOfWeek(dayOfWeek, standard);
    }

    /**
     * The day of the week for an ISO ordinal value from 0 to 6
     *
     * @param dayOfWeek THe value from 0 to 6
     * @return The day of the week
     */
    public static DayOfWeek isoDayOfWeek(int dayOfWeek)
    {
        return dayOfWeek(dayOfWeek, ISO);
    }

    /**
     * The day of the week for Java ordinal value from 1 to 7
     *
     * @param day THe value from 0 to 6
     * @return The day of the week
     */
    public static DayOfWeek javaDayOfWeek(java.time.DayOfWeek day)
    {
        return dayOfWeek(day.getValue(), JAVA);
    }

    /**
     * The day of the week for Java ordinal value from 1 to 7
     *
     * @param dayOfWeek THe value from 0 to 6
     * @return The day of the week
     */
    public static DayOfWeek javaDayOfWeek(int dayOfWeek)
    {
        return dayOfWeek(dayOfWeek, ISO);
    }

    public enum Standard
    {
        ISO,
        JAVA
    }

    private final Standard standard;

    protected DayOfWeek(int dayOfWeek, Standard standard)
    {
        super(dayOfWeek);

        this.standard = standard;

        ensureBetweenInclusive(asIso(), 0, 6, "Invalid day of the week: " + this);
    }

    /**
     * @return This day of the week as an ISO-8601 ordinal value
     */
    public int asIso()
    {
        return isIso() ? asInt() : asInt() - 1;
    }

    public int asJava()
    {
        return isJava() ? asInt() : asInt() + 1;
    }

    public java.time.DayOfWeek asJavaDayOfWeek()
    {
        return java.time.DayOfWeek.of(asJava());
    }

    @Override
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public String asString(final Format format)
    {
        switch (format)
        {
            case DEBUG:
                return asJavaDayOfWeek().name() + " (" + standard + ")";

            default:
                return asJavaDayOfWeek().name();
        }
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof DayOfWeek)
        {
            DayOfWeek that = (DayOfWeek) object;
            return this.asIso() == that.asIso();
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.code(asIso());
    }

    public boolean isIso()
    {
        return standard == ISO;
    }

    public boolean isJava()
    {
        return standard == JAVA;
    }

    @Override
    public DayOfWeek maximum()
    {
        return SUNDAY;
    }

    @Override
    public DayOfWeek minimum()
    {
        return MONDAY;
    }

    @Override
    public DayOfWeek newInstance(long ordinal)
    {
        return dayOfWeek(asInt(), standard);
    }

    @Override
    public DayOfWeek newInstance(Long javaOrdinal)
    {
        return newInstance(javaOrdinal.longValue());
    }

    @Override
    public String toString()
    {
        return asString(USER_LABEL);
    }
}
