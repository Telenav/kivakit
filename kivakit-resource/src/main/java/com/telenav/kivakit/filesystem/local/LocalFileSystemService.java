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

package com.telenav.kivakit.filesystem.local;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.string.Strings;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.filesystem.Folders;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Implementation of {@link FileSystemService} provider interface for the local filesystem.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlRelation(label = "creates", referent = LocalDisk.class)
@UmlRelation(label = "creates", referent = LocalFile.class)
@UmlRelation(label = "creates", referent = LocalFolder.class)
@UmlNotPublicApi
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE,
             audience = AUDIENCE_INTERNAL)
public class LocalFileSystemService implements FileSystemService
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(@NotNull FilePath path)
    {
        if (path.schemes().equals(StringList.stringList("file")))
        {
            return true;
        }

        if (path.hasScheme())
        {
            return false;
        }

        try
        {
            Paths.get(path.toString());
            return true;
        }
        catch (InvalidPathException e)
        {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull DiskService diskService(@NotNull FilePath path)
    {
        return new LocalDisk(new LocalFolder(normalize(path)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull FileService fileService(@NotNull FilePath path)
    {
        return new LocalFile(normalize(path));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull FolderService folderService(@NotNull FilePath path)
    {
        return new LocalFolder(normalize(path));
    }

    private FilePath normalize(FilePath path)
    {
        if (path.startsWith("~"))
        {
            return FilePath.parseFilePath(Listener.consoleListener(), Strings.replace(path.toString(), 0, 1,
                    Folders.userHome().toString())).withoutSchemes();
        }
        return path.withoutSchemes();
    }
}
