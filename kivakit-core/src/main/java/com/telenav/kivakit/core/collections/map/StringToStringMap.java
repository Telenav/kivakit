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

package com.telenav.kivakit.core.collections.map;

import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.core.value.count.Maximum;
import com.telenav.kivakit.core.language.primitive.Ints;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.project.lexakai.DiagramCollections;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A bounded map from string to string.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramCollections.class)
public class StringToStringMap extends BaseStringMap<String>
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public StringToStringMap()
    {
        this(Maximum.MAXIMUM);
    }

    public StringToStringMap(Maximum maximumSize)
    {
        super(maximumSize);
    }

    public int asInt(String key)
    {
        return Ints.parseFast(get(key), Integer.MIN_VALUE);
    }

    public Count count(String key)
    {
        return Count.parseCount(LOGGER, get(key));
    }

    public String get(String key)
    {
        return super.get(key);
    }
}
