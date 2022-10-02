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

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResource;
import com.telenav.kivakit.resource.internal.lexakai.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_DEFAULT_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * An object which has a {@link ResourcePath}, as returned by {@link #path()}. Convenience methods provide access to the
 * path's filename and extension(s).
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #path()}</li>
 *     <li>{@link #uri()}</li>
 *     <li>{@link #url()}</li>
 * </ul>
 *
 * <p><b>Matching</b></p>
 *
 * <ul>
 *     <li>{@link #matches(Matcher)}</li>
 * </ul>
 *
 * <p><b>File Names</b></p>
 *
 * <ul>
 *     <li>{@link #baseFileName()}</li>
 *     <li>{@link #compoundExtension()}</li>
 *     <li>{@link #extension()}</li>
 *     <li>{@link #fileName()}</li>
 *     <li>{@link #hasExtension(Extension)}</li>
 * </ul>
 *
 * <p><b>Conversions</b></p>
 *
 * <ul>
 *     <li>{@link #asJavaFile()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@ApiQuality(stability = API_STABLE_DEFAULT_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public interface ResourcePathed extends UriIdentified
{
    /**
     * Returns this path as a java.io.File
     */
    default java.io.File asJavaFile()
    {
        return path().asJavaFile();
    }

    /**
     * Returns the base name of the file name of this object
     */
    default FileName baseFileName()
    {
        return fileName().baseName();
    }

    /**
     * Returns the compound extension of this file named object (for example, ".xml", ".osm.pbf" or ".txd.gz")
     */
    default Extension compoundExtension()
    {
        return fileName().compoundExtension();
    }

    /**
     * Returns the extension of this resource
     */
    default Extension extension()
    {
        return fileName().extension();
    }

    /**
     * Returns the file name of this resource
     */
    default FileName fileName()
    {
        return path().fileName();
    }

    /**
     * Returns true if this resource ends with the given extension
     */
    default boolean hasExtension(@NotNull Extension extension)
    {
        return fileName().endsWith(extension);
    }

    /**
     * Returns true if the resource path matches the given matcher
     */
    default boolean matches(@NotNull Matcher<String> matcher)
    {
        return matcher.matches(path().asString());
    }

    /**
     * Returns the path for this object
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

    /**
     * Returns the path for this object as a URL
     */
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
