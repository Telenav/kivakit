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

package com.telenav.kivakit.kernel.language.time.conversion.converters;

import com.telenav.kivakit.kernel.language.time.conversion.BaseFormattedLocalTimeConverter;
import com.telenav.kivakit.kernel.language.time.conversion.TimeFormat;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageTime;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageTime.class)
public class LocalDateTimeWithSecondsConverter extends BaseFormattedLocalTimeConverter
{
    public LocalDateTimeWithSecondsConverter(final Listener listener)
    {
        super(listener, TimeFormat.DATE_TIME_WITH_SECONDS);
    }

    public LocalDateTimeWithSecondsConverter(final Listener listener, final ZoneId zone)
    {
        super(listener, TimeFormat.DATE_TIME_WITH_SECONDS, zone);
    }
}
