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

import com.telenav.kivakit.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.resource.reading.ResourceReader;
import com.telenav.kivakit.resource.resources.string.StringResource;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Interface to an object that is {@link Readable} and can be read with a {@link ResourceReader}. A reader can be
 * obtained with:
 *
 * <ul>
 *     <li>{@link #reader()}</li>
 *     <li>{@link #reader(ProgressReporter)}</li>
 *     <li>{@link #reader(ProgressReporter, Charset)}</li>
 * </ul>
 * <p>
 * The {@link #resource()} method must be defined by the implementer, as well as the method
 * {@link #copyTo(WritableResource, CopyMode, ProgressReporter)}.
 */
@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlRelation(label = "provides", referent = ResourceReader.class)
@LexakaiJavadoc(complete = true)
public interface ReadableResource extends Readable
{
    default StringResource asStringResource()
    {
        return new StringResource(reader().asString());
    }

    /**
     * @return The charset used by this resource
     */
    default Charset charset()
    {
        return StandardCharsets.UTF_8;
    }

    /**
     * Copies this resource to the given destination
     *
     * @param destination The destination to write to
     */
    void copyTo(WritableResource destination, CopyMode mode, ProgressReporter reporter);

    /**
     * @return A reader with convenient methods for reading from the resource
     */
    default ResourceReader reader(ProgressReporter reporter)
    {
        return new ResourceReader(resource(), reporter, charset());
    }

    /**
     * @return A reader with convenient methods for reading from the resource
     */
    default ResourceReader reader()
    {
        return reader(ProgressReporter.NULL);
    }

    /**
     *
     */
    default ResourceReader reader(ProgressReporter reporter, Charset charset)
    {
        return new ResourceReader(resource(), reporter, charset);
    }

    /**
     * @return The resource being read
     */
    Resource resource();
}
