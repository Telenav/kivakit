////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.network.core;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.network.core.internal.lexakai.DiagramNetworkLocation;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Query parameters, as used in HTTP URLs.
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link QueryParameters#QueryParameters }</li>
 * </ul>
 *
 * <p><b>Parsing</b></p>
 *
 * <ul>
 *     <li>{@link #parseQueryParameters(Listener, String)}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <ul>
 *     <li>{@link #asVariableMap()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramNetworkLocation.class)
@ApiQuality(stability = STABLE_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public class QueryParameters
{
    /**
     * Parses the given text into query parameters
     *
     * @param listener The listener to call with any problems
     * @param text The text to parse
     * @return The query parameters
     */
    @SuppressWarnings("unused")
    public static QueryParameters parseQueryParameters(Listener listener, String text)
    {
        return new QueryParameters(text);
    }

    /** True to sort the parameters */
    private boolean sorted;

    /** The text parsed to create this object */
    private String text;

    /** The variable map of parameters */
    private VariableMap<String> map;

    /**
     * Creates query parameters using the given variable map
     *
     * @param map The variables to include in query parameters
     */
    public QueryParameters(VariableMap<String> map)
    {
        this.map = map;
    }

    protected QueryParameters(String text)
    {
        this.text = text;
    }

    /**
     * Returns these query parameters as a variable map
     */
    public VariableMap<String> asVariableMap()
    {
        if (map == null)
        {
            map = new VariableMap<>();
            for (var assignment : StringList.split(text, "&"))
            {
                var split = StringList.split(assignment, "=");
                if (split.size() == 2)
                {
                    map.add(split.get(0), split.get(1));
                }
            }
        }
        return map;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof QueryParameters)
        {
            var that = (QueryParameters) object;
            return asVariableMap().equals(that.asVariableMap());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return asVariableMap().hashCode();
    }

    public boolean isEmpty()
    {
        return asVariableMap().isEmpty();
    }

    @Override
    public String toString()
    {
        if (!sorted)
        {
            if (map == null)
            {
                map = asVariableMap();
            }
            var assignments = new StringList();
            List<String> keys = new ArrayList<>(map.keySet());
            Collections.sort(keys);
            for (var key : keys)
            {
                assignments.add(key + "=" + map.get(key));
            }
            text = assignments.join("&");
            sorted = true;
        }
        return text;
    }
}
