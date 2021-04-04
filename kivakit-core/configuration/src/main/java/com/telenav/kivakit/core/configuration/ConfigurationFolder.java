////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.resource.path.Extension;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of configuration objects stored in a folder. The folder contains a set of .properties files that are used to
 * instantiate and populate the configuration objects.
 * <p>
 * <i>See the superclass {@link ConfigurationSet} for details on how this works.</i>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ClassEscapesDefinedScope")
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class ConfigurationFolder extends ConfigurationSet
{
    private final Folder folder;

    /**
     * @param folder The folder containing .properties files specifying configuration objects
     */
    public ConfigurationFolder(final Folder folder)
    {
        this.folder = folder;
    }

    @Override
    public String name()
    {
        return "[ConfigurationFolder folder = " + folder.path() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Set<ConfigurationEntry> onLoadConfigurations()
    {
        final Set<ConfigurationEntry> configurations = new HashSet<>();

        // Go through properties files in the folder
        for (final var file : folder.files().matching(Extension.PROPERTIES.matcher()))
        {
            // and add a configuration entry for each file
            configurations.add(internalLoadConfiguration(file));
        }

        return configurations;
    }
}
