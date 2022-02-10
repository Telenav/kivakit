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

package com.telenav.kivakit.kernel.language.objects;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.interfaces.loading.Loadable;
import com.telenav.kivakit.kernel.interfaces.loading.Unloadable;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A lazy-initializing value. Given a factory that creates a value, only creates the object when {@link #get()} is
 * called. After that the value is cached and {@link #get()} will return the same value. {@link #clear()} can be used to
 * clear the value and force it to be re-created in the future.
 * <p>
 * <b>Example</b>
 * <pre>
 *  private static Lazy&lt;EdgeAttributes&gt; singleton = Lazy.of(EdgeAttributes::new);
 * </pre>
 *
 * <b>NOTE:</b> This class is not thread-safe.
 *
 * @param <Value> The type of value to create
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageObject.class)
public class Lazy<Value> implements Loadable, Unloadable
{
    /**
     * Factory method to create a Lazy object with the given value factory
     */
    public static <V> Lazy<V> of(Factory<V> factory)
    {
        return new Lazy<>(factory);
    }

    /** The value, or null if it doesn't exist */
    private Value value;

    /** The factory to create a new value */
    private final Factory<Value> factory;

    /**
     * @param factory A factory to create values when needed
     */
    protected Lazy(Factory<Value> factory)
    {
        this.factory = factory;
    }

    /**
     * Clears the value so it must be recreated
     */
    public void clear()
    {
        value = null;
    }

    /**
     * @return The value
     */
    public final Value get()
    {
        load();
        return value;
    }

    public boolean has()
    {
        return value != null;
    }

    @Override
    public void load()
    {
        if (value == null)
        {
            value = factory.newInstance();
        }
    }

    @Override
    public void unload()
    {
        clear();
    }
}
