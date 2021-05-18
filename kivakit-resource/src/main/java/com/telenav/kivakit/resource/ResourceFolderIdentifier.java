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

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourceService;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An identifier for a {@link ResourceFolder} implementation, including either a {@link Package} or a {@link Folder}.
 * The {@link ResourceFolder} can be resolved by searching for an implementation which accepts the identifier, with
 * {@link #resolve()} or {@link ResourceFolder#resolve(ResourceFolderIdentifier)}.
 *
 * @author jonathanl (shibo)
 * @see ResourceFolder
 */
@UmlClassDiagram(diagram = DiagramResourceService.class)
@LexakaiJavadoc(complete = true)
public class ResourceFolderIdentifier
{
    private final String identifier;

    public ResourceFolderIdentifier(final String identifier)
    {
        this.identifier = identifier;
    }

    public String identifier()
    {
        return identifier;
    }

    public ResourceFolder resolve()
    {
        return ResourceFolder.resolve(this);
    }

    @Override
    public String toString()
    {
        return identifier;
    }
}
