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

package com.telenav.kivakit.core.collections.primitive.map.multi;

import com.telenav.kivakit.core.collections.primitive.list.PrimitiveList;
import com.telenav.kivakit.core.collections.primitive.map.multi.fixed.IntToByteFixedMultiMap;
import com.telenav.kivakit.core.collections.primitive.map.multi.fixed.LongToLongFixedMultiMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMultiMap;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.List;

/**
 * A map from one scalar primitive to another, like {@link LongToLongFixedMultiMap} or {@link IntToByteFixedMultiMap}.
 * This interface allows all such maps to be treated the same, by widening the keys and values to long primitives.
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMultiMap.class)
public interface PrimitiveScalarMultiMap
{
    /**
     * @return A list of primitives for the given key, or null if there is none
     */
    PrimitiveList getPrimitiveList(long key);

    /**
     * @return A list of primitives for the given key, or null if there is none
     */
    default PrimitiveList getSignedPrimitiveList(final long key)
    {
        return getPrimitiveList(key);
    }

    /**
     * @return True if the given key represents null
     */
    boolean isScalarKeyNull(long key);

    /**
     * Stores the given list of primitives under the given key
     */
    void putPrimitiveList(long key, PrimitiveList values);

    /**
     * Stores the given list of values under the given key
     */
    void putPrimitiveList(long key, List<? extends Quantizable> values);
}
