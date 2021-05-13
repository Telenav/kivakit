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

package com.telenav.kivakit.filesystem;

import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.UnaryOperator;

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
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@LexakaiJavadoc(complete = true)
public class FolderList implements List<Folder>
{
    /**
     * <b>Not public API</b>
     */
    public static FolderList forVirtual(final List<? extends FolderService> virtualFolders)
    {
        final var folders = new FolderList();
        for (final FolderService folder : virtualFolders)
        {
            folders.add(new Folder(folder));
        }
        return folders;
    }

    /**
     * Converts to and from folder lists separated by commas
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<FolderList>
    {
        public Converter(final Listener listener)
        {
            super(listener);
        }

        @Override
        protected FolderList onConvertToObject(final String value)
        {
            final var folders = new FolderList();
            for (final var path : value.split(","))
            {
                final var folder = Folder.parse(path);
                folders.add(folder);
            }
            return folders;
        }
    }

    @UmlAggregation
    private final List<Folder> folders = new ArrayList<>();

    public FolderList()
    {
    }

    @Override
    public boolean add(final Folder folder)
    {
        return folders.add(folder);
    }

    @Override
    public void add(final int index, final Folder folder)
    {
        folders.add(index, folder);
    }

    @Override
    public boolean addAll(final int index,
                          @NotNull final Collection<? extends Folder> c)
    {
        return folders.addAll(index, c);
    }

    @Override
    public boolean addAll(final Collection<? extends Folder> collection)
    {
        for (final Folder folder : collection)
        {
            add(folder);
        }
        return true;
    }

    public Set<Folder> asSet()
    {
        return new HashSet<>(this);
    }

    @Override
    public void clear()
    {
        folders.clear();
    }

    @Override
    public boolean contains(final Object o)
    {
        return folders.contains(o);
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c)
    {
        return folders.containsAll(c);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof FolderList)
        {
            final FolderList that = (FolderList) object;
            return folders.equals(that.folders);
        }
        return false;
    }

    @Override
    public Folder get(final int index)
    {
        return folders.get(index);
    }

    @Override
    public int hashCode()
    {
        return folders.hashCode();
    }

    @Override
    public int indexOf(final Object o)
    {
        return folders.indexOf(o);
    }

    @Override
    public boolean isEmpty()
    {
        return folders.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<Folder> iterator()
    {
        return folders.iterator();
    }

    @Override
    public int lastIndexOf(final Object o)
    {
        return folders.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<Folder> listIterator()
    {
        return folders.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<Folder> listIterator(final int index)
    {
        return folders.listIterator(index);
    }

    public FolderList matching(final Matcher<Folder> matcher)
    {
        final var folders = new FolderList();
        for (final var folder : this)
        {
            if (matcher.matches(folder))
            {
                folders.add(folder);
            }
        }
        return folders;
    }

    @Override
    public boolean remove(final Object o)
    {
        return folders.remove(o);
    }

    @Override
    public Folder remove(final int index)
    {
        return folders.remove(index);
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c)
    {
        return folders.removeAll(c);
    }

    @Override
    public void replaceAll(final UnaryOperator<Folder> operator)
    {
        folders.replaceAll(operator);
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c)
    {
        return folders.retainAll(c);
    }

    @Override
    public Folder set(final int index, final Folder folder)
    {
        return folders.set(index, folder);
    }

    @Override
    public int size()
    {
        return folders.size();
    }

    @Override
    public void sort(final Comparator<? super Folder> c)
    {
        folders.sort(c);
    }

    @NotNull
    @Override
    public List<Folder> subList(final int fromIndex, final int toIndex)
    {
        return folders.subList(fromIndex, toIndex);
    }

    @NotNull
    @Override
    public Object[] toArray()
    {
        return folders.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull final T[] array)
    {
        return folders.toArray(array);
    }
}
