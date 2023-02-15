package com.telenav.kivakit.interfaces.object;

import com.telenav.kivakit.interfaces.code.Callback;

import java.util.function.Function;

/**
 * Interface for objects that can copy themselves. The {@link #copy()} method makes a copy. The
 * {@link #applyToCopy(Function)} creates a copy that is transformed by the given function. The method
 * {@link #copy(Callback)} creates a copy that is mutated by the given callback.
 *
 * @author Jonathan Locke
 */
public interface Copyable<T>
{
    /**
     * Returns a copy of this object
     *
     * @return The copy
     */
    T copy();

    /**
     * Returns a copy of this object as modified by the given mutator
     *
     * @param mutator the function
     * @return The copy
     */
    default T copy(Callback<T> mutator)
    {
        var copy = copy();
        mutator.call(copy);
        return copy;
    }

    /**
     * Returns a copy of this object as transformed by the given function
     *
     * @param function the function
     * @return The copy
     */
    default T applyToCopy(Function<T, T> function)
    {
        return function.apply(copy());
    }
}
