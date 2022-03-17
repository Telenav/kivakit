package com.telenav.kivakit.interfaces.collection;

/**
 * Interface for iteration where hasNext() is replaced by the semantics that null represents the end of iteration
 *
 * @author jonathanl (shibo)
 */
@FunctionalInterface
public interface NextValue<Value>
{
    /**
     * @return The next Value or null if there is none
     */
    Value next();
}
