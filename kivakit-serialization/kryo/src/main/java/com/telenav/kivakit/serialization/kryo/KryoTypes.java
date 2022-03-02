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
import com.telenav.kivakit.core.collections.Collections;
import com.telenav.kivakit.core.collections.Maps;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.Debug;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.serialization.core.SerializationSession;
import com.telenav.kivakit.serialization.core.SerializationSessionFactory;
import com.telenav.lexakai.annotations.LexakaiJavadoc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.telenav.kivakit.core.ensure.Ensure.fail;

/**
 * A set of Kryo type registrations required to construct a {@link KryoSerializationSession}.
 *
 * <p><b>Kryo Serialization Identifiers</b></p>
 *
 * <p>
 * For best performance using Kryo serialization, types are assigned Kryo serialization identifiers. See the Kryo
 * documentation for more information. Types registered with this class are assigned identifiers in sequential order in
 * groups. The method {@link #group(String, Runnable)} brackets the next group of identifiers of {@link #GROUP_SIZE},
 * assigning the next identifier from the group to each class registered within the group using {@link #register(Class)}
 * or {@link #register(Class, Serializer)}. For example:
 * </p>
 *
 * <pre>
 * group("collections", () -&gt;
 * {
 *     register(HashMap.class);    // identifier 2,000,001
 *     register(HashSet.class);    // identifier 2,000,002
 *     register(ArrayList.class);  // identifier 2,000,003
 *     register(LinkedList.class); // identifier 2,000,004
 * });
 *
 * group("primitives", () -&gt;
 * {
 *     register(byte[].class);     // identifier 2,001,001
 *     register(short[].class);    // identifier 2,001,002
 * });
 * </pre>
 *
 * <p><b>Serialization</b></p>
 *
 * <p>
 * Once all relevant serializable types have been added to a type set, it can be merged with other type set(s) using
 * {@link #mergedWith(KryoTypes)}. The resulting merged set can be used to produce a {@link SerializationSessionFactory}
 * with {@link #sessionFactory()}. This session factory can then be used to create a {@link SerializationSession} which
 * can be used to read and write data.
 * </p>
 *
 * <p><b>Backwards Compatibility</b></p>
 *
 * <p>
 * To maintain backwards compatibility with prior serialized data, it is necessary to add types and groups in the same
 * order, and to merge {@link KryoTypes} sets in the same order. If new items need to be added, they must be added at
 * the end of the list. Plenty of space is available in the identifier groupings to allow for this (1,000 per group and
 * 1,000,000 per {@link KryoTypes} set).
 * </p>
 *
 * <p><b>Important Note</b></p>
 *
 * <p>
 * This serialization abstraction does not currently make use of the Kryo serializer *CompatibleFieldSerializer* for
 * performance reasons. It may be desirable to add this in the future to support the altering of fields without breaking
 * backwards (and forwards) data compatibility.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see KryoSerializationSession
 */
