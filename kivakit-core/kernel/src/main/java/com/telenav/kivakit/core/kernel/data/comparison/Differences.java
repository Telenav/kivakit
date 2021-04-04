////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.comparison;

import com.telenav.kivakit.core.kernel.interfaces.numeric.Sized;
import com.telenav.kivakit.core.kernel.language.collections.list.ObjectList;
import com.telenav.kivakit.core.kernel.language.collections.list.StringList;
import com.telenav.kivakit.core.kernel.language.iteration.Iterables;
import com.telenav.kivakit.core.kernel.language.objects.Objects;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataComparison;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

/**
 * Tracks the difference between objects that are compared with {@link #compare(String, Object, Object)} or {@link
 * #compare(String, Iterable, Iterable)}. If there have been any differences, {@link #isDifferent()} will return true,
 * and if not, {@link #isIdentical()} will return true. The number of differences is available through {@link #size()}
 * and {@link #toString()} returns a description of all the differences that have been encountered.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataComparison.class)
public class Differences implements Sized
{
    @UmlAggregation
    private final StringList descriptions = new StringList();

    /**
     * Adds the description of a difference
     */
    public void add(final String description)
    {
        descriptions.add(description);
    }

    /**
     * Adds the given differences to this
     */
    public void add(final Differences that)
    {
        for (final var description : that.descriptions)
        {
            add(description);
        }
    }

    /**
     * Compares the values returned by <i>a</i> and <i>b</i>, adding the difference description if they do not match
     *
     * @return True if the values are equal, false if they are not
     */
    public <T> boolean compare(final String description, final Iterable<T> a, final Iterable<T> b)
    {
        if (!Iterables.equals(a, b))
        {
            final var aDescription = a == null ? "null" : new ObjectList<>().appendAll(a);
            final var bDescription = b == null ? "null" : new ObjectList<>().appendAll(b);

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
    public <T> boolean compare(final String description, final T a, final T b)
    {
        if (!Objects.equal(a, b))
        {
            add(description + " (" + a + " vs " + b + ")");
            return false;
        }
        return true;
    }

    /**
     * @return True if any differences have been found
     */
    public boolean isDifferent()
    {
        return !isIdentical();
    }

    /**
     * @return True if no differences have been found
     */
    public boolean isIdentical()
    {
        return descriptions.isEmpty();
    }

    /**
     * @return The number of differences that have been found
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
