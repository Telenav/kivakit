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

package com.telenav.kivakit.filesystem.local;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.filesystem.spi.FileService;
import com.telenav.kivakit.filesystem.spi.FileSystemService;
import com.telenav.kivakit.filesystem.spi.FolderService;
import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

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
@LexakaiJavadoc(complete = true)
public class LocalFileSystemService implements FileSystemService
{
    @Override
    public boolean accepts(final FilePath path)
    {
        if (path.hasScheme())
        {
            return false;
        }
        try
        {
            Paths.get(path.toString());
            return true;
        }
        catch (final InvalidPathException e)
        {
            return false;
        }
    }

    @Override
    public DiskService diskService(final FilePath path)
    {
        return new LocalDisk(new LocalFolder(normalize(path)));
    }

    @Override
    public FileService fileService(final FilePath path)
    {
        return new LocalFile(normalize(path));
    }

    @Override
    public FolderService folderService(final FilePath path)
    {
        return new LocalFolder(normalize(path));
    }

    private FilePath normalize(final FilePath path)
    {
        if (path.startsWith("~"))
        {
            return FilePath.parseFilePath(Strings.replace(path.toString(), 0, 1, Folder.userHome().toString()));
        }
        return path;
    }
}
