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

import com.telenav.kivakit.resource.path.FilePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * Service provider interface (SPI) for filesystems. FileSystemService is further broken down into SPIs for disk, folder
 * and file services. All of these interfaces must be implemented to provide a filesystem service.
 *
 * @author jonathanl (shibo)
 * @see DiskService
 * @see FileService
 * @see FolderService
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@LexakaiJavadoc(complete = true)
public interface FileSystemService
{
    /**
     * @return Determines if the given path is accepted as a path for by this filesystem service
     */
    boolean accepts(FilePath path);

    /**
     * @return The {@link DiskService} for the given path
     */
    @UmlRelation(label = "provides")
    DiskService diskService(FilePath path);

    /**
     * @return The {@link FileService} for the given path
     */
    @UmlRelation(label = "provides")
    FileService fileService(FilePath path);

    /**
     * @return The {@link FolderService} for the given path
     */
    @UmlRelation(label = "provides")
    FolderService folderService(FilePath path);
}