@LexakaiJavadoc(complete = true)
public class KryoTypes implements Named
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    /** The number of identifiers in each group */
    public static final int GROUP_SIZE = 1_000;

    /** The number of identifiers in each KryoTypes set */
    public static final int KRYO_TYPES_SIZE = 2_000_000;

    /** The base identifier for dynamic registration */
    private static final int DYNAMIC_IDENTIFIER_FIRST = 1_000_000;

    /** Entries for classes */
    private Map<Class<?>, KryoTypeEntry> entries = new LinkedHashMap<>();

    /** Used identifiers */
    private final Map<Integer, Class<?>> identifiers = new HashMap<>();

    /** The Kryo type sets that have been merged into this set */
    private ObjectList<KryoTypes> merged = new ObjectList<>();

    /** The next identifier for Kryo registration */
    private int nextIdentifier = KRYO_TYPES_SIZE;

    public KryoTypes()
    {
        merged.add(this);
    }

    /**
     * @return A deep copy of the given type set
     */
    public KryoTypes deepCopy()
    {
        // Make a copy of this set
        var copy = new KryoTypes();
        copy.merged = merged.copy();
        copy.entries = Maps.deepCopy(LinkedHashMap::new, entries, KryoTypeEntry::new);
        copy.nextIdentifier = nextIdentifier;
        return copy;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof KryoTypes)
        {
            KryoTypes that = (KryoTypes) object;
            return entries.equals(that.entries);
        }
        return false;
    }

    /**
     * Delimits a group of related registrations
     */
    public void group(String name, Runnable code)
    {
        // Advance to the next group
        nextGroup();

        // then run the bracketed code block
        code.run();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(nextIdentifier);
    }

    /**
     * @return The identifier for the given type
     */
    public int identifier(Class<?> type)
    {
        return entries.get(type).identifier();
    }

    /**
     * @return A new type set that contains the entries from this type set and those from the given set
     */
    public KryoTypes mergedWith(KryoTypes that)
    {
        // Create deep copy of this set,
        var merged = deepCopy();

        // set its next identifier to the next set,
        merged.nextKryoTypes();

        // then go through the entries in that,
        for (var entry : that.entries.values())
        {
            // check that the entry is not already in the merged set,
            assert !merged.entries.containsValue(entry);

            // and add it.
            if (entry.isDynamic())
            {
                merged.registerDynamic(entry.type(), entry.serializer(), entry.identifier());
            }
            else
            {
                merged.register(entry.type(), entry.serializer());
            }
        }

        var thisFirstEntry = Collections.first(entries.values());
        var thatFirstEntry = Collections.first(that.entries.values());

        assert thisFirstEntry != null;
        assert thatFirstEntry != null;

        // and finally, return the merged set.
        merged.merged.add(that);
        return merged;
    }

    @Override
    public String name()
    {
        return merged.join("+", set -> Classes.simpleName(set.getClass()));
    }

    /**
     * Registers the given class with Kryo using the next registration identifier in the current registration group.
     *
     * @param type The type to register
     */
    public KryoTypes register(Class<?> type)
    {
        addEntry(type, null, ++nextIdentifier);
        return this;
    }

    /**
     * Registers the given type for serialization using the given serializer
     *
     * @param type The type to register
     * @param serializer The serializer to use for the given type
     */
    public KryoTypes register(Class<?> type, Serializer<?> serializer)
    {
        addEntry(type, serializer, ++nextIdentifier);
        return this;
    }

    /**
     * Registers the given type for serialization using the given serializer
     *
     * @param type The type to register
     * @param serializer The serializer to use for the given type
     * @param identifier The identifier to register
     */
    public KryoTypes registerDynamic(Class<?> type, Serializer<?> serializer, int identifier)
    {
        addEntry(type, serializer, DYNAMIC_IDENTIFIER_FIRST + identifier);
        return this;
    }

    /**
     * @return The set of all registered types
     */
    public Set<Class<?>> registeredTypes()
    {
        return entries.keySet();
    }

    /**
     * @return A session factory for this set of types
     */
    public SerializationSessionFactory sessionFactory()
    {
        return new SerializationSessionFactory(() -> new KryoSerializationSession(this));
    }

    @Override
    public String toString()
    {
        var lines = new StringList();
        for (var entry : entries.values())
        {
            lines.add(entry.toString());
        }
        return lines.join("\n");
    }

    /**
     * <b>Not public API</b>
     *
     * <p>
     * Registers all the kryo types in set of types with the given {@link Kryo} object.
     * </p>
     *
     * @param kryo The kryo serializer to register types with
     */
    void registerWith(Kryo kryo)
    {
        DEBUG.trace("Registering $", name());

        // Go through each entry,
        for (var entry : entries.values())
        {
            // and register it with kryo,
            DEBUG.trace("Registering $", entry);
            entry.register(kryo);
        }
    }

    /**
     * Registers the given type for serialization using the given identifier
     *
     * @param type The type to register
     * @param serializer Any type serializer
     */
    private void addEntry(Class<?> type, Serializer<?> serializer, int identifier)
    {
        // If the identifier is already used,
        if (identifiers.containsKey(identifier))
        {
            // make sure it is a re-registration of the same type
            var registeredType = identifiers.get(identifier);
            if (!registeredType.equals(type))
            {
                fail("Identifier $ for ${class} already used by type ${class}", identifier, type, registeredType);
            }
        }

        // then create the type entry
        var entry = new KryoTypeEntry()
                .type(type)
                .serializer(serializer)
                .identifier(identifier);

        // and store it in the map.
        DEBUG.trace("Register $ => $", identifier, type);
        entries.put(type, entry);
        identifiers.put(identifier, type);
    }

    private void nextGroup()
    {
        nextIdentifier = ((nextIdentifier / GROUP_SIZE) + 1) * GROUP_SIZE;
    }

    private void nextKryoTypes()
    {
        nextIdentifier = ((nextIdentifier / KRYO_TYPES_SIZE) + 1) * KRYO_TYPES_SIZE;
    }
}
