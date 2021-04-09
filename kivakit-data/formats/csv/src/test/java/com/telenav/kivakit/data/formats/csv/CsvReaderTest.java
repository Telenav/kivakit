////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
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
        try (final var myReader = new CsvReader(resource, schema, ',', ProgressReporter.NULL))
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
