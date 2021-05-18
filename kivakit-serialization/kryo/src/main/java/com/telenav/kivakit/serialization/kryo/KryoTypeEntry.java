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

package com.telenav.kivakit.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.telenav.kivakit.kernel.language.objects.Hash;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Kryo type entries in the entries map
 * </p>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
class KryoTypeEntry
{
    /** The serialized type */
    private Class<?> type;

    /** Any (optional) custom serializer */
    private Serializer<?> serializer;

    /** The Kryo registration identifier */
    private int identifier;

    KryoTypeEntry()
    {
    }

    KryoTypeEntry(final KryoTypeEntry that)
    {
        type = that.type;
        serializer = that.serializer;
        identifier = that.identifier;
    }

    public KryoTypeEntry copy()
    {
        return new KryoTypeEntry(this);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof KryoTypeEntry)
        {
            final KryoTypeEntry that = (KryoTypeEntry) object;
            return type.equals(that.type) && identifier == that.identifier;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.many(type, identifier);
    }

    @Override
    public String toString()
    {
        return Message.format("${class} ($)", type(), identifier());
    }

    KryoTypeEntry identifier(final int identifier)
    {
        this.identifier = identifier;
        return this;
    }

    int identifier()
    {
        return identifier;
    }

    void register(final Kryo kryo)
    {
        if (serializer == null)
        {
            kryo.register(type, identifier());
        }
        else
        {
            kryo.register(type, serializer, identifier());
        }
    }

    Serializer<?> serializer()
    {
        return serializer;
    }

    KryoTypeEntry serializer(final Serializer<?> serializer)
    {
        this.serializer = serializer;
        return this;
    }

    KryoTypeEntry type(final Class<?> type)
    {
        this.type = type;
        return this;
    }

    Class<?> type()
    {
        return type;
    }
}
