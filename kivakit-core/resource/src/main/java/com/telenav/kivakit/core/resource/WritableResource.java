////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.kernel.interfaces.io.Writable;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramFileSystemFile;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.core.resource.writing.ResourceWriter;

import java.io.PrintWriter;
import java.nio.charset.Charset;

@UmlClassDiagram(diagram = DiagramFileSystemFile.class)
@UmlClassDiagram(diagram = DiagramResource.class)
public interface WritableResource extends Resource, Writable
{
    default PrintWriter printWriter()
    {
        return writer().printWriter();
    }

    @UmlRelation(label = "provides")
    default ResourceWriter writer()
    {
        return new ResourceWriter(this);
    }

    default ResourceWriter writer(final Charset charset)
    {
        return new ResourceWriter(this, charset);
    }
}
