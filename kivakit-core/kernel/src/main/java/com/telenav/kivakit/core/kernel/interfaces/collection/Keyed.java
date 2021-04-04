////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.interfaces.collection;

/**
 * An object which can retrieve a value given a key.
 *
 * @param <Key> The type of key
 * @param <Value> The type of value
 * @author jonathanl (shibo)
 */
public interface Keyed<Key, Value>
{
    /**
     * @return The value for the given key
     */
    Value get(Key key);
}
