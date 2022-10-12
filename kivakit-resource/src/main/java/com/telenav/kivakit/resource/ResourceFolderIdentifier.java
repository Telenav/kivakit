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

package com.telenav.kivakit.resource;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourceService;
import com.telenav.kivakit.resource.packages.Package;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.resource.ResourceFolder.resolveResourceFolder;

/**
 * An identifier for a {@link ResourceFolder} implementation, including either a {@link Package} or a {@link Folder}.
 * The {@link ResourceFolder} can be resolved by searching for an implementation which accepts the identifier, with
 * {@link #resolve(Listener)} or {@link ResourceFolder#resolveResourceFolder(Listener, ResourceFolderIdentifier)}.
 *
 * @author jonathanl (shibo)
 * @see ResourceFolder
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class ResourceFolderIdentifier
{
    private final String identifier;

    public ResourceFolderIdentifier(@NotNull String identifier)
    {
        this.identifier = identifier;
    }

    public String identifier()
    {
        return identifier;
    }

    public ResourceFolder<?> resolve(@NotNull Listener listener)
    {
        return resolveResourceFolder(listener, this);
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
