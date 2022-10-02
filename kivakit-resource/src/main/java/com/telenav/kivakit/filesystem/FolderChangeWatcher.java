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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.collections.watcher.CollectionChangeListener;
import com.telenav.kivakit.collections.watcher.PeriodicCollectionChangeWatcher;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Watches for changes to the contents of a {@link Folder}. {@link CollectionChangeListener}s are notified when changes
 * occur. Listeners can be added with {@link #addListener(CollectionChangeListener)} and removed with
 * {@link #removeListener(CollectionChangeListener)}.
 *
 * <p><b>Collection Change Listeners</b></p>
 *
 * <ul>
 *     <li>{@link #addListener(CollectionChangeListener)}</li>
 *     <li>{@link #removeListener(CollectionChangeListener)}</li>
 * </ul>
 *
 * <p><b>Protected Callbacks</b></p>
 *
 * <ul>
 *     <li>{@link #onAdded(Object)}</li>
 *     <li>{@link #onModified(Object)}</li>
 *     <li>{@link #onRemoved(Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see PeriodicCollectionChangeWatcher
 */
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@UmlRelation(label = "watches", referent = Folder.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE)
public class FolderChangeWatcher extends PeriodicCollectionChangeWatcher<FileSystemObject>
{
    /** The folder to watch */
    private final Folder folder;

    /**
     * Watches the given folder for changes
     *
     * @param folder The folder to watch
     * @param frequency The frequency to inspect the folder at
     */
    public FolderChangeWatcher(@NotNull Folder folder, @NotNull Frequency frequency)
    {
        super(frequency);
        this.folder = folder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Time lastModified(@NotNull FileSystemObject object)
    {
        return object.lastModified();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Set<FileSystemObject> objects()
    {
        Set<FileSystemObject> objects = new HashSet<>();
        objects.addAll(folder.files());
        objects.addAll(folder.folders());
        for (var object : objects)
        {
            trace("Watcher sees $ modified at $", object, lastModified(object));
        }
        return objects;
    }
}
