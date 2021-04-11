////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.service.registry.serialization;

import com.google.gson.GsonBuilder;
import com.telenav.kivakit.core.application.ApplicationIdentifier;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.network.core.Port;
import com.telenav.kivakit.core.serialization.json.GsonFactory;
import com.telenav.kivakit.service.registry.ServiceType;
import com.telenav.kivakit.service.registry.serialization.serializers.ApplicationIdentifierSerializer;
import com.telenav.kivakit.service.registry.serialization.serializers.ProblemSerializer;
import com.telenav.kivakit.service.registry.serialization.serializers.ServiceTypeSerializer;
import com.telenav.kivakit.service.registry.serialization.serializers.TimeSerializer;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * Factory for GSON serializers
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ServiceRegistryGsonFactory extends GsonFactory
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Override
    protected GsonBuilder addSerializers(final GsonBuilder builder)
    {
        addSerializer(builder, Port.class, serializer(new Port.Converter(LOGGER)));
        addSerializer(builder, ApplicationIdentifier.class, new ApplicationIdentifierSerializer());
        addSerializer(builder, ServiceType.class, new ServiceTypeSerializer());
        addSerializer(builder, Problem.class, new ProblemSerializer());
        addSerializer(builder, Time.class, new TimeSerializer());
        return builder;
    }
}
