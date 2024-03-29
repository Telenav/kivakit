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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.spi.DiskService;
import com.telenav.kivakit.resource.internal.lexakai.DiagramFileSystemService;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlNotPublicApi;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Audience.AUDIENCE_INTERNAL;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.value.count.Bytes.bytes;

/**
 * Implementation of {@link DiskService} provider interface for the local filesystem.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemService.class)
@UmlNotPublicApi
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED,
             audience = AUDIENCE_INTERNAL)
public class LocalDisk implements DiskService
{
    private final LocalFolder root;

    public LocalDisk(@NotNull LocalFolder folder)
    {
        // Go up the folder hierarchy until we find a partition mount point
        var at = folder;
        while (at.asJavaFile().getFreeSpace() == 0 && at.parentService() != null)
        {
            at = at.parentService();
        }
        root = at;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes free()
    {
        return bytes(root.asJavaFile().getFreeSpace());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalFolder root()
    {
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes size()
    {
        return bytes(root.asJavaFile().getTotalSpace());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return root.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Bytes usable()
    {
        return bytes(root.asJavaFile().getUsableSpace());
    }
}
