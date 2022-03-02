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

package com.telenav.kivakit.filesystem.spi;

import com.telenav.kivakit.resource.WritableResource;
import com.telenav.kivakit.resource.project.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A service provider interface (SPI) for filesystem files. Adds {@link #renameTo(FileService)} to the methods required
 * by {@link FileSystemObjectService}.
 *
 * @author jonathanl (shibo)
 * @see FileSystemObjectService
 * @see FileSystemService
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@LexakaiJavadoc(complete = true)
public interface FileService extends
        FileSystemObjectService,
        WritableResource
{
    default java.io.File asJavaFile()
    {
        return null;
    }

    @Override
    default boolean isRemote()
    {
        return FileSystemObjectService.super.isRemote();
    }

    /**
     * @param that The file to rename to
     * @return True if the file was renamed
     */
    boolean renameTo(FileService that);
}
