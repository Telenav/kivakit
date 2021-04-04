////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection.populator;

import com.telenav.kivakit.core.kernel.language.reflection.Type;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyFilter;
import com.telenav.kivakit.core.kernel.language.reflection.property.PropertyValueSource;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Populates object properties given a property filter and a source of property values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class ObjectPopulator
{
    private final PropertyValueSource source;

    private final Listener listener;

    private final PropertyFilter filter;

    public ObjectPopulator(final Listener listener, final PropertyFilter filter, final PropertyValueSource source)
    {
        this.listener = listener;
        this.filter = filter;
        this.source = source;
    }

    /**
     * Populates the given object using the values obtained from the property value source
     *
     * @param object The object to populate
     * @return The populated object (for method chaining)
     */
    public <T> T populate(final T object)
    {
        // Go through each property on the object
        for (final var property : Type.of(object).properties(filter))
        {
            // Get any value for the given property
            final var value = source.valueFor(property);

            // If the value is non-null,
            if (value != null)
            {
                // set the property value
                final var message = property.set(listener, object, value);

                // and if something went wrong,
                if (message instanceof Problem)
                {
                    // notify any listeners
                    listener.receive(message);
                }
            }
            else
            {
                listener.warning("No value found for property: $", property.name());
            }
        }
        return object;
    }
}
