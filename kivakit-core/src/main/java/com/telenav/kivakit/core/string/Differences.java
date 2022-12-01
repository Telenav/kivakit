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

package com.telenav.kivakit.core.string;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.internal.lexakai.DiagramLanguage;
import com.telenav.kivakit.interfaces.collection.Sized;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.iteration.Iterables.equalIterables;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.language.Objects.isEqual;

/**
 * Tracks the difference between objects that are compared with {@link #compare(String, Object, Object)} or
 * {@link #compare(String, Iterable, Iterable)}. If there have been any differences, {@link #isDifferent()} will return
 * true, and if not, {@link #isIdentical()} will return true. The number of differences is available through
 * {@link #size()} and {@link #toString()} returns a description of all the differences that have been encountered.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguage.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class Differences implements Sized
{
    @UmlAggregation
    private final StringList descriptions = new StringList();

    /**
     * Adds the description of a difference
     */
    public void add(String description)
    {
        descriptions.add(description);
    }

    /**
     * Adds the given differences to this
     */
    public void add(Differences that)
    {
        for (var description : that.descriptions)
        {
            add(description);
        }
    }

    /**
     * Compares the values returned by <i>a</i> and <i>b</i>, adding the difference description if they do not match
     *
     * @return True if the values are equal, false if they are not
     */
    public <T> boolean compare(String description, Iterable<T> a, Iterable<T> b)
    {
        if (!equalIterables(a, b))
        {
            var aDescription = a == null ? "null" : list().appendAll(a);
            var bDescription = b == null ? "null" : list().appendAll(b);

            add(description + " (" + aDescription + " vs " + bDescription + ")");

            return false;
        }
        return true;
    }

    /**
     * Compares the values <i>a</i> and <i>b</i>, adding the difference description if they do not match
     *
     * @return True if the values are equal, false if they are not
     */
    public <T> boolean compare(String description, T a, T b)
    {
        if (!isEqual(a, b))
        {
            add(description + " (" + a + " vs " + b + ")");
            return false;
        }
        return true;
    }

    /**
     * Returns true if any differences have been found
     */
    public boolean isDifferent()
    {
        return !isIdentical();
    }

    /**
     * Returns true if no differences have been found
     */
    public boolean isIdentical()
    {
        return descriptions.isEmpty();
    }

    /**
     * Returns the number of differences that have been found
     */
    @Override
    @UmlExcludeMember
    public int size()
    {
        return descriptions.size();
    }

    @Override
    public String toString()
    {
        return descriptions.join();
    }
}
