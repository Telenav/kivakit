////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.path;

import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResource;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourcePath;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.*;

/**
 * An object which has a {@link ResourcePath}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramResourcePath.class)
@UmlClassDiagram(diagram = DiagramResource.class)
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
