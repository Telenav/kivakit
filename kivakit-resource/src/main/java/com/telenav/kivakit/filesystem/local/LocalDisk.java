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

import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.kernel.language.values.count.Bytes;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;

/**
 * Implementation of {@link DiskService} provider interface for the local filesystem.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@LexakaiJavadoc(complete = true)
@UmlNotPublicApi
public class LocalDisk implements DiskService
{
    private final LocalFolder root;

    public LocalDisk(LocalFolder folder)
    {
        // Go up the folder hierarchy until we find a partition mount point
        var at = folder;
        while (at.asJavaFile().getFreeSpace() == 0 && at.parent() != null)
        {
            at = at.parent();
        }
        root = at;
    }

    @Override
    public Bytes free()
    {
        return Bytes.bytes(root.asJavaFile().getFreeSpace());
    }

    @Override
    public LocalFolder root()
    {
        return root;
    }

    @Override
    public Bytes size()
    {
        return Bytes.bytes(root.asJavaFile().getTotalSpace());
    }

    @Override
    public String toString()
    {
        return root.toString();
    }

    @Override
    public Bytes usable()
    {
        return Bytes.bytes(root.asJavaFile().getUsableSpace());
    }
}
