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

package com.telenav.kivakit.conversion.core.time.kivakit;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.time.BaseLocalTimeConverter;
import com.telenav.kivakit.conversion.internal.lexakai.DiagramConversionTime;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.time.ZoneId;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.core.time.KivaKitTimeFormats.KIVAKIT_TIME;

/**
 * Converts local time values to and from a KivaKit-defined format that is a valid filename across Mac, Linux and
 * Windows operating systems
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramConversionTime.class)
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class KivaKitLocalTimeConverter extends BaseLocalTimeConverter
{
    public KivaKitLocalTimeConverter(Listener listener, ZoneId zone)
    {
        super(listener, KIVAKIT_TIME, zone);
    }

    public KivaKitLocalTimeConverter()
    {
        this(throwingListener());
    }

    public KivaKitLocalTimeConverter(ZoneId zone)
    {
        this(throwingListener(), zone);
    }

    /**
     * @param listener The listener to report problems to
     */
    public KivaKitLocalTimeConverter(Listener listener)
    {
        super(listener, KIVAKIT_TIME, ZoneId.systemDefault());
    }
}
