////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
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
