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
import com.telenav.kivakit.core.collections.iteration.Iterables;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.BaseResourceList;
import com.telenav.kivakit.resource.Extension;
import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemFile;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.ArrayList;
import java.util.List;

import static com.telenav.kivakit.filesystem.FilePath.filePath;

/**
 * A list of files with useful methods added. Additional methods include:
 *
 * <ul>
 *     <li>{@link #asJavaFiles()} - This file list as a list of {@link java.io.File} objects</li>
 *     <li>{@link #asSet()} - This list as a set</li>
 *     <li>{@link #first()} - The first file in this list</li>
 *     <li>{@link #largest()} - The largest file in this list</li>
 *     <li>{@link #matching(Matcher)} - The files in this list matching the given matcher</li>
 *     <li>{@link #smallest()} - The smallest file in this list</li>
 *     <li>{@link #sortedLargestToSmallest()} - This file list sorted</li>
 *     <li>{@link #sortedLargestToSmallest()} - This file list sorted</li>
 *     <li>{@link #sortedOldestToNewest()} ()} - This file list sorted</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@LexakaiJavadoc(complete = true)
public class FileList extends BaseResourceList<File> implements Iterable<File>
{
    public static FileList fileList(File... files)
    {
        return fileList(Iterables.iterable(files));
    }

    public static FileList fileList(Iterable<File> files)
    {
        return new FileList(files);
    }

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    public static FileList forServices(List<? extends FileService> fileServices)
    {
        var files = new FileList();
        for (var file : fileServices)
        {
            files.add(new File(file));
        }
        return files;
    }

    /**
     * Converts to and from {@link FileList}s
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<FileList>
    {
        private final Extension extension;

        public Converter(Listener listener, Extension extension)
        {
            super(listener);
            this.extension = extension;
        }

        @Override
        protected FileList onToValue(String value)
        {
            var files = new FileList();
            for (var path : value.split(","))
            {
                var file = File.parseFile(this, path);
                if (file.isFolder())
                {
                    files.addAll(file.asFolder().nestedFiles(extension.matcher()));
                }
                else
                {
                    if (file.fileName().endsWith(extension))
                    {
                        files.add(file);
                    }
                    else
                    {
                        warning("$ is not a $ file", file, extension);
                    }
                }
            }
            return files;
        }
    }

    public FileList()
    {
    }

    protected FileList(Iterable<File> that)
    {
        addAll(that);
    }

    @UmlExcludeMember
    public List<java.io.File> asJavaFiles()
    {
        var files = new ArrayList<java.io.File>();
        forEach(file -> files.add(file.asJavaFile()));
        return files;
    }


    @Override
    public File largest()
    {
        return (File) super.largest();
    }

    @Override
    public FileList matching(Matcher<File> matcher)
    {
        return (FileList) super.matching(matcher);
    }

    @Override
    public File smallest()
    {
        return (File) super.smallest();
    }

    @Override
    public FileList sortedLargestToSmallest()
    {
        return (FileList) super.sortedLargestToSmallest();
    }

    @Override
    @SuppressWarnings("UnusedReturnValue")
    public FileList sortedOldestToNewest()
    {
        return (FileList) super.sortedOldestToNewest();
    }

    @Override
    protected File newResource(ResourcePath path)
    {
        return File.file(filePath(path));
    }

    @Override
    protected BaseResourceList<File> newResourceList()
    {
        return new FileList();
    }
}
