////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.objects.reference;

import com.telenav.kivakit.core.kernel.language.time.Duration;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Holds a reference to a given object and a creation time. If the reference gets to be too old, the object is recreated
 * by a call to {@link #onNewObject()}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageObject.class)
public abstract class ExpiringReference<T>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private Time created;

    private T object;

    private final Duration maximumAge;

    /**
     * @param maximumAge Maximum allowable reference age (before a new object is created)
     */
    protected ExpiringReference(final Duration maximumAge)
    {
        this.maximumAge = maximumAge;
    }

    public synchronized T get()
    {
        // If the object hasn't yet been created or the current object is too old,
        if (object == null || created.isOlderThan(maximumAge))
        {
            // create a new object
            newObject();
        }
        return object;
    }

    public boolean isNull()
    {
        return object == null;
    }

    protected void newObject()
    {
        try
        {
            // Create new object outside synchronized block
            final var newObject = onNewObject();

            // If the new object was successfully created,
            if (newObject != null)
            {
                // update object reference and created time
                synchronized (this)
                {
                    object = newObject;
                    created = Time.now();
                }
            }
            else
            {
                LOGGER.problem("onNewObject() returned null reference");
            }
        }
        catch (final Exception e)
        {
            LOGGER.problem(e, "Couldn't create new reference");
        }
    }

    protected abstract T onNewObject();
}
