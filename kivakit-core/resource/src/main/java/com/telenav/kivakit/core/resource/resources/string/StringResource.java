////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.resources.string;

import com.telenav.kivakit.core.kernel.language.io.StringInput;
import com.telenav.kivakit.core.kernel.language.values.count.Bytes;
import com.telenav.kivakit.core.resource.ResourcePath;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceType;
import com.telenav.kivakit.core.resource.reading.BaseReadableResource;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;
import java.util.function.Function;

@UmlClassDiagram(diagram = DiagramResourceType.class)
public class StringResource extends BaseReadableResource
{
    private final String value;

    public StringResource(final String value)
    {
        super(ResourcePath.parseUnixResourcePath("/objects/String@" + Integer.toHexString(value.hashCode())));
        this.value = value;
    }

    @Override
    public Bytes bytes()
    {
        return Bytes.bytes(value.length());
    }

    @Override
    public InputStream onOpenForReading()
    {
        return new StringInput(value);
    }

    public StringResource transform(final Function<String, String> transformation)
    {
        return new StringResource(transformation.apply(value));
    }
}
