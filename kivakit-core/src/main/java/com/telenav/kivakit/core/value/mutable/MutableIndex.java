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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * A thread-safe mutable index value for use in lambdas and anonymous inner classes. Can be {@link #increment()}ed,
 * {@link #decrement()}ed, added to with {@link #offset(int)}, set with {@link #set(int)} and retrieved with
 * {@link #get()}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramValue.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class MutableIndex extends MutableLong
{
    /**
     * Creates an index with the value zero
     */
    public MutableIndex()
    {
        this(0);
    }

    /**
     * Creates an index with the given initial value
     */
    public MutableIndex(int index)
    {
        super(index);
        ensure(index >= 0, "Negative count ", index);
    }

    /**
     * Gets the value of this index
     */
    public int index()
    {
        return (int) get();
    }

    /**
     * Gets the value of this index
     */
    public void index(int index)
    {
        set(index);
    }

    /**
     * Adds the given value to this index, returning the new value
     */
    @SuppressWarnings("UnusedReturnValue")
    public int offset(int that)
    {
        return (int) plus(that);
    }
}
