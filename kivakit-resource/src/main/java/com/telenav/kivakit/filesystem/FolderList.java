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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A list of folders with additional useful methods, including:
 *
 * <ul>
 *     <li>{@link #asSet()} - This list as a set</li>
 *     <li>{@link #matching(Matcher)} - The folders in this list that match the given matcher</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class FolderList extends ObjectList<Folder>
{
    /**
     * <b>Not public API</b>
     */
    public static FolderList folderList(@NotNull List<? extends FolderService> folderServices)
    {
        var folders = new FolderList();
        for (FolderService folder : folderServices)
        {
            folders.add(new Folder(folder));
        }
        return folders;
    }

    public FolderList()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Folder> asSet()
    {
        return new HashSet<>(this);
    }

    @Override
    public FolderList copy()
    {
        return (FolderList) super.copy();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FolderList matching(@NotNull Matcher<Folder> matcher)
    {
        var folders = new FolderList();
        for (var folder : this)
        {
            if (matcher.matches(folder))
            {
                folders.add(folder);
            }
        }
        return folders;
    }

    @Override
    public FolderList with(Folder folder)
    {
        return (FolderList) super.with(folder);
    }

    @Override
    public FolderList with(Folder[] value)
    {
        return (FolderList) super.with(value);
    }

    @Override
    public FolderList with(Collection<Folder> value)
    {
        return (FolderList) super.with(value);
    }

    @Override
    public FolderList with(Iterable<Folder> value)
    {
        return (FolderList) super.with(value);
    }

    @Override
    public FolderList without(Collection<Folder> that)
    {
        return (FolderList) super.without(that);
    }

    @Override
    public FolderList without(Folder folder)
    {
        return (FolderList) super.without(folder);
    }

    @Override
    public FolderList without(Matcher<Folder> matcher)
    {
        return (FolderList) super.without(matcher);
    }

    @Override
    public FolderList without(Folder[] that)
    {
        return (FolderList) super.without(that);
    }

    @Override
    protected FolderList onNewList()
    {
        return new FolderList();
    }
}
