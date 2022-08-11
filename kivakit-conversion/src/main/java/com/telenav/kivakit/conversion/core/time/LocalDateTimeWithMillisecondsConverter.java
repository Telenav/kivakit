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

import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.TimeFormats;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
public class LocalDateTimeWithMillisecondsConverter extends BaseFormattedLocalTimeConverter
{
    public LocalDateTimeWithMillisecondsConverter(Listener listener)
    {
        super(listener, TimeFormats.KIVAKIT_DATE_TIME_MILLISECONDS);
    }

    public LocalDateTimeWithMillisecondsConverter(Listener listener, ZoneId zone)
    {
        super(listener, TimeFormats.KIVAKIT_DATE_TIME_MILLISECONDS, zone);
    }
}
