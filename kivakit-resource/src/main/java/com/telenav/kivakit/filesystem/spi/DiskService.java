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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.ApiType.SERVICE_PROVIDER_INTERFACE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

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
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE,
            type = SERVICE_PROVIDER_INTERFACE)
public interface DiskService
{
    /**
     * Returns free space on this disk
     */
    Bytes free();

    /**
     * Returns percentage of this disk that is free
     */
    default Percent percentFree()
    {
        return free().percentOf(size());
    }

    /**
     * Returns percentage of this disk that is usable
     */
    default Percent percentUsable()
    {
        return usable().percentOf(size());
    }

    /**
     * Returns root folder on this disk
     */
    FolderService root();

    /**
     * Returns total size of this disk
     */
    Bytes size();

    /**
     * Returns usable space on this disk
     */
    Bytes usable();
}
