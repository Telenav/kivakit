////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.objects;

import com.telenav.kivakit.core.kernel.interfaces.factory.MapFactory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A lazy-initializing value whose factory takes a parameter. Given a {@link MapFactory} that creates a value for a
 * parameter, only creates the object when {@link #get(Parameter)} is called. After that the value is cached and
 * {@link #get(Parameter)} will return the same value. {@link #clear()} can be used to clear the value and force
 * it to be re-created in the future.
 * <p>
 * <b>Example</b>
 * <pre>
 *  private static final MapLazy&lt;Type, Session&gt; singleton =
 *      new MapLazy&lt;&gt;(Session::new);
 * </pre>
 *
 * @param <Value> The type of value to create
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageObject.class)
public class MappedLazy<Parameter, Value>
{
    /** The value, or null if it doesn't exist */
    private Value value;

    /** The factory to create a new value */
    private final MapFactory<Parameter, Value> factory;

    public MappedLazy(final MapFactory<Parameter, Value> factory)
    {
        this.factory = factory;
    }

    /**
     * Clears the value so it must be recreated
     */
    public void clear()
    {
        value = null;
    }

    /**
     * @return The value
     */
    public final Value get(final Parameter parameter)
    {
        if (value == null)
        {
            value = factory.newInstance(parameter);
        }
        return value;
    }

    public boolean has()
    {
        return value != null;
    }
}
