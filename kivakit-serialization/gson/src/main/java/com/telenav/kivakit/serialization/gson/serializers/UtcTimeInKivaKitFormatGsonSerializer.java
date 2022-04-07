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

package com.telenav.kivakit.serialization.gson.serializers;

import com.telenav.kivakit.conversion.core.time.LocalDateTimeConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.serialization.gson.PrimitiveGsonSerializer;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Serializes {@link Time} objects to and from JSON as a number of milliseconds since the start of the UNIX epoch.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class UtcTimeInKivaKitFormatGsonSerializer extends PrimitiveGsonSerializer<Time, String>
{
    public UtcTimeInKivaKitFormatGsonSerializer()
    {
        super(String.class);
    }

    @Override
    protected Time toObject(String identifier)
    {
        return new LocalDateTimeConverter(Listener.throwing(), LocalTime.utcTimeZone()).convert(identifier);
    }

    @Override
    protected String toPrimitive(Time time)
    {
        return time.inTimeZone(LocalTime.utcTimeZone()).asDateTimeString();
    }
}
