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

package com.telenav.kivakit.network.core;

import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.values.count.Maximum;
import com.telenav.kivakit.network.core.project.lexakai.diagrams.DiagramNetworkLocation;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Query parameters, as used in HTTP URLs.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@LexakaiJavadoc(complete = true)
public class QueryParameters
{
    public static QueryParameters parse(final String string)
    {
        return new QueryParameters(string);
    }

    private boolean sorted;

    private String string;

    private VariableMap<String> map;

    public QueryParameters(final VariableMap<String> map)
    {
        this.map = map;
    }

    protected QueryParameters(final String string)
    {
        this.string = string;
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