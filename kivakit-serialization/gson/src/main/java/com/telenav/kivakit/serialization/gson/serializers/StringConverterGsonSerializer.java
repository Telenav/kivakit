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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.StringConverter;
import com.telenav.kivakit.serialization.gson.factory.JsonSerializerDeserializer;

import java.lang.reflect.Type;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * An adapter that converts a {@link StringConverter} into a {@link JsonSerializer} and {@link JsonDeserializer} by
 * using the {@link StringConverter#convert(Object)} and {@link StringConverter#unconvert(Object)} methods to serialize
 * and deserialize strings.
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class StringConverterGsonSerializer<Value> implements JsonSerializerDeserializer<Value>
{
    private final StringConverter<Value> converter;

    public StringConverterGsonSerializer(StringConverter<Value> converter)
    {
        this.converter = converter;
    }

    @Override
    public Value deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        return converter.convert(context.deserialize(json, String.class));
    }

    @Override
    public JsonElement serialize(Value value, Type typeOfSrc, JsonSerializationContext context)
    {
        return context.serialize(converter.unconvert(value));
    }
}
