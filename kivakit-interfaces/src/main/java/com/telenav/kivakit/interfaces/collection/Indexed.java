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

package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.lexakai.DiagramCollection;
import com.telenav.kivakit.interfaces.numeric.QuantumComparable;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An object which has an index and is also {@link Quantizable} as are many objects.
 *
 * @author jonathanl (shibo)
 * @see Quantizable
 */
@UmlClassDiagram(diagram = DiagramCollection.class)
@LexakaiJavadoc(complete = true)
public interface Indexed extends
        Quantizable,
        QuantumComparable<Indexed>
{
    /**
     * @return The index
     */
    int index();

    /**
     * @return The index as a quantum value
     */
    @Override
    default long quantum()
    {
        return index();
    }
}
