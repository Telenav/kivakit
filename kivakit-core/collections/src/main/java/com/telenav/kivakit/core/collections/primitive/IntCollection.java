////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.collections.primitive;

import com.telenav.kivakit.core.collections.primitive.iteration.IntIterable;
import com.telenav.kivakit.core.collections.primitive.iteration.IntIterator;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveCollection;
import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.core.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * A collection of primitive int values. All primitive collections have a name that can be retrieved with {@link
 * #objectName()} and a size retrieved with {@link Sized#size()} and they can be emptied with {@link #clear()}. Values
 * in this collection can be iterated with an {@link IntIterator} returned by the {@link IntIterable#iterator()}
 * method.
 * <p>
 * A null value can be assigned to the collection with {@link #nullInt()} and a value can be tested for nullity with
 * {@link #isNull(int)}. In the case where null values may not be desired, {@link #hasNullInt(boolean)} can be used to
 * designate that a collection has no null value and {@link #hasNullInt()} will return true if the collection has a null
 * value.
 * <p>
 * New values can be added with {@link #add(int)}, {@link #addAll(IntCollection)} and {@link #addAll(int[])} and removed
 * with {@link #remove(int)}. In addition, {@link Quantizable} values can be added with {@link #addAll(List)}, where
 * each value is quantized via {@link Quantizable#quantum()} before being added. Since many objects are quantizable,
 * this method is especially useful.
 * <p>
 * Whether a given value or collection of values is in the collection can be determined with {@link #contains(int)} and
 * {@link #containsAll(IntCollection)}.
 *
 * @author jonathanl (shibo)
 * @see Named
 * @see Sized
 * @see IntIterable
 * @see Quantizable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveCollection.class)
public interface IntCollection extends IntIterable, Sized, NamedObject
{
    /**
     * Adds a value to this collection
     *
     * @return True if the value was added, false if there was no room to add it
     */
    boolean add(int value);

    /**
     * Adds all values in the given array
     */
    default boolean addAll(final int[] that)
    {
        for (final var value : that)
        {
            if (!add(value))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the given quantizable values
     */
    default void addAll(final List<? extends Quantizable> values)
    {
        for (final Quantizable value : values)
        {
            add((int) value.quantum());
        }
    }

    /**
     * Adds all values in the given array
     */
    default boolean addAll(final IntCollection that)
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
    default boolean contains(final int value)
    {
        return unsupported();
    }

    /**
     * @return True if this collection contains all the values in the given collection
     */
    default boolean containsAll(final IntCollection that)
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
     * @return True if there is a null int defined for this collection
     */
    boolean hasNullInt();

    /**
     * Sets whether this collection has a null value or not
     */
    PrimitiveCollection hasNullInt(boolean has);

    /**
     * @return True if the given value is the null value
     */
    boolean isNull(int value);

    /**
     * @return The int being used to represent the null value
     */
    int nullInt();

    /**
     * Removes the given value from this collection. Some collections may choose not to implement this method.
     *
     * @return True if the value was removed
     */
    default boolean remove(final int value)
    {
        return unsupported();
    }
}
