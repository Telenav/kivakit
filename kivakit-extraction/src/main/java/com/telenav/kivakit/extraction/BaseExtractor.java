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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.extraction.internal.lexakai.DiagramExtraction;
import com.telenav.kivakit.interfaces.collection.Keyed;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * A base implementation for extractors that handles edge conditions like exceptions as well as extracting lists from
 * arrays, maps and {@link Keyed} objects.
 *
 * <p><b>Extraction</b></p>
 *
 * <ul>
 *     <li>{@link #extract(Object)}</li>
 *     <li>{@link #extractAll(Object[])}</li>
 *     <li>{@link #extractAll(StringList)}</li>
 * </ul>
 *
 * <p><b>Extractor Implementation</b></p>
 *
 * <ul>
 *     <li>{@link #onExtract(Object)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramExtraction.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
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
     * Returns the value extracted from the given object, or null if an exception is thrown
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
     * Returns a list of values extracted from the given array of objects
     */
    public ObjectList<Value> extractAll(From[] values)
    {
        var extracted = new ObjectList<Value>();
        for (var value : values)
        {
            extracted.add(extract(value));
        }
        return extracted;
    }

    /**
     * Returns a list of values extracted from the given array of objects
     */
    @SuppressWarnings("unchecked")
    public ObjectList<Value> extractAll(StringList values)
    {
        var extracted = new ObjectList<Value>();
        for (var value : values)
        {
            extracted.add(extract((From) value));
        }
        return extracted;
    }

    /**
     * Returns called to extract a value from the given object
     */
    public abstract Value onExtract(From object);
}
