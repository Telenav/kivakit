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

package com.telenav.kivakit.core.object;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramObject;
import com.telenav.kivakit.interfaces.collection.Clearable;
import com.telenav.kivakit.interfaces.factory.Factory;
import com.telenav.kivakit.interfaces.value.Source;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A lazy-initializing value. Given a factory that creates a value, only creates the object when {@link #get()} is
 * called. After that the value is cached and {@link #get()} will return the same value. {@link #clear()} can be used to
 * clear the value and force it to be re-created in the future.
 *
 * <p><b>Example</b></p>
 *
 * <pre>
 *  private static Lazy&lt;EdgeAttributes&gt; singleton = Lazy.of(EdgeAttributes::new);
 * </pre>
 *
 * @param <Value> The type of value to create
 * @author jonathanl (shibo)
 * @author Sergiy Yevtushenko
 */
@UmlClassDiagram(diagram = DiagramObject.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class Lazy<Value> implements Clearable
{
    /**
     * Factory method to create a Lazy object with the given value factory
     */
    public static <V> Lazy<V> lazy(Factory<V> factory)
    {
        return new Lazy<>(factory);
    }

    /** The factory to create a new value */
    private final Factory<Value> factory;

    /** A reference to the initialize method */
    private final Source<Value> createValueMethod = this::createValue;

    /** A reference to the method that currently returns a value */
    private volatile Source<Value> valueSource = createValueMethod;

    /**
     * @param factory A factory to create values whenever needed
     */
    protected Lazy(Factory<Value> factory)
    {
        this.factory = factory;
    }

    /**
     * Clears this lazy value. It will be recreated by the factory if {@link #get()} is called.
     */
    @Override
    public synchronized void clear()
    {
        valueSource = this::createValue;
    }

    /**
     * @return The lazy-loaded value
     */
    public final Value get()
    {
        return valueSource.get();
    }

    public final boolean has()
    {
        return get() != null;
    }

    private synchronized Value createValue()
    {
        // If we haven't entered this method yet,
        if (valueSource == createValueMethod)
        {
            // create a new value,
            var value = factory.newInstance();

            // and have the value source return that constant value,
            valueSource = () -> value;
        }

        // then return the value.
        return get();
    }
}
