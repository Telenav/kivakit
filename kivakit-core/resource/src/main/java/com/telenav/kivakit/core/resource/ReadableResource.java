////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource;

import com.telenav.kivakit.core.kernel.interfaces.io.Readable;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.core.resource.reading.ResourceReader;
import com.telenav.kivakit.core.resource.resources.string.StringResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
@UmlRelation(label = "provides", referent = ResourceReader.class)
public interface ReadableResource extends Readable
{
    default StringResource asStringResource()
    {
        return new StringResource(reader().string());
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
    void copyTo(final WritableResource destination, final CopyMode mode, ProgressReporter reporter);

    /**
     * @return A reader with convenient methods for reading from the resource
     */
    default ResourceReader reader(final ProgressReporter reporter)
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
    default ResourceReader reader(final ProgressReporter reporter, final Charset charset)
    {
        return new ResourceReader(resource(), reporter, charset);
    }

    /**
     * @return The resource being read
     */
    Resource resource();
}
