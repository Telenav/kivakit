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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.MessageFormat;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.serialization.gson.PrimitiveGsonSerializer;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Serializes {@link Problem}s to and from JSON.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class ProblemGsonSerializer extends PrimitiveGsonSerializer<Problem, String>
{
    private final MessageFormat format;

    public ProblemGsonSerializer(MessageFormat format)
    {
        super(String.class);
        this.format = format;
    }

    @Override
    protected Problem toObject(String message)
    {
        return new Problem(message);
    }

    @Override
    protected String toPrimitive(Problem problem)
    {
        return problem.formatted(format);
    }
}
