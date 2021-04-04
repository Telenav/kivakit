////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.library.text;

import com.telenav.kivakit.core.kernel.interfaces.naming.Named;
import com.telenav.kivakit.data.formats.library.text.project.lexakai.diagrams.DiagramText;
import com.telenav.lexakai.annotations.UmlClassDiagram;

@UmlClassDiagram(diagram = DiagramText.class)
public class TextColumn implements Named
{
    private final int index;

    private final String name;

    /**
     * @param name The name identifying the column.
     * @param index the zero-indexed location of the column within the row.
     */
    public TextColumn(final String name, final int index)
    {
        this.name = name;
        this.index = index;
    }

    @Override
    public String name()
    {
        return name;
    }

    int getIndex()
    {
        return index;
    }
}
