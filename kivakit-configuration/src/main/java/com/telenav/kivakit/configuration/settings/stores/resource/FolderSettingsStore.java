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

package com.telenav.kivakit.configuration.settings.stores.resource;

import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.configuration.settings.SettingsObject;
import com.telenav.kivakit.configuration.settings.SettingsStore;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.configuration.settings.SettingsStore.Access.ADD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.Access.CLEAR;
import static com.telenav.kivakit.configuration.settings.SettingsStore.Access.LOAD;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * <p>
 * A folder containing settings objects defined by <i>.properties</i> files.
 * </p>
 *
 * <p>
 * A {@link FolderSettingsStore} can be created with {@link #of(Folder)}. The specified folder must contain a set of
 * <i>.properties</i> files, each of which can be used to instantiate and populate a single settings object.
 * <i>See {@link BaseResourceSettingsStore} for details on how this works.</i>
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseResourceSettingsStore
 * @see Settings
 * @see SettingsStore
 * @see SettingsObject
 * @see Folder
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class FolderSettingsStore extends BaseResourceSettingsStore
{
    /**
     * @param folder The folder containing .properties files specifying settings objects
     */
    public static FolderSettingsStore of(Listener listener, Folder folder)
    {
        return listener.listenTo(new FolderSettingsStore(folder));
    }

    /** The folder containing .properties files defining settings objects */
    private final Folder folder;

    /**
     * @param folder The folder containing .properties files specifying settings objects
     */
    protected FolderSettingsStore(Folder folder)
    {
        this.folder = folder;
    }

    @Override
    public Set<Access> access()
    {
        return Set.of(ADD, CLEAR, LOAD);
    }

    @Override
    public String name()
    {
        return "[FolderSettingsStore folder = " + folder.path() + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public Set<SettingsObject> onLoad()
    {
        Set<SettingsObject> entries = new HashSet<>();

        // Go through properties files in the folder
        for (var file : folder.files().matching(Extension.PROPERTIES.fileMatcher()))
        {
            // and add a configuration entry for each file
            entries.add(loadFromProperties(file));
        }

        return entries;
    }

    @Override
    public boolean onSave(SettingsObject object)
    {
        return unsupported();
    }
}
