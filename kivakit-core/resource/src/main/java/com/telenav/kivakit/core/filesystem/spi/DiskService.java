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

package com.telenav.kivakit.core.filesystem.spi;

import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A service provider interface (SPI) for filesystem disks. Implementers provide:
 *
 * <ul>
 *     <li>{@link #free()} - Number of bytes free on the disk</li>
 *     <li>{@link #size()} - The total size of the disk</li>
 *     <li>{@link #usable()} - The number of bytes on the disk that are usable</li>
 *     <li>{@link #root()} - The root folder of the disk</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see FileSystemService
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@LexakaiJavadoc(complete = true)
public interface DiskService
{
    /**
     * @return Free space on this disk
     */
    Bytes free();

    /**
     * @return Percentage of this disk that is free
     */
    default Percent percentFree()
    {
        return free().percentOf(size());
    }

    /**
     * @return Percentage of this disk that is usable
     */
    default Percent percentUsable()
    {
        return usable().percentOf(size());
    }

    /**
     * @return Root folder on this disk
     */
    FolderService root();

    /**
     * @return Total size of this disk
     */
    Bytes size();

    /**
     * @return Usable space on this disk
     */
    Bytes usable();
}
