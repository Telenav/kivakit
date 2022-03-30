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

package com.telenav.kivakit.core.value.mutable;

import com.telenav.kivakit.core.lexakai.DiagramValue;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Function;

/**
 * A value that can be retrieved with {@link #get()} and mutated with {@link #set(Object)} or {@link #update(Function)}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramValue.class)
@LexakaiJavadoc(complete = true)
public class MutableValue<T>
{
    private T value;

    public MutableValue()
    {
    }

    public MutableValue(T value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof MutableValue)
        {
            var that = (MutableValue<?>) object;
            return value.equals(that.value);
        }
        return false;
    }

    public T get()
    {
        return value;
    }

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

    public void set(T value)
    {
        this.value = value;
    }

    public void update(Function<T, T> updater)
    {
        value = updater.apply(value);
    }
}
