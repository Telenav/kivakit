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

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFolder;
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

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

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
@SuppressWarnings("NullableProblems") @UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@LexakaiJavadoc(complete = true)
public class FolderList implements List<Folder>
{
    /**
     * <b>Not public API</b>
     */
    public static FolderList forVirtual(List<? extends FolderService> virtualFolders)
    {
        var folders = new FolderList();
        for (FolderService folder : virtualFolders)
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
        public Converter(Listener listener)
        {
            super(listener);
        }

        @Override
        protected FolderList onToValue(String value)
        {
            var folders = new FolderList();
            for (var path : value.split(","))
            {
                var folder = Folder.parseFolder(this, path);
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
    public boolean add(Folder folder)
    {
        return folders.add(folder);
    }

    @Override
    public void add(int index, Folder folder)
    {
        folders.add(index, folder);
    }

    @Override
    public boolean addAll(int index,
                          @NotNull Collection<? extends Folder> c)
    {
        return folders.addAll(index, c);
    }

    @Override
    public boolean addAll(Collection<? extends Folder> collection)
    {
        for (Folder folder : collection)
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
    public boolean contains(Object o)
    {
        return folders.contains(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c)
    {
        return folders.containsAll(c);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof FolderList)
        {
            FolderList that = (FolderList) object;
            return folders.equals(that.folders);
        }
        return false;
    }

    @Override
    public Folder get(int index)
    {
        return folders.get(index);
    }

    @Override
    public int hashCode()
    {
        return folders.hashCode();
    }

    @Override
    public int indexOf(Object o)
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
    public int lastIndexOf(Object o)
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
    public ListIterator<Folder> listIterator(int index)
    {
        return folders.listIterator(index);
    }

    public FolderList matching(Matcher<Folder> matcher)
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
    public boolean remove(Object o)
    {
        return folders.remove(o);
    }

    @Override
    public Folder remove(int index)
    {
        return folders.remove(index);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c)
    {
        return folders.removeAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<Folder> operator)
    {
        folders.replaceAll(operator);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c)
    {
        return folders.retainAll(c);
    }

    @Override
    public Folder set(int index, Folder folder)
    {
        return folders.set(index, folder);
    }

    @Override
    public int size()
    {
        return folders.size();
    }

    @Override
    public void sort(Comparator<? super Folder> c)
    {
        folders.sort(c);
    }

    @NotNull
    @Override
    public List<Folder> subList(int fromIndex, int toIndex)
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
    public <T> T[] toArray(@NotNull T[] array)
    {
       return unsupported();
    }
}
