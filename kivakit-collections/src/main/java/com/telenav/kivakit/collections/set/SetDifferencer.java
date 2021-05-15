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

package com.telenav.kivakit.collections.set;

import com.telenav.kivakit.collections.project.lexakai.diagrams.DiagramSet;
import com.telenav.kivakit.kernel.interfaces.comparison.Equality;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.Set;

/**
 * Computes the edit difference between two sets. As the comparison proceeds, each element that is added, removed or
 * updated (according to an identity comparator) in the after set when compared with the before set, is passed to a
 * corresponding subclass method.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramSet.class)
public abstract class SetDifferencer<Element>
{
    private final Equality<Element> updateComparator;

    protected SetDifferencer(final Equality<Element> updateComparator)
    {
        this.updateComparator = updateComparator;
    }

    /**
     * Compares beforeSet with afterSet, calling {@link #onAdded(Object)}, {@link #onRemoved(Object)} and {@link
     * #onUpdated(Object)} appropriately.
     *
     * @param beforeSet The set as it used to be
     * @param afterSet The set as it will be
     */
    public void compare(final Set<Element> beforeSet, final Set<Element> afterSet)
    {
        // All members of before that are also members of after
        final var beforeUnionAfter = new ConcurrentHashSet<Element>();

        // Go through all objects held before
        for (final var before : beforeSet)
        {
            // If the after set no longer contains the object,
            if (!afterSet.contains(before))
            {
                // the object was removed
                onRemoved(before);
            }
            else
            {
                // otherwise save the object so it can be looked up below
                if (updateComparator != null)
                {
                    beforeUnionAfter.add(before);
                }
            }
        }

        // Go through all objects held after
        for (final var after : afterSet)
        {
            // If the before set didn't have the object,
            if (!beforeSet.contains(after))
            {
                // the object was added
                onAdded(after);
            }
            else
            {
                if (updateComparator != null)
                {
                    // Get the before version of the after object
                    final var before = beforeUnionAfter.get(after);

                    // If they are not identical
                    if (!updateComparator.isEqual(before, after))
                    {
                        // the object was updated
                        onUpdated(after);
                    }
                    else
                    {
                        onNotChanged(before, after);
                    }
                }
            }
        }
    }

    protected abstract void onAdded(Element value);

    @SuppressWarnings({ "EmptyMethod" })
    protected void onNotChanged(final Element oldValue, final Element newValue)
    {
    }

    protected abstract void onRemoved(Element value);

    protected abstract void onUpdated(Element value);
}