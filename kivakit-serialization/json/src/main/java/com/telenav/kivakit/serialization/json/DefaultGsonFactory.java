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

package com.telenav.kivakit.serialization.json;

import com.google.gson.GsonBuilder;
import com.telenav.kivakit.kernel.language.time.Duration;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.serialization.json.serializers.ProblemGsonSerializer;

import static com.telenav.kivakit.kernel.messaging.messages.MessageFormatter.Format.WITHOUT_EXCEPTION;

/**
 * Factory for GSON serializers
 *
 * @author jonathanl (shibo)
 */
public class DefaultGsonFactory extends GsonFactory implements RepeaterMixin
{
    @Override
    protected void onAddSerializers(GsonBuilder builder)
    {
        super.onAddSerializers(builder);

        addSerializer(builder, Problem.class, new ProblemGsonSerializer(WITHOUT_EXCEPTION));
        addSerializer(builder, Duration.class, serializer(new Duration.Converter(this)));
    }

    @Override
    protected void onInitialize(GsonBuilder builder)
    {
        super.onInitialize(builder);

        builder.disableHtmlEscaping();
        builder.setPrettyPrinting();
    }
}
