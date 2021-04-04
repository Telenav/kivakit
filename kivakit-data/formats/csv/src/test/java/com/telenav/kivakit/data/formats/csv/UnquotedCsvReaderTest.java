////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.DoubleConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class UnquotedCsvReaderTest extends UnitTest
{
    @Test
    public void test()
    {
        final var year = CsvColumn.of("year", new IntegerConverter(this));
        final var make = CsvColumn.of("make");
        final var model = CsvColumn.of("model");
        final var description = CsvColumn.of("description");
        final var price = CsvColumn.of("price", new DoubleConverter(this));
        final var schema = new CsvSchema(year, make, model, description, price);

        final var resource = PackageResource.packageResource(PackagePath.packagePath(UnquotedCsvReaderTest.class), "SampleUnquotedCsv.csv");
        try (final var reader = new UnquotedCsvReader(resource, ProgressReporter.NULL, schema, ';'))
        {
            reader.skipLines(1);
            ensure(reader.hasNext());
            final var firstDataLine = reader.next();
            // simple test
            ensureEqual(firstDataLine.get(year), 1997);
            // comma-within-column test
            ensureEqual(firstDataLine.get(description), "\"ac, abs, moon\"");
            reader.next();
            final var thirdLine = reader.next();
            // quote-within-column test
            ensureEqual(thirdLine.get(model), "\"Venture \"\"Extended Edition, Very Large\"\"\"");
        }
    }
}
