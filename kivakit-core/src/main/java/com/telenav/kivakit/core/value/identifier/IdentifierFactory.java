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

package com.telenav.kivakit.core.value.identifier;

import com.telenav.kivakit.core.internal.lexakai.DiagramIdentifier;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A thread-safe factory that produces identifiers
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramIdentifier.class)
public class IdentifierFactory implements Factory<Identifier>
{
    private final AtomicLong next;

    public IdentifierFactory()
    {
        this(0);
    }

    public IdentifierFactory(long base)
    {
        next = new AtomicLong(base);
    }

    @Override
    public Identifier newInstance()
    {
        return new Identifier(next.incrementAndGet());
    }
}
