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
        try (final var reader = new UnquotedCsvReader(resource, schema, ';', ProgressReporter.NULL))
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
