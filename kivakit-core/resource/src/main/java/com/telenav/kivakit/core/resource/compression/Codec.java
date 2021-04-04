////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.resource.compression;

import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.resource.compression.codecs.NullCodec;
import com.telenav.kivakit.core.resource.project.lexakai.diagrams.DiagramResourceCompression;

@UmlClassDiagram(diagram = DiagramResourceCompression.class)
public interface Codec extends Compressor, Decompressor
{
    Codec NULL = new NullCodec();
}
