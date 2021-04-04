////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.csv;

import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.DoubleConverter;
import com.telenav.kivakit.core.kernel.data.conversion.string.primitive.IntegerConverter;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.progress.ProgressReporter;
import com.telenav.kivakit.core.resource.resources.packaged.PackageResource;
import com.telenav.kivakit.core.test.UnitTest;
import org.junit.Test;

public class CsvReaderTest extends UnitTest
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

        final var resource = PackageResource.packageResource(PackagePath.packagePath(CsvReaderTest.class), "SampleCsv.csv");
        try (final var myReader = new CsvReader(resource, ProgressReporter.NULL, schema, ','))
        {
            myReader.skipLines(1);
            ensure(myReader.hasNext());

            // 1st line
            final var firstDataLine = myReader.next();
            ensureEqual(firstDataLine.get(year), 1997);
            ensureEqual(firstDataLine.get(description), "ac, abs, moon");
            ensureEqual(firstDataLine.get(price), 3000.0);

            // 2nd line
            myReader.next();

            // 3rd line
            final var thirdLine = myReader.next();
            ensureEqual(thirdLine.get(model), "Venture \"Extended Edition, Very Large\"");

            // 4th line
            final var fourthLine = myReader.next();
            ensure(fourthLine.get(description).indexOf('\n') >= 0);

            // 5th line
            final var fifthLine = myReader.next();
            ensureEqual(fifthLine.get(model), "\"Venture Extended Edition, Very Large\"");

            // 6th line
            final var sixthLine = myReader.next();
            ensureEqual(sixthLine.get(model), "\"K\" Ct");
        }
    }
}
