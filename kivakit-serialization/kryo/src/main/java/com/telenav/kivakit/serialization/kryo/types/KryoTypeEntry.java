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

package com.telenav.kivakit.serialization.kryo.types;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.language.Hash;
import com.telenav.kivakit.core.string.Strings;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiType.PRIVATE_API;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Kryo type entries in the entries map
 * </p>
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED,
            type = PRIVATE_API)
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

    KryoTypeEntry(KryoTypeEntry that)
    {
        type = that.type;
        serializer = that.serializer;
        identifier = that.identifier;
    }

    /**
     * Returns a copy of this entry
     */
    public KryoTypeEntry copy()
    {
        return new KryoTypeEntry(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof KryoTypeEntry)
        {
            KryoTypeEntry that = (KryoTypeEntry) object;
            return type.equals(that.type) && identifier == that.identifier;
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Hash.hashMany(type, identifier);
    }

    @Override
    public String toString()
    {
        return Strings.format("${class} ($)", type(), identifier());
    }

    KryoTypeEntry identifier(int identifier)
    {
        this.identifier = identifier;
        return this;
    }

    int identifier()
    {
        return identifier;
    }

    boolean isDynamic()
    {
        return identifier < KryoTypes.KRYO_TYPES_SIZE;
    }

    void register(Kryo kryo)
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

    KryoTypeEntry serializer(Serializer<?> serializer)
    {
        this.serializer = serializer;
        return this;
    }

    KryoTypeEntry type(Class<?> type)
    {
        this.type = type;
        return this;
    }

    Class<?> type()
    {
        return type;
    }
}
