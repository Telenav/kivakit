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

package com.telenav.kivakit.resource.path;

import com.telenav.kivakit.resource.ResourcePath;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

/**
 * An object which has a {@link ResourcePath}, as returned by {@link #path()}. Convenience methods provide access to the
 * path's filename and extension(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@LexakaiJavadoc(complete = true)
public interface ResourcePathed
{
    /**
     * @return The base name of the file name of this object
     */
    default FileName baseName()
    {
        return fileName().base();
    }

    /**
     * @return The compound extension of this file named object (for example, ".xml", ".osm.pbf" or ".txd.gz")
     */
    default Extension compoundExtension()
    {
        return fileName().compoundExtension();
    }

    /**
     * @return The extension of this resource
     */
    default Extension extension()
    {
        return fileName().extension();
    }

    /**
     * @return The file name of this resource
     */
    default FileName fileName()
    {
        return path().fileName();
    }

    /**
     * @return True if this resource ends with the given extension
     */
    default boolean hasExtension(final Extension extension)
    {
        return fileName().endsWith(extension);
    }

    /**
     * @return The path to this object
     */
    @UmlRelation(label = "supplies")
    ResourcePath path();
}
