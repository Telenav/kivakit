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

package com.telenav.kivakit.kernel.language.values.identifier;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Uses an object to efficiently provide ({@link #hashCode} and {@link #equals(Object)}) identity.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
@LexakaiJavadoc(complete = true)
public class ObjectIdentifier<T>
{
    private final T object;

    private final int hashCode;

    public ObjectIdentifier(final T object)
    {
        this.object = object;
        hashCode = object.hashCode();
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ObjectIdentifier)
        {
            final var that = (ObjectIdentifier<?>) object;
            return hashCode == that.hashCode && this.object.equals(that.object);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    public T object()
    {
        return object;
    }

    @Override
    public String toString()
    {
        return "[ObjectIdentifier object = " + object.toString() + "]";
    }
}