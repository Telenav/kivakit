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

package

        com.telenav.kivakit.core.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.language.collections.Collections;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.collections.map.Maps;
import com.telenav.kivakit.core.kernel.language.types.Classes;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.serialization.core.SerializationSession;
import com.telenav.kivakit.core.serialization.core.SerializationSessionFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.ensure;

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
 *     register(HashMap.class);    // identifier 1,000,001
 *     register(HashSet.class);    // identifier 1,000,002
 *     register(ArrayList.class);  // identifier 1,000,003
 *     register(LinkedList.class); // identifier 1,000,004
 * });
 *
 * group("primitives", () -&gt;
 * {
 *     register(byte[].class);     // identifier 1,001,001
 *     register(short[].class);    // identifier 1,001,002
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
public class KryoTypes implements Named
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    /** The number of identifiers in each group */
    public static final int GROUP_SIZE = 1_000;

    /** The number of identifiers in each KryoTypes set */
    public static final int KRYO_TYPES_SIZE = 1_000_000;

    /** The next identifier for Kryo registration */
    private int nextIdentifier = KRYO_TYPES_SIZE;

    /** The Kryo type sets that have been merged into this set */
    private ObjectList<KryoTypes> merged = new ObjectList<>();

    /** Entries for classes */
    private Map<Class<?>, KryoTypeEntry> entries = new LinkedHashMap<>();

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
        final var copy = new KryoTypes();
        copy.merged = merged.copy();
        copy.entries = Maps.deepCopy(LinkedHashMap::new, entries, KryoTypeEntry::new);
        copy.nextIdentifier = nextIdentifier;
        return copy;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof KryoTypes)
        {
            final KryoTypes that = (KryoTypes) object;
            return entries.equals(that.entries);
        }
        return false;
    }

    /**
     * Delimits a group of related registrations
     */
    public void group(final String name, final Runnable code)
    {
        // Run the bracketed code block
        code.run();

        // then advance to the next group
        nextGroup();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(nextIdentifier);
    }

    /**
     * @return The identifier for the given type
     */
    public int identifier(final Class<?> type)
    {
        return entries.get(type).identifier();
    }

    /**
     * @return A new type set that contains the entries from this type set and those from the given set
     */
    public KryoTypes mergedWith(final KryoTypes that)
    {
        // Create deep copy of this set,
        final var merged = deepCopy();

        // set its next identifier to the next set,
        merged.nextKryoTypes();

        // then go though the entries in that,
        for (final var entry : that.entries.values())
        {
            // check that the entry is not already in the merged set,
            assert !merged.entries.containsValue(entry);

            // and add it.
            merged.register(entry.type(), entry.serializer());
        }

        final var thisFirstEntry = Collections.first(entries.values());
        final var thatFirstEntry = Collections.first(that.entries.values());

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
    public KryoTypes register(final Class<?> type)
    {
        addEntry(type, null);
        return this;
    }

    /**
     * Registers the given type for serialization using the given serializer
     *
     * @param type The type to register
     * @param serializer The serializer to use for the given type
     */
    public KryoTypes register(final Class<?> type, final Serializer<?> serializer)
    {
        addEntry(type, serializer);
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
        final var lines = new StringList();
        for (final var entry : entries.values())
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
    void registerWith(final Kryo kryo)
    {
        DEBUG.trace("Registering $", name());

        // Go through each entry,
        for (final var entry : entries.values())
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
    private void addEntry(final Class<?> type, final Serializer<?> serializer)
    {
        // Get any existing identifier,
        final var existing = entries.get(type);

        // and ensure that there isn't one,
        ensure(existing == null, "Identifier conflict for ${class}", type);

        // then create the type entry
        final var entry = new KryoTypeEntry()
                .type(type)
                .serializer(serializer)
                .identifier(nextIdentifier++);

        // and store it in the map.
        entries.put(type, entry);
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
