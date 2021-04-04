////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
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
