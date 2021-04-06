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

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.collections.watcher.PeriodicCollectionChangeWatcher;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.core.kernel.language.time.Time;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFolder;

import java.util.HashSet;
import java.util.Set;

@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@UmlRelation(label = "watches", referent = Folder.class)
public class FolderChangeWatcher extends PeriodicCollectionChangeWatcher<FileSystemObject>
{
    private final Folder folder;

    public FolderChangeWatcher(final Folder folder, final Frequency frequency)
    {
        super(frequency);
        this.folder = folder;
    }

    @Override
    protected Time lastModified(final FileSystemObject object)
    {
        return object.lastModified();
    }

    @Override
    protected Set<FileSystemObject> objects()
    {
        final Set<FileSystemObject> objects = new HashSet<>();
        objects.addAll(folder.files());
        objects.addAll(folder.folders());
        for (final var object : objects)
        {
            trace("Watcher sees $ modified at $", object, lastModified(object));
        }
        return objects;
    }
}
