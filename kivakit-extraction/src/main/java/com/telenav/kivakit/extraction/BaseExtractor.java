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

package com.telenav.kivakit.extraction;

import com.telenav.kivakit.annotations.code.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.extraction.internal.lexakai.DiagramExtraction;
import com.telenav.kivakit.interfaces.collection.Keyed;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A base implementation for extractors that handles edge conditions like exceptions as well as extracting lists from
 * arrays, maps and {@link Keyed} objects.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramExtraction.class)
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class BaseExtractor<Value, From> extends BaseRepeater implements Extractor<Value, From>
{
    /**
     * @param listener The listener to hear any conversion issues
     */
    protected BaseExtractor(Listener listener)
    {
        addListener(listener);
    }

    /**
     * @return The value extracted from the given object, or null if an exception is thrown
     */
    @Override
    @UmlExcludeMember
    public Value extract(From object)
    {
        try
        {
            return onExtract(object);
        }
        catch (Exception e)
        {
            problem(e, "Extractor threw exception");
            return null;
        }
    }

    /**
     * @return A list of values extracted from the given array of objects
     */
    public ObjectList<Value> extract(From[] values)
    {
        var extracted = new ObjectList<Value>();
        for (var value : values)
        {
            extracted.add(extract(value));
        }
        return extracted;
    }

    /**
     * @return A list of values extracted from the given array of objects
     */
    @SuppressWarnings("unchecked")
    public ObjectList<Value> extract(StringList values)
    {
        var extracted = new ObjectList<Value>();
        for (var value : values)
        {
            extracted.add(extract((From) value));
        }
        return extracted;
    }

    /**
     * @return Called to extract a value from the given object
     */
    public abstract Value onExtract(From object);
}
