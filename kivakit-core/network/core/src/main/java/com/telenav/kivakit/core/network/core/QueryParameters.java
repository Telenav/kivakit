////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.network.core;

import com.telenav.kivakit.core.network.core.project.lexakai.diagrams.DiagramNetworkLocation;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
public class QueryParameters
{
    private boolean sorted;

    private String string;

    private VariableMap<String> map;

    public QueryParameters(final String string)
    {
        this.string = string;
    }

    public QueryParameters(final VariableMap<String> map)
    {
        this.map = map;
    }

    public VariableMap<String> asMap()
    {
        if (map == null)
        {
            map = new VariableMap<>();
            for (final var assignment : StringList.split(Maximum._1_000, string, "&"))
            {
                final var split = StringList.split(assignment, "=");
                if (split.size() == 2)
                {
                    map.add(split.get(0), split.get(1));
                }
            }
        }
        return map;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof QueryParameters)
        {
            final var that = (QueryParameters) object;
            return asMap().equals(that.asMap());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return asMap().hashCode();
    }

    public boolean isEmpty()
    {
        return asMap().isEmpty();
    }

    @Override
    public String toString()
    {
        if (!sorted)
        {
            if (map == null)
            {
                map = asMap();
            }
            final var assignments = new StringList();
            final List<String> keys = new ArrayList<>(map.keySet());
            Collections.sort(keys);
            for (final var key : keys)
            {
                assignments.add(key + "=" + map.get(key));
            }
            string = assignments.join("&");
            sorted = true;
        }
        return string;
    }
}
