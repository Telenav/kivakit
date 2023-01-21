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

package com.telenav.kivakit.serialization.gson.serializers.value;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonValueSerializer;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Serializes {@link Problem}s to and from JSON.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class ProblemGsonSerializer extends BaseGsonValueSerializer<Problem, String>
{
    private final MessageFormat format;

    public ProblemGsonSerializer(Listener listener, MessageFormat format)
    {
        super(Problem.class, String.class);
        this.format = format;
    }

    @Override
    protected Problem onDeserialize(String serialized)
    {
        return new Problem(serialized);
    }

    @Override
    protected String onSerialize(Problem value)
    {
        return value.formatted(format);
    }
}
