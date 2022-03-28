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

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.test.UnitTest;
import org.junit.Test;

public class QueryParametersTest extends UnitTest
{
    @Test
    public void testEquals()
    {
        var q1 = new QueryParameters("d=4&a=1&c=3&b=2");
        var q2 = new QueryParameters("a=1&b=2&c=3&d=4");
        ensureEqual(q1, q2);
    }

    @Test
    public void testMap()
    {
        var variables = new VariableMap<String>();
        variables.put("d", "4");
        variables.put("a", "1");
        variables.put("c", "3");
        variables.put("b", "2");
        var parameters = new QueryParameters(variables);
        ensureEqual(4, parameters.asMap().size());
        ensureEqual("a=1&b=2&c=3&d=4", parameters.toString());
        var q1 = new QueryParameters("d=4&a=1&c=3&b=2");
        ensureEqual(q1, parameters);
    }

    @Test
    public void testParseString()
    {
        var parameters = new QueryParameters("x=1&y=2");
        var variables = parameters.asMap();
        ensureEqual(2, variables.size());
        ensureEqual("1", variables.get("x"));
        ensureEqual("2", variables.get("y"));
    }

    @Test
    public void testToString()
    {
        var parameters = new QueryParameters("y=2&x=1");
        ensureEqual("x=1&y=2", parameters.toString());
        var p2 = new QueryParameters("d=4&a=1&c=3&b=2");
        ensureEqual("a=1&b=2&c=3&d=4", p2.toString());
    }
}
