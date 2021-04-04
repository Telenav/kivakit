////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.objects;

import com.telenav.kivakit.core.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.core.kernel.interfaces.loading.Loadable;
import com.telenav.kivakit.core.kernel.interfaces.loading.Unloadable;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A lazy-initializing value. Given a factory that creates a value, only creates the object when {@link #get()} is
 * called. After that the value is cached and {@link #get()} will return the same value. {@link #clear()} can be used to
 * clear the value and force it to be re-created in the future.
 * <p>
 * <b>Example</b>
 * <pre>
 *  private static final Lazy&lt;EdgeAttributes&gt; singleton =
 *      new Lazy&lt;&gt;(EdgeAttributes::new);
 * </pre>
 *
 * @param <Value> The type of value to create
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageObject.class)
public class Lazy<Value> implements Loadable, Unloadable
{
    public static <Type> Lazy<Type> of(final Factory<Type> factory)
    {
        return new Lazy<>(factory);
    }

    /** The value, or null if it doesn't exist */
    private Value value;

    /** The factory to create a new value */
    private final Factory<Value> factory;

    /**
     * @param factory A factory to create values when needed
     */
    protected Lazy(final Factory<Value> factory)
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
    public final Value get()
    {
        load();
        return value;
    }

    public boolean has()
    {
        return value != null;
    }

    @Override
    public synchronized void load()
    {
        if (value == null)
        {
            value = factory.newInstance();
        }
    }

    @Override
    public void unload()
    {
        clear();
    }
}
