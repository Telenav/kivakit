////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  © 2020 Telenav - All rights reserved.                                                                              /
//  This software is the confidential and proprietary information of Telenav ("Confidential Information").             /
//  You shall not disclose such Confidential Information and shall use it only in accordance with the                  /
//  terms of the license agreement you entered into with Telenav.                                                      /
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.data.formats.library;

import com.telenav.kivakit.core.resource.Resource;
import com.telenav.kivakit.data.formats.library.project.lexakai.diagrams.DiagramDataFormat;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure.fail;

/**
 * The (binary or text) format of an input {@link Resource}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataFormat.class)
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
