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

package com.telenav.kivakit.settings.stores;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.serialization.ObjectSerializer;
import com.telenav.kivakit.resource.serialization.ObjectSerializerRegistry;
import com.telenav.kivakit.settings.SettingsObject;
import com.telenav.kivakit.settings.SettingsRegistry;
import com.telenav.kivakit.settings.SettingsStore;
import com.telenav.kivakit.settings.internal.lexakai.DiagramSettings;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABILITY_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.unsupported;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.DELETE;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.INDEX;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.LOAD;
import static com.telenav.kivakit.settings.SettingsStore.AccessMode.UNLOAD;
import static java.util.Collections.emptySet;

/**
 * <p>
 * A read-only folder containing settings objects defined by <i>.properties</i> files.
 * </p>
 *
 * <p>
 * A {@link ResourceFolderSettingsStore} can be created with
 * {@link ResourceFolderSettingsStore#ResourceFolderSettingsStore(Listener, ResourceFolder)}. The specified package
 * should contain a set of settings files, each of which can be passed to the {@link ObjectSerializer} for the file's
 * extension to deserialize the object. Object serializers are located with the {@link ObjectSerializerRegistry} object found
 * in the global {@link Registry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see BaseResourceSettingsStore
 * @see ObjectSerializerRegistry
 * @see SettingsRegistry
 * @see SettingsStore
 * @see SettingsObject
 * @see Folder
 */
@UmlClassDiagram(diagram = DiagramSettings.class)
@CodeQuality(stability = STABILITY_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class ResourceFolderSettingsStore extends BaseResourceSettingsStore
{
    /** The folder containing .properties files defining settings objects */
    private final ResourceFolder<?> folder;

    /**
     * @param folder The folder containing files specifying settings objects
     */
    public ResourceFolderSettingsStore(Listener listener, ResourceFolder<?> folder)
    {
        listener.listenTo(this);

        this.folder = folder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AccessMode> accessModes()
    {
        return Set.of(INDEX, DELETE, UNLOAD, LOAD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String name()
    {
        return format("${class} $", folder.getClass(), folder.path());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    public Set<SettingsObject> onLoad()
    {
        var objects = new ObjectSet<SettingsObject>();

        var serializers = require(ObjectSerializerRegistry.class, ObjectSerializerRegistry::new);
        if (serializers.serializers().isEmpty())
        {
            problem("Cannot load settings: no registered object serializers for $", this);
            return emptySet();
        }

        // Go through files in the folder
        for (var resource : folder.resources())
        {
            // get any serializer for the file extension,
            var serializer = serializers.serializer(resource.extension());
            if (serializer != null)
            {
                objects.addIfNotNull(read(resource));
            }
        }

        return objects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onSave(SettingsObject object)
    {
        return unsupported();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean onDelete(SettingsObject object)
    {
        return unsupported();
    }
}
