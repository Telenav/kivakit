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

import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.lexakai.DiagramResource;
import com.telenav.kivakit.resource.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * An object which has a {@link ResourcePath}, as returned by {@link #path()}. Convenience methods provide access to the
 * path's filename and extension(s).
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@LexakaiJavadoc(complete = true)
public interface ResourcePathed extends UriIdentified
{
    default java.io.File asJavaFile()
    {
        return path().asJavaFile();
    }

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
    default boolean hasExtension(Extension extension)
    {
        return fileName().endsWith(extension);
    }

    /**
     * @return True if the resource path matches the given matcher
     */
    default boolean matches(Matcher<String> matcher)
    {
        return matcher.matches(path().asString());
    }

    /**
     * @return The path to this object
     */
    @UmlRelation(label = "supplies")
    ResourcePath path();

    /**
     * {@inheritDoc}
     */
    @Override
    default URI uri()
    {
        return path().uri();
    }

    default URL url()
    {
        try
        {
            return uri().toURL();
        }
        catch (MalformedURLException e)
        {
            new Problem(e, "Unable to convert to URL: $", uri()).throwAsIllegalStateException();
            return null;
        }
    }
}
