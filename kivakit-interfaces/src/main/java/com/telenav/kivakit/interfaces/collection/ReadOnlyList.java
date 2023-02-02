package com.telenav.kivakit.interfaces.collection;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * A {@link List} that implements all mutation methods by throwing an exception
 *
 * @author Jonathan Locke
 */
public interface ReadOnlyList<E> extends List<E>
{
    @Override
    default boolean add(E e)
    {
        readOnly();
        return false;
    }

    @Override
    default void add(int index, E element)
    {
        readOnly();
    }

    @Override
    default boolean addAll(@NotNull Collection<? extends E> c)
    {
        readOnly();
        return false;
    }

    @Override
    default boolean addAll(int index, @NotNull Collection<? extends E> c)
    {
        readOnly();
        return false;
    }

    @Override
    default void clear()
    {
        readOnly();
    }

    @Override
    default boolean remove(Object o)
    {
        readOnly();
        return false;
    }

    @Override
    default E remove(int index)
    {
        readOnly();
        return null;
    }

    @Override
    default boolean removeAll(@NotNull Collection<?> c)
    {
        readOnly();
        return false;
    }

    @Override
    default boolean retainAll(@NotNull Collection<?> c)
    {
        readOnly();
        return false;
    }

    @Override
    default E set(int index, E element)
    {
        readOnly();
        return null;
    }

    @NotNull
    @Override
    default List<E> subList(int fromIndex, int toIndex)
    {
        readOnly();
        return null;
    }

    private <T> T readOnly()
    {
        throw new IllegalStateException("Cannot modify read only list");
    }
}
