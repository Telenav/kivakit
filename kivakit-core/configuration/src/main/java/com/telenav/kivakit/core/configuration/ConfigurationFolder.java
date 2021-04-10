////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.resource.path.Extension;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

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
        for (final var file : folder.files().matching(Extension.PROPERTIES.fileMatcher()))
        {
            // and add a configuration entry for each file
            configurations.add(internalLoadConfiguration(file));
        }

        return configurations;
    }
}
