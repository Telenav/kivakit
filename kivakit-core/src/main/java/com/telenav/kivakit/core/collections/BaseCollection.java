package com.telenav.kivakit.core.collections;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.core.collections.iteration.FilteredIterable;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.string.StringConversions;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Appendable;
import com.telenav.kivakit.interfaces.collection.Joinable;
import com.telenav.kivakit.interfaces.collection.Sequence;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.collections.list.ObjectList.objectList;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.TO_STRING;

/**
 * Base class for all KivaKit collections.
 * <p>
 * A set with a maximum size. Adds useful methods to the usual {@link Set} operations, as well as implementing:
 *
 * <ul>
 *     <li>{@link Addable}</li>
 *     <li>{@link Collection}</li>
 *     <li>{@link Countable}</li>
 *     <li>{@link Joinable}</li>
 *     <li>{@link Sequence}</li>
 *     <li>{@link Sized}</li>
 *     <li>{@link Joinable}</li>
 *     <li>{@link StringFormattable}</li>
 * </ul>
 *
 *  <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link #add(Object)}</li>
 *     <li>{@link #addIfNotNull(Object)}</li>
 *     <li>{@link #addAll(Collection)}</li>
 *     <li>{@link #addAll(Object[])}</li>
 *     <li>{@link #addAll(Iterable)}</li>
 *     <li>{@link #addAll(Iterator)}</li>
 *     <li>{@link #addAllMatching(Object[], Matcher)}</li>
 *     <li>{@link #addAllMatching(Iterable, Matcher)}</li>
 *     <li>{@link #addAllMatching(Iterable, Matcher)}</li>
 *     <li>{@link #addAllMatching(Collection, Matcher)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asSet()}</li>
 *     <li>{@link #asIterable(Matcher)}</li>
 *     <li>{@link #asIterator(Matcher)}</li>
 *     <li>{@link #asString(Format)}</li>
 * </ul>
 *
 * <p><b>Access</b></p>
 *
 * <ul>
 *     <li>{@link #first()}</li>
 *     <li>{@link #mapped(Function)}</li>
 * </ul>
 *
 * <p><b>Membership</b></p>
 *
 * <ul>
 *     <li>{@link #contains(Object)}</li>
 *     <li>{@link #containsAll(Collection)}</li>
 * </ul>
 *
 * <p><b>Size</b></p>
 *
 * <ul>
 *     <li>{@link #size()}</li>
 *     <li>{@link #count()}</li>
 *     <li>{@link #isEmpty()}</li>
 *     <li>{@link #isNonEmpty()}</li>
 * </ul>
 *
 * <p><b>Bounds</b></p>
 *
 * <ul>
 *     <li>{@link #maximumSize()} - The maximum size of this list</li>
 *     <li>{@link #totalRoom()} - The maximum size of this list</li>
 *     <li>{@link #hasRoomFor(int)} - For use by subclasses to check their size</li>
 *     <li>{@link #onOutOfRoom(int)} - Responds with a warning when the list is out of space</li>
 * </ul>
 *
 * <p><b>Removing</b></p>
 *
 * <ul>
 *     <li>{@link #clear()}</li>
 *     <li>{@link #remove(Object)}</li>
 *     <li>{@link #removeAll(Collection)}</li>
 *     <li>{@link #removeIf(Predicate)}</li>
 *     <li>{@link #removeAllMatching(Matcher)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asArray(Class)} - This list as an array of the given type</li>
 *     <li>{@link #asIterable()}</li>
 *     <li>{@link #asIterable(Matcher)}</li>
 *     <li>{@link #asIterator()}</li>
 *     <li>{@link #asIterator(Matcher)}</li>
 *     <li>{@link #asSet()}</li>
 *     <li>{@link #asString(Format)}</li>
 *     <li>{@link #asStringList()}</li>
 * </ul>
 *
 * <p><b>String Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #join()} - This list joined by the list {@link #separator()}</li>
 *     <li>{@link #separator()} - The separator used when joining this list into a string</li>
 * </ul>
 *
 * <p><b>Functional Methods</b></p>
 *
 * <ul>
 *     <li>{@link #mapped(Function)}</li>
 *     <li>{@link #with(Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @author jonathanl (shibo)
 * @see Addable
 * @see Appendable
 * @see Collection
 * @see Countable
 * @see Joinable
 * @see Sequence
 * @see Sized
 * @see StringFormattable
 */
