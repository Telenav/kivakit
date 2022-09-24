package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.Factory;

/**
 * Makes a copy of a given sequence
 *
 * @param <Value> The sequence value
 * @param <Copy> The copy
 * @author jonathanl (shibo)
 */
public interface Copyable<Value, Copy extends Addable<Value> & Sequence<Value>> extends
        Sequence<Value>,
        Factory<Copy>
{
    /**
     * @return A copy of this object
     */
    default Copy copy()
    {
        var set = newInstance();
        set.addAll(this);
        return set;
    }

    /**
     * @return This bounded list filtered to only the elements that match the given matcher
     */
    default Copy matching(Matcher<Value> matcher)
    {
        var filtered = newInstance();
        filtered.addAll(asIterable(matcher));
        return filtered;
    }

    /**
     * @return This list without the matching elements
     */
    default Copy without(Matcher<Value> matcher)
    {
        var iterator = iterator();
        var without = newInstance();
        while (iterator.hasNext())
        {
            var element = iterator.next();
            if (!matcher.matches(element))
            {
                without.add(element);
            }
        }
        return without;
    }
}
