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

package com.telenav.kivakit.core.filesystem;

import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFolder;
import com.telenav.kivakit.core.filesystem.spi.DiskService;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.kernel.language.values.level.Percent;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

@UmlClassDiagram(diagram = DiagramFileSystemFolder.class)
public class Disk
{
    private final DiskService disk;

    @UmlExcludeMember
    public Disk(final DiskService disk)
    {
        this.disk = disk;
    }

    public Bytes free()
    {
        return disk.free();
    }

    public Percent percentFree()
    {
        return disk.percentFree();
    }

    public Percent percentUsable()
    {
        return disk.percentUsable();
    }

    public Folder root()
    {
        return new Folder(disk.root());
    }

    public Bytes size()
    {
        return disk.size();
    }

    @Override
    public String toString()
    {
        return disk.toString();
    }

    public Bytes usable()
    {
        return disk.usable();
    }
}
