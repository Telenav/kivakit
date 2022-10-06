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

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.core.value.level.Percent;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemFolder;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Represents a logical disk where folders and files are stored. Note that not all filesystems have a disk. For example,
 * HDFS and S3 are filesystems that are cloud-based and not associated with a single disk or computer. Provides methods
 * to get the root folder and to determine how large the disk is and how much space is left.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Disk
{
    /** The disk service provider */
    private final DiskService disk;

    @UmlExcludeMember
    public Disk(@NotNull DiskService disk)
    {
        this.disk = disk;
    }

    /**
     * Returns space free on this disk in {@link Bytes}
     */
    public Bytes free()
    {
        return disk.free();
    }

    /**
     * Returns the percentage of the disk that is free
     */
    public Percent percentFree()
    {
        return disk.percentFree();
    }

    /**
     * Returns the percentage of this disk that can be written to, taking into account permissions and other factors
     */
    public Percent percentUsable()
    {
        return disk.percentUsable();
    }

    /**
     * Returns the root folder of this disk
     */
    public Folder root()
    {
        return new Folder(disk.root());
    }

    /**
     * Returns the total size of this disk
     */
    public Bytes size()
    {
        return disk.size();
    }

    @Override
    public String toString()
    {
        return disk.toString();
    }

    /**
     * Returns the amount of usable free space on this disk, taking into account write permissions and other factors
     */
    public Bytes usable()
    {
        return disk.usable();
    }
}
