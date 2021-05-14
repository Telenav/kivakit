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

package com.telenav.kivakit.kernel.language.values.mutable;

import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * A value that can be retrived with {@link #get()} and mutated with {@link #set(Object)} or {@link #update(Function)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
@LexakaiJavadoc(complete = true)
public class ConcurrentMutableValue<T>
{
    private final AtomicReference<T> value = new AtomicReference<>();

    public ConcurrentMutableValue()
    {
    }

    public ConcurrentMutableValue(final T value)
    {
        this.value.set(value);
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof ConcurrentMutableValue)
        {
            final var that = (ConcurrentMutableValue<?>) object;
            return value.get().equals(that.value.get());
        }
        return false;
    }

    public T get()
    {
        return value.get();
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    public void set(final T value)
    {
        this.value.set(value);
    }

    public void update(final Function<T, T> updater)
    {
        value.getAndUpdate(updater::apply);
    }
}
