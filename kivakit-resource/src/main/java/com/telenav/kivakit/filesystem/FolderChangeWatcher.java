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

import com.telenav.kivakit.collections.watcher.CollectionChangeListener;
import com.telenav.kivakit.collections.watcher.PeriodicCollectionChangeWatcher;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.HashSet;
import java.util.Set;

/**
 * Watches for changes to the contents of a {@link Folder}. {@link CollectionChangeListener}s are notified when changes
 * occur and can be added with {@link #addListener(CollectionChangeListener)} and removed with {@link
 * #removeListener(CollectionChangeListener)}.
 *
 * @author jonathanl (shibo)
 * @see PeriodicCollectionChangeWatcher
 */
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@UmlRelation(label = "watches", referent = Folder.class)
@LexakaiJavadoc(complete = true)
public class FolderChangeWatcher extends PeriodicCollectionChangeWatcher<FileSystemObject>
{
    private final Folder folder;

    public FolderChangeWatcher(Folder folder, Frequency frequency)
    {
        super(frequency);
        this.folder = folder;
    }

    @Override
    protected Time lastModified(FileSystemObject object)
    {
        return object.lastModified();
    }

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
