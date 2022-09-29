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

package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.TimeFormats;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class LocalTimeConverter extends BaseFormattedLocalTimeConverter
{
    /**
     * @param listener The listener to report problems to
     * @param zone The timezone
     */
    public LocalTimeConverter(Listener listener, ZoneId zone)
    {
        super(listener, TimeFormats.KIVAKIT_TIME, zone);
    }

    /**
     * @param listener The listener to report problems to
     */
    public LocalTimeConverter(Listener listener)
    {
        super(listener, TimeFormats.KIVAKIT_TIME);
    }

    /**
     * We don't append timezones to kivakit time format
     */
    @Override
    protected boolean appendTimeZone()
    {
        return false;
    }

    @Override
    protected String onToString(LocalTime value)
    {
        return DateTimeFormatter.ofPattern("h.mma").format(value.asJavaLocalDateTime());
    }
}
