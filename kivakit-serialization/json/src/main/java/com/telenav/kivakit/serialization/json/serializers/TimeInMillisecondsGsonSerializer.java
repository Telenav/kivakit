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

package com.telenav.kivakit.serialization.json.serializers;

import com.telenav.kivakit.kernel.language.time.Time;
import com.telenav.kivakit.serialization.json.PrimitiveGsonSerializer;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Serializes {@link Time} objects to and from JSON as a number of milliseconds since the start of the UNIX epoch.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class TimeInMillisecondsGsonSerializer extends PrimitiveGsonSerializer<Time, Long>
{
    public TimeInMillisecondsGsonSerializer()
    {
        super(Long.class);
    }

    @Override
    protected Time toObject(Long identifier)
    {
        return Time.milliseconds(identifier);
    }

    @Override
    protected Long toPrimitive(Time time)
    {
        return time.asMilliseconds();
    }
}
