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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.lang.reflect.Type;

/**
 * Serializer base class for primitive types
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public abstract class PrimitiveGsonSerializer<T, Primitive> implements GsonSerializer<T>
{
    private final Class<Primitive> type;

    protected PrimitiveGsonSerializer(Class<Primitive> type)
    {
        this.type = type;
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        Primitive primitive = context.deserialize(json, type);
        return toObject(primitive);
    }

    @Override
    public JsonElement serialize(T value, Type typeOfSrc, JsonSerializationContext context)
    {
        var primitive = toPrimitive(value);
        return context.serialize(primitive);
    }

    protected abstract T toObject(Primitive scalar);

    protected abstract Primitive toPrimitive(T object);
}
