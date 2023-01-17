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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitLocalDateTimeConverter;
import com.telenav.kivakit.core.time.LocalTime;
import com.telenav.kivakit.serialization.gson.PrimitiveGsonSerializer;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.LocalTime.localTimeZone;

/**
 * Serializes {@link LocalTime} objects to and from JSON in KivaKit format.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class LocalTimeGsonSerializer extends PrimitiveGsonSerializer<LocalTime, String>
{
    public LocalTimeGsonSerializer()
    {
        super(String.class);
    }

    @Override
    protected LocalTime toObject(String text)
    {
        return new KivaKitLocalDateTimeConverter(localTimeZone()).convert(text);
    }

    @Override
    protected String toPrimitive(LocalTime time)
    {
        return time.asUtc().toString();
    }
}
