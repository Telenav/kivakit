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

import com.telenav.kivakit.resource.project.lexakai.DiagramFileSystemFile;
import com.telenav.kivakit.resource.project.lexakai.DiagramResource;
import com.telenav.kivakit.resource.writing.ResourceWriter;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.io.PrintWriter;
import java.nio.charset.Charset;

/**
 * Adds the ability to write to {@link Resource} and {@link Writable}. A {@link ResourceWriter}, which provides a
 * collection of useful methods for writing to resource, can be accessed with {@link #writer()} or {@link
 * #writer(Charset)}. For convenience, a {@link PrintWriter} can be retrieved with {@link #printWriter()}
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@LexakaiJavadoc(complete = true)
public interface WritableResource extends Resource, Writable
{
    /**
     * @return A {@link PrintWriter} for writing to this resource
     */
    default PrintWriter printWriter()
    {
        return writer().printWriter();
    }

    /**
     * @return A {@link ResourceWriter} for writing to this resource
     */
    @UmlRelation(label = "provides")
    default ResourceWriter writer()
    {
        return new ResourceWriter(this);
    }

    /**
     * @return A {@link ResourceWriter} for writing to this resource with the given {@link Charset}
     */
    default ResourceWriter writer(Charset charset)
    {
        return new ResourceWriter(this, charset);
    }
}
