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

package com.telenav.kivakit.serialization.gson.serializers.time;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonSerializer;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.time.Time.epochMilliseconds;

/**
 * Serializes {@link Time} objects to and from JSON as a number of milliseconds since the start of the UNIX epoch.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class TimeInMillisecondsGsonSerializer extends BaseGsonSerializer<Time, Long>
{
    public TimeInMillisecondsGsonSerializer()
    {
        super(Time.class, Long.class);
    }

    @Override
    protected Time onDeserialize(Long serialized)
    {
        return epochMilliseconds(serialized);
    }

    @Override
    protected Long onSerialize(Time value)
    {
        return value.milliseconds();
    }
}
