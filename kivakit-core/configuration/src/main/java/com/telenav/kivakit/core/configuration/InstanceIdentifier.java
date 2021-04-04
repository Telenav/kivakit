////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.configuration.lookup.Lookup;
import com.telenav.kivakit.core.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.core.configuration.project.lexakai.diagrams.DiagramLookup;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;
import com.telenav.kivakit.core.kernel.language.values.identifier.StringIdentifier;

/**
 * An identifier for a particular instance of a class of configuration object stored in a {@link ConfigurationSet}. Also
 * used by {@link Lookup} when locating an object by class which has more than one instance.
 *
 * @author jonathanl (shibo)
 * @see Lookup
 * @see ConfigurationSet
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class)
@UmlClassDiagram(diagram = DiagramLookup.class)
@UmlExcludeSuperTypes
public class InstanceIdentifier extends StringIdentifier
{
    /** Identifies the one and only instance of a singleton */
    public static final InstanceIdentifier SINGLETON = new InstanceIdentifier("[SINGLETON]");

    public InstanceIdentifier(final Class<?> value)
    {
        super(value.getSimpleName());
    }

    public InstanceIdentifier(final Enum<?> value)
    {
        super(value.name());
    }

    public InstanceIdentifier(final String string)
    {
        super(string);
    }
}
