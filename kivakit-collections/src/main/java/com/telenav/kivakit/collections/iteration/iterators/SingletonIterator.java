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

package com.telenav.kivakit.collections.iteration.iterators;

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramIteration;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.unsupported;

/**
 * An {@link Iterator} implementation with only one value to iterate.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIteration.class)
@LexakaiJavadoc(complete = true)
public class SingletonIterator<Element> implements Iterator<Element>
{
    private final Element element;

    private boolean iterated;

    public SingletonIterator(final Element element)
    {
        this.element = element;
    }

    @Override
    public boolean hasNext()
    {
        return !iterated;
    }

    @Override
    public Element next()
    {
        if (!iterated)
        {
            iterated = true;
            return element;
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove()
    {
        unsupported();
    }
}
