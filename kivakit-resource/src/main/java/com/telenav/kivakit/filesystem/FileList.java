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
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.BaseResourceList;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.ResourcePathed;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFile;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.core.collections.iteration.Iterables.iterable;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static com.telenav.kivakit.filesystem.File.file;
import static com.telenav.kivakit.filesystem.FilePath.filePath;

/**
 * A list of files with useful methods added
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #fileList(File...)}</li>
 *     <li>{@link #fileList(Iterable)}</li>
 * </ul>
 *
 * <p><b>Adding</b></p>
 *
 * <ul>
 *     <li>{@link BaseResourceList#add(Resource)}</li>
 *     <li>{@link BaseResourceList#add(int, Resource)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asJavaFiles()} - This file list as a list of {@link java.io.File} objects</li>
 *     <li>{@link #asSet()}</li>
 * </ul>
 *
 * <p><b>Retrieval</b></p>
 *
 * <ul>
 *     <li>{@link #first()} - The first file in this list</li>
 *     <li>{@link #largest()} - The largest file in this list</li>
 *     <li>{@link #last()}</li>
 *     <li>{@link #matching(Matcher)} - The files in this list matching the given matcher</li>
 *     <li>{@link #smallest()} - The smallest file in this list</li>
 *     <li>{@link #sortedLargestToSmallest()} - This file list sorted</li>
 *     <li>{@link #sortedLargestToSmallest()} - This file list sorted</li>
 *     <li>{@link #sortedOldestToNewest()} ()} - This file list sorted</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public class FileList extends BaseResourceList<File> implements Iterable<File>
{
    /**
     * Creates a list of files from the given arguments
     *
     * @param files The files
     * @return The {@link FileList}
     */
    public static FileList fileList(@NotNull File... files)
    {
        return fileList(iterable(files));
    }

    /**
     * Creates a list of files from the given arguments
     *
     * @param files The files
     * @return The {@link FileList}
     */
    public static FileList fileList(@NotNull Iterable<File> files)
    {
        return new FileList(files);
    }

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    public static FileList fileListForServices(List<? extends FileService> fileServices)
    {
        var files = new FileList();
        for (var file : fileServices)
        {
            files.add(new File(file));
        }
        return files;
    }

    public FileList()
    {
    }

    protected FileList(@NotNull Iterable<File> that)
    {
        addAll(that);
    }

    /**
     * Returns a list of Java files
     */
    @UmlExcludeMember
    public ObjectList<java.io.File> asJavaFiles()
    {
        var files = new ObjectList<java.io.File>();
        forEach(file -> files.add(file.asJavaFile()));
        return files;
    }

    @Override
    public FileList copy()
    {
        return (FileList) super.copy();
    }

    /**
     * Returns the largest file in this list
     */
    @Override
    public File largest()
    {
        return (File) super.largest();
    }

    /**
     * Returns the files in this list that match the given matcher
     */
    @Override
    public FileList matching(@NotNull Matcher<File> matcher)
    {
        return (FileList) super.matching(matcher);
    }

    /**
     * Returns the smallest file in this list
     */
    @Override
    public File smallest()
    {
        return (File) super.smallest();
    }

    /**
     * Returns a list of files sorted from largest to smallest
     */
    @Override
    public FileList sortedLargestToSmallest()
    {
        return (FileList) super.sortedLargestToSmallest();
    }

    /**
     * Returns a list of files sorted from oldest to newest
     */
    @Override
    @SuppressWarnings("UnusedReturnValue")
    public FileList sortedOldestToNewest()
    {
        return (FileList) super.sortedOldestToNewest();
    }

    @Override
    public FileList with(ResourceFolder<?> folder, Filter<ResourcePathed> filter)
    {
        return (FileList) super.with(folder, filter);
    }

    @Override
    public FileList with(Iterable<File> that)
    {
        return (FileList) super.with(that);
    }

    @Override
    public FileList with(Collection<File> that)
    {
        return (FileList) super.with(that);
    }

    @Override
    public FileList with(File file)
    {
        return (FileList) super.with(file);
    }

    @Override
    public FileList with(File... value)
    {
        return (FileList) super.with(value);
    }

    @Override
    public FileList without(Filter<ResourcePathed> filter)
    {
        return (FileList) super.without(filter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected File newResource(@NotNull ResourcePath path)
    {
        return file(throwingListener(), filePath(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BaseResourceList<File> newResourceList()
    {
        return new FileList();
    }
}
