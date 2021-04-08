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

package com.telenav.kivakit.data.formats.library;

import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.data.formats.library.project.lexakai.diagrams.DiagramDataFormat;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * Identifying values for common data formats.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataFormat.class)
@LexakaiJavadoc(complete = true)
public enum DataFormat
{
    Graph, // KivaKit graph file format
    CSV, // Comma-separated variables format
    Database, // The data came directly from database
    XML, // OSM XML format
    PBF, // OpenStreetMap native protobuf format being used for data from UniDB and OSM community
    ;

    public static DataFormat of(final Resource input)
    {
        switch (input.extension().toString())
        {
            case ".graph":
                return Graph;

            case ".pbf":
            case ".osm.pbf":
                return PBF;

            case ".csv":
                return CSV;

            case ".osm":
                return XML;

            default:
                return fail("Data format of '$' is not recognized", input);
        }
    }

    public boolean isCsv()
    {
        return this == CSV;
    }

    public boolean isDatabase()
    {
        return this == Database;
    }

    public boolean isGraph()
    {
        return this == Graph;
    }

    public boolean isOsm()
    {
        return this == XML;
    }

    public boolean isPbf()
    {
        return this == PBF;
    }
}
