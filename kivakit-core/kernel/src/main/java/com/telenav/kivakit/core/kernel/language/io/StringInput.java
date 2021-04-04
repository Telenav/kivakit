////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.io;

import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageIo;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.InputStream;

/**
 * An input stream built around a StringBuilder to read it.
 *
 * @author matthieun
 */
@UmlClassDiagram(diagram = DiagramLanguageIo.class)
public class StringInput extends InputStream
{
    private int index;

    private final String toRead;

    public StringInput(final String toRead)
    {
        this.toRead = toRead;
    }

    @Override
    public int read()
    {
        if (index < toRead.length())
        {
            final var result = toRead.charAt(index);
            index++;
            return result;
        }
        else
        {
            return -1;
        }
    }
}
