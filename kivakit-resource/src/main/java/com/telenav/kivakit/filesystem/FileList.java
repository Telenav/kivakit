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
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class FileList extends ObjectList<File>
{
    public static FileList files(File... files)
    {
        var list = new FileList();
        list.addAll(Arrays.asList(files));
        return list;
    }

    /**
     * <b>Not public API</b>
     */
    @UmlExcludeMember
    public static FileList forServices(List<? extends FileService> virtualFiles)
    {
        var files = new FileList();
        for (var file : virtualFiles)
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
                    files.addAll(file.asFolder().nestedFiles(extension.fileMatcher()));
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

    FileList(FileList that)
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
    public Set<File> asSet()
    {
        return new HashSet<>(this);
    }

    public byte[] digest()
    {
        MessageDigest digester;
        try
        {
            digester = MessageDigest.getInstance("MD5");
            var builder = new StringBuilder();
            for (var file : this)
            {
                builder.append("[")
                        .append(file.path().absolute())
                        .append(":")
                        .append(file.created())
                        .append(":")
                        .append(file.lastModified())
                        .append(":")
                        .append(file.sizeInBytes())
                        .append("]");
            }
            return digester.digest(builder.toString().getBytes());
        }
        catch (NoSuchAlgorithmException ignored)
        {
            // Not possible
        }
        return null;
    }

    public File largest()
    {
        File largest = null;
        for (var file : this)
        {
            if (largest == null || file.isLargerThan(largest))
            {
                largest = file;
            }
        }
        return largest;
    }

    @Override
    public FileList matching(Matcher<File> matcher)
    {
        var files = new FileList();
        for (var file : this)
        {
            if (matcher.matches(file))
            {
                files.add(file);
            }
        }
        return files;
    }

    public File smallest()
    {
        File smallest = null;
        for (var file : this)
        {
            if (smallest == null || file.isSmallerThan(smallest))
            {
                smallest = file;
            }
        }
        return smallest;
    }

    public FileList sortedLargestToSmallest()
    {
        var sorted = new FileList(this);
        sorted.sort((a, b) ->
        {
            if (a.isLargerThan(b))
            {
                return -1;
            }
            if (b.isLargerThan(a))
            {
                return 1;
            }
            return 0;
        });
        return sorted;
    }

    @SuppressWarnings("UnusedReturnValue")
    public FileList sortedOldestToNewest()
    {
        var sorted = new FileList(this);
        sorted.sort((a, b) ->
        {
            if (a.isOlderThan(b))
            {
                return -1;
            }
            if (b.isOlderThan(a))
            {
                return 1;
            }
            return 0;
        });
        return sorted;
    }

    public Bytes totalSize()
    {
        var bytes = Bytes._0;
        for (var file : this)
        {
            bytes = bytes.plus(file.sizeInBytes());
        }
        return bytes;
    }
}
