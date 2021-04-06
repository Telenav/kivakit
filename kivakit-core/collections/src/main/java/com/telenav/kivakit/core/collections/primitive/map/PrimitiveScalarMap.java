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

package com.telenav.kivakit.core.collections.primitive.map;

import com.telenav.kivakit.core.collections.primitive.map.scalars.IntToByteMap;
import com.telenav.kivakit.core.collections.primitive.map.scalars.LongToLongMap;
import com.telenav.kivakit.core.collections.project.lexakai.diagrams.DiagramPrimitiveMap;
import com.telenav.kivakit.core.kernel.interfaces.numeric.Quantizable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A map from one scalar primitive to another, like {@link LongToLongMap} or {@link IntToByteMap}. This interface allows
 * all such maps to be treated the same, by widening the key and value to a long primitive.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramPrimitiveMap.class)
public interface PrimitiveScalarMap
{
    /**
     * @return The value for the given key
     */
    long getScalar(long key);

    /**
     * @return True if the given key represents null
     */
    boolean isScalarKeyNull(long key);

    /**
     * @return True if the given value represents null
     */
    boolean isScalarValueNull(long value);

    /**
     * Stores the given value under the given key
     */
    void putScalar(long key, long value);

    default void putScalar(final long key, final Quantizable value)
    {
        putScalar(key, value.quantum());
    }
}
