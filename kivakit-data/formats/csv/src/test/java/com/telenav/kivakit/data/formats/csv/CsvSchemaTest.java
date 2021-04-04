////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class CsvSchemaTest extends UnitTest
{
    @Test
    public void testColumnForName()
    {
        final var identifierColumn = CsvColumn.of("roadSectionIdentifier");
        final var shapesColumn = CsvColumn.of("roadSectionShapes");
        final CsvSchema schema = CsvSchema.of(identifierColumn, shapesColumn);
        ensureEqual(identifierColumn, schema.columnForName("roadSectionIdentifier"));
        ensureEqual(shapesColumn, schema.columnForName("roadSectionShapes"));
    }
}
