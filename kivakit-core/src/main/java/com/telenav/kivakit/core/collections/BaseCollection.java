package com.telenav.kivakit.core.collections;

import com.telenav.kivakit.core.collections.iteration.BaseIterator;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.string.StringTo;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Countable;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.interfaces.collection.Addable;
import com.telenav.kivakit.interfaces.collection.Appendable;
import com.telenav.kivakit.interfaces.collection.Copyable;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Function;

import static com.telenav.kivakit.interfaces.string.StringFormattable.Format.TO_STRING;

/**
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused") public abstract class BaseCollection<Value> implements
        Iterable<Value>,
        Sized,
        Countable,
        Addable<Value>,
        Copyable<Value, BaseCollection<Value>>,
        Appendable<Value>,
        Collection<Value>,
        StringFormattable
{
    /** The maximum number of values that can be stored in this list */
    private int maximumSize;

    /** True if this set ran out of room, and we've already warned about it */
    private boolean warnedAboutOutOfRoom;

    /**
     * @param maximumSize The maximum size of this list
     */
    protected BaseCollection(Maximum maximumSize)
    {
        this.maximumSize = maximumSize.asInt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(Value value)
    {
        return hasRoomFor(1) && onAdd(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends Value> elements)
    {
        if (hasRoomFor(elements.size()))
        {
            return collection().addAll(elements);
        }
        return false;
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

    @Override
    public @NotNull
    Iterator<Value> asIterator()
    {
        return asIterator(Matcher.matchAll());
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
    public String asString(StringFormattable.Format format)
    {
        switch (format)
        {
            case DEBUG:
                return join(separator(), StringTo::debug);

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
        collection().clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object that)
    {
        return collection().contains(that);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(@NotNull Collection<?> collection)
    {
        return new HashSet<>(collection()).containsAll(collection);
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
        return collection().equals(object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return collection().hashCode();
    }

    @Override
    public boolean isEmpty()
    {
        return Addable.super.isEmpty();
    }

    @Override
    public boolean isZero()
    {
        return Countable.super.isZero();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Value> iterator()
    {
        return collection().iterator();
    }

    /**
     * @return This bounded list with all elements mapped by the given mapper to the mapper's target type
     */
    @SuppressWarnings("unchecked")
    public <Output> BaseCollection<Output> mapped(Function<Value, Output> mapper)
    {
        var filtered = (BaseCollection<Output>) newInstance();
        for (var element : this)
        {
            filtered.add(mapper.apply(element));
        }
        return filtered;
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
        return collection().add(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onAppend(Value value)
    {
        return onAdd(value);
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
            Ensure.warning(new Throwable(), "Adding $ values, would exceed maximum size of $. Ignoring operation.", values, totalRoom());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object element)
    {
        return collection().remove(element);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(@NotNull Collection<?> collection)
    {
        return collection().removeAll(collection);
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
        return collection().retainAll(collection);
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
        return collection().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray()
    {
        return collection().toArray();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "SuspiciousToArrayCall" })
    @Override
    public <E> E[] toArray(E @NotNull [] array)
    {
        return collection().toArray(array);
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
     * Returns this list with the given value
     */
    public BaseCollection<Value> with(Value value)
    {
        var copy = copy();
        copy.add(value);
        return copy;
    }

    /**
     * Returns this list with the given values
     */
    public BaseCollection<Value> with(Collection<Value> that)
    {
        var set = copy();
        set.addAll(that);
        return set;
    }

    /**
     * @return The collection being wrapped
     */
    protected abstract Collection<Value> collection();

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
        return StringTo.string(value);
    }
}
