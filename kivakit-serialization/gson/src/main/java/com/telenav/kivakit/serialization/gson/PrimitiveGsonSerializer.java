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

package com.telenav.kivakit.serialization.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.serialization.gson.factory.JsonSerializerDeserializer;

import java.lang.reflect.Type;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Serializer base class for implementing {@link Gson} serialization and deserialization of primitive types. Subclasses
 * must override {@link #toPrimitive(Object)} and {@link #toObject(Object)}.
 *
 * @author jonathanl (shibo)
 * @see JsonSerializer
 * @see JsonDeserializer
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public abstract class PrimitiveGsonSerializer<T, Primitive> implements JsonSerializerDeserializer<T>
{
    private final Class<Primitive> type;

    /**
     * @param type The primitive type
     */
    protected PrimitiveGsonSerializer(Class<Primitive> type)
    {
        this.type = type;
    }

    /**
     * {@link JsonDeserializer} method for deserializing a primitive value
     */
    @Override
    public T deserialize(JsonElement json,
                         Type typeOfT,
                         JsonDeserializationContext context) throws JsonParseException
    {
        Primitive primitive = context.deserialize(json, type);
        return toObject(primitive);
    }

    /**
     * {@link JsonSerializer} method for serializing an object
     */
    @Override
    public JsonElement serialize(T value, Type typeOfSrc, JsonSerializationContext context)
    {
        var primitive = toPrimitive(value);
        return context.serialize(primitive);
    }

    /**
     * Convert the given primitive value to an object. For example, a {@link Duration} can be constructed from a
     * <i>long</i> value representing the number of milliseconds.
     *
     * @param primitive The primitive to convert
     * @return The object constructed from the primitive value
     */
    protected abstract T toObject(Primitive primitive);

    /**
     * Convert the given object to a primitive value. For example, a {@link Duration} can be converted to a
     * <i>long</i> value representing the number of milliseconds.
     *
     * @param object The object to convert
     * @return The primitive value that represents the object
     */
    protected abstract Primitive toPrimitive(T object);
}
