////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive;

import com.telenav.kivakit.core.collections.primitive.iteration.ShortIterable;
import com.telenav.kivakit.core.collections.primitive.iteration.ShortIterator;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveCollection;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A collection of primitive short values. All primitive collections have a name that can be retrieved with {@link
 * #objectName()} ()} and a size retrieved with {@link Sized#size()} and they can be emptied with {@link #clear()}.
 * Values in this collection can be iterated with an {@link ShortIterator} returned by the {@link
 * ShortIterable#iterator()} method.
 * <p>
 * A null value can be assigned to the collection with {@link #nullShort()} and a value can be tested for nullity with
 * {@link #isNull(short)}. In the case where null values may not be desired, {@link #hasNullShort(boolean)} can be used
 * to designate that a collection has no null value and {@link #hasNullShort()} will return true if the collection has a
 * null value.
 * <p>
 * New values can be added with {@link #add(short)}, {@link #addAll(ShortCollection)} and {@link #addAll(short[])} and
 * removed with {@link #remove(short)}. In addition, {@link Quantizable} values can be added with {@link #addAll(List)},
 * where each value is quantized via {@link Quantizable#quantum()} before being added. Since many objects are
 * quantizable, this method is especially useful.
 * <p>
 * Whether a given value or collection of values is in the collection can be determined with {@link #contains(short)}
 * and {@link #containsAll(ShortCollection)}.
 *
 * @author jonathanl (shibo)
 * @see Named
 * @see Sized
 * @see ShortIterable
 * @see Quantizable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveCollection.class)
public interface ShortCollection extends ShortIterable, Sized, NamedObject
{
    /**
     * Adds a value to this collection
     *
     * @return True if the value was added, false if there was no room to add it
     */
    boolean add(short value);

    /**
     * Adds all values in the given array
     */
    default void addAll(final short[] that)
    {
        for (final var value : that)
        {
            add(value);
        }
    }

    /**
     * Adds the given quantizable values
     */
    default void addAll(final List<? extends Quantizable> values)
    {
        for (final Quantizable value : values)
        {
            add((short) value.quantum());
        }
    }

    /**
     * Adds all values in the given array
     */
    default boolean addAll(final ShortCollection that)
    {
        final var iterator = that.iterator();
        while (iterator.hasNext())
        {
            if (!add(iterator.next()))
            {
                return false;
            }
        }
        return false;
    }

    /**
     * Removes all values from this collection
     */
    default void clear()
    {
        unsupported();
    }

    /**
     * @return True if this collection contains the given value. Some collections may choose not to implement this
     * method if the search is too inefficient.
     */
    default boolean contains(final short value)
    {
        return unsupported();
    }

    /**
     * @return True if this collection contains all the values in the given collection
     */
    default boolean containsAll(final ShortCollection that)
    {
        final var values = that.iterator();
        while (values.hasNext())
        {
            if (!contains(values.next()))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True if there is a null short defined for this collection
     */
    boolean hasNullShort();

    /**
     * Sets whether this collection has a null value or not
     */
    PrimitiveCollection hasNullShort(boolean has);

    /**
     * @return True if the given value is the null value
     */
    boolean isNull(short value);

    /**
     * @return The short being used to represent the null value
     */
    short nullShort();

    /**
     * Removes the given value from this collection. Some collections may choose not to implement this method.
     *
     * @return True if the value was removed
     */
    default boolean remove(final short value)
    {
        return unsupported();
    }
}
