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

package com.telenav.kivakit.settings.settings.stores;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.ObjectSerializers;
import com.telenav.kivakit.settings.project.lexakai.DiagramSettings;
import com.telenav.kivakit.settings.settings.Settings;
import com.telenav.kivakit.settings.settings.SettingsObject;
import com.telenav.kivakit.settings.settings.SettingsStore;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Set;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.settings.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.settings.settings.SettingsStore.AccessMode.UNLOAD;

/**
 * <p>
 * A folder containing settings objects defined by <i>.properties</i> files.
 * </p>
 *
 * <p>
 * A {@link FolderSettingsStore} can be created with {@link #of(Listener, Folder, ObjectSerializers)}. The specified
 * package should contain a set of settings files, each of which can be passed to the {@link ObjectSerializer} for the
 * file's extension to deserialize the object.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseResourceSettingsStore
 * @see ObjectSerializers
 * @see Settings
 * @see SettingsStore
 * @see SettingsObject
 * @see Folder
 */
@UmlClassDiagram(diagram = DiagramSettings.class)
public class FolderSettingsStore extends BaseResourceSettingsStore
{
    /**
     * @param folder The folder containing files specifying settings objects
     * @param serializers The {@link ObjectSerializers} to deserialize objects
     */
    public static FolderSettingsStore of(Listener listener, Folder folder, ObjectSerializers serializers)
    {
        return listener.listenTo(new FolderSettingsStore(folder, serializers));
    }

    /** The folder containing .properties files defining settings objects */
    private final Folder folder;

    /**
     * Serializers for reading files in the folder
     */
    private final ObjectSerializers serializers;

    /**
     * @param folder The folder containing .properties files specifying settings objects
     */
    protected FolderSettingsStore(Folder folder, ObjectSerializers serializers)
    {
        super(serializers);
        this.folder = folder;
        this.serializers = serializers;
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

        // Go through files in the folder
        for (var file : folder.files())
        {
            // get any serializer for the file extension,
            var serializer = serializers.serializer(file.extension());
            if (serializer != null)
            {
                objects.addIfNotNull(read(file));
            }
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
