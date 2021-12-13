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
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Set;

import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.configuration.settings.SettingsStore.AccessMode.UNLOAD;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;
import static com.telenav.kivakit.resource.path.Extension.JSON;
import static com.telenav.kivakit.resource.path.Extension.PROPERTIES;

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
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD);
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
        var objects = new ObjectSet<SettingsObject>();

        // Go through .properties files in the folder
        for (var at : folder.files().matching(PROPERTIES.fileMatcher()))
        {
            // and add a settings objects for each file
            objects.addIfNotNull(loadFromProperties(at));
        }

        // Go through .json files in the folder
        for (var at : folder.files().matching(JSON.fileMatcher()))
        {
            // and add a settings objects for each file
            objects.addIfNotNull(loadFromJson(at));
        }

        return objects;
    }

    @Override
    public boolean onSave(SettingsObject object)
    {
        return unsupported();
    }

    @Override
    protected boolean onDelete(SettingsObject object)
    {
        return unsupported();
    }
}
