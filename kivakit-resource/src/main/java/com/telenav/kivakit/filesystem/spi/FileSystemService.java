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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.filesystem.FilePath;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_SERVICE_PROVIDER_INTERFACE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Service provider interface (SPI) for filesystems. FileSystemService is further broken down into SPIs for disk, folder
 * and file services. All of these interfaces must be implemented to provide a filesystem service.
 *
 * @author jonathanl (shibo)
 * @see DiskService
 * @see FileService
 * @see FolderService
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_SERVICE_PROVIDER_INTERFACE)
public interface FileSystemService
{
    /**
     * Returns determines if the given path is accepted as a path for by this filesystem service
     */
    boolean accepts(@NotNull FilePath path);

    /**
     * Returns the {@link DiskService} for the given path
     */
    @UmlRelation(label = "provides")
    @NotNull
    DiskService diskService(@NotNull FilePath path);

    /**
     * Returns the {@link FileService} for the given path
     */
    @UmlRelation(label = "provides")
    @NotNull
    FileService fileService(@NotNull FilePath path);

    /**
     * Returns the {@link FolderService} for the given path
     */
    @UmlRelation(label = "provides")
    @NotNull
    FolderService folderService(@NotNull FilePath path);
}