@SuppressWarnings("unused")
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_INSUFFICIENT,
            documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseCollection<Value> implements
        Addable<Value>,
        Collection<Value>,
        Countable,
        Joinable<Value>,
        Sequence<Value>,
        Sized,
        StringFormattable
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** The maximum number of values that can be stored in this list */
    private int maximumSize;

    /** True if this set ran out of room, and we've already warned about it */
    private boolean warnedAboutOutOfRoom;

    /**
     * @param maximumSize The maximum size of this list
     */
    protected BaseCollection(Maximum maximumSize)
    {
        this.maximumSize = ensureNotNull(maximumSize.asInt());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Value value)
    {
        return Addable.super.add(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(@NotNull Collection<? extends Value> values)
    {
        return Addable.super.addAll(values);
    }

    /**
     * @return This list as an array
     */
    @SuppressWarnings({ "unchecked" })
    public Value[] asArray(Class<Value> type)
    {
        var array = (Value[]) Array.newInstance(type, size());
        toArray(array);
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Iterator<Value> asIterator(Matcher<Value> matcher)
    {
        return new BaseIterator<>()
        {
            private final Iterator<Value> iterator = iterator();

            @Override
            protected Value onNext()
            {
                while (iterator.hasNext())
                {
                    var element = iterator.next();
                    if (matcher.matches(element))
                    {
                        return element;
                    }
                }
                return null;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    public String asString(StringFormattable.@NotNull Format format)
    {
        switch (format)
        {
            case DEBUG:
                return join(separator(), StringConversions::toDebugString);

            default:
                return join();
        }
    }

    /**
     * @return This list as a string list
     */
    public StringList asStringList()
    {
        return new StringList(maximumSize(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear()
    {
        backingCollection().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object that)
    {
        return backingCollection().contains(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> collection)
    {
        return new HashSet<>(backingCollection()).containsAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Count count()
    {
        return Count.count(size());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object object)
    {
        return backingCollection().equals(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return backingCollection().hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        return Addable.super.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Value> iterator()
    {
        return backingCollection().iterator();
    }

    /**
     * Returns this bounded list with all elements mapped by the given mapper to the mapper's target type
     */
    @SuppressWarnings("unchecked")
    public <Output> BaseCollection<Output> mapped(Function<Value, Output> mapper)
    {
        var filtered = (BaseCollection<Output>) newCollection();
        for (var element : this)
        {
            filtered.add(mapper.apply(element));
        }
        return filtered;
    }

    /**
     * The matching values in this collection as an {@link Iterable}.
     *
     * @param matcher The matcher
     * @return The matching values
     */
    public Iterable<Value> matchingAsIterable(Matcher<Value> matcher)
    {
        return new FilteredIterable<>(backingCollection(), matcher);
    }

    /**
     * @return The maximum size of this bounded list
     */
    public final Maximum maximumSize()
    {
        return Maximum.maximum(maximumSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onAdd(Value value)
    {
        return backingCollection().add(value);
    }

    /**
     * Called when a bounded list runs out of room
     */
    @Override
    public void onOutOfRoom(int values)
    {
        if (!warnedAboutOutOfRoom)
        {
            warnedAboutOutOfRoom = true;
            LOGGER.warning(new Throwable(), "Adding $ values, would exceed maximum size of $. Ignoring operation.", values, totalRoom());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object element)
    {
        return backingCollection().remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(@NotNull Collection<?> collection)
    {
        return backingCollection().removeAll(collection);
    }

    /**
     * Removes all values matching the given matcher
     */
    public boolean removeAllMatching(Matcher<Value> matcher)
    {
        return removeIf(matcher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(@NotNull Collection<?> collection)
    {
        return backingCollection().retainAll(collection);
    }

    /**
     * @return The separator to use when joining this list
     */
    @Override
    public String separator()
    {
        return ", ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size()
    {
        return backingCollection().size();
    }

    /**
     * @return A copy of this collection sorted by the given comparator
     */
    public ObjectList<Value> sorted(Comparator<Value> comparator)
    {
        var sorted = objectList(this);
        sorted.sort(comparator);
        return sorted;
    }

    /**
     * @return An {@link ObjectList} with the values in this collection in sorted order.
     */
    @SuppressWarnings("unchecked")
    public ObjectList<Value> sorted()
    {
        return sorted((a, b) ->
        {
            if (a instanceof Comparable)
            {
                return ((Comparable<Value>) a).compareTo(b);
            }
            throw new IllegalStateException("Cannot sort list of values that don't implement Comparable");
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray()
    {
        return backingCollection().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "SuspiciousToArrayCall" })
    @Override
    public <E> E[] toArray(E @NotNull [] array)
    {
        return backingCollection().toArray(array);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return asString(TO_STRING);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int totalRoom()
    {
        return maximumSize;
    }

    /**
     * Returns this list with the given values
     */
    public BaseCollection<Value> with(Collection<Value> that)
    {
        var copy = newCollection();
        copy.addAll(this);
        copy.addAll(that);
        return copy;
    }

    /**
     * Makes a copy of this object but with the given value appended
     *
     * @param value The value to add
     * @return This object
     */
    public BaseCollection<Value> with(Value value)
    {
        var copy = newCollection();
        copy.addAll(this);
        copy.add(value);
        return copy;
    }

    /**
     * Returns the collection being wrapped
     */
    protected abstract Collection<Value> backingCollection();

    /**
     * Creates a new collection of the subtype class
     */
    protected final BaseCollection<Value> newCollection()
    {
        var instance = onNewCollection();
        instance.maximumSize = maximumSize;
        return instance;
    }

    /**
     * Creates a new collection of the subtype class
     */
    protected abstract BaseCollection<Value> onNewCollection();

    /**
     * Convert the given value to a string
     *
     * @param value The value
     * @return A string corresponding to the value
     */
    protected String toString(Value value)
    {
        return StringConversions.toString(value);
    }
}
