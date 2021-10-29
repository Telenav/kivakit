////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.configuration.settings;

import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.HashSet;
import java.util.Set;

/**
 * A set of configuration objects stored in a folder. The folder contains a set of .properties files that are used to
 * instantiate and populate the configuration objects.
 * <p>
 * <i>See the superclass {@link Settings} for details on how this works.</i>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ClassEscapesDefinedScope")
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class SettingsFolder extends Settings
{
    /**
     * @param folder The folder containing .properties files specifying configuration objects
     */
    public static SettingsFolder of(Folder folder)
    {
        return new SettingsFolder(folder);
    }

    private final Folder folder;

    /**
     * @param folder The folder containing .properties files specifying configuration objects
     */
    protected SettingsFolder(Folder folder)
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
    protected Set<Entry> onLoad()
    {
        Set<Entry> entries = new HashSet<>();

        // Go through properties files in the folder
        for (var file : folder.files().matching(Extension.PROPERTIES.fileMatcher()))
        {
            // and add a configuration entry for each file
            entries.add(internalLoadConfiguration(file));
        }

        return entries;
    }
}
