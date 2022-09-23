package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.interfaces.factory.Factory;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_DEFAULT_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.NOT_TESTED;

/**
 * Retrieves different subsections from an {@link Indexable}.
 *
 * @param <Value> The value being indexed
 * @param <Section> A type that can contain subsections of an {@link Indexable}, normally a list
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@ApiQuality(stability = STABLE_DEFAULT_EXPANDABLE,
            testing = NOT_TESTED,
            documentation = DOCUMENTED)
public interface Sectionable<Value, Section extends Addable<Value> & Indexable<Value>> extends
        Indexable<Value>,
        Addable<Value>,
        Factory<Section>
{
    /**
     * @return The first n values in this object. If there are fewer than count values, all values are returned.
     */
    default Indexable<Value> first(int count)
    {
        var list = newInstance();
        for (var i = 0; i < Math.min(count, size()); i++)
        {
            list.add(get(i));
        }
        return list;
    }

    /**
     * @return The last n values in this object. If there are fewer than count values, all values are returned.
     */
    default Indexable<Value> last(int count)
    {
        var list = newInstance();
        for (var i = Math.max(size() - count - 1, 0); i < size(); i++)
        {
            list.add(get(i));
        }
        return list;
    }

    /**
     * @return The values in this object to the left of the index, exclusive
     */
    default Indexable<Value> leftOf(int index)
    {
        var left = newInstance();
        for (var i = 0; i < index; i++)
        {
            left.add(get(i));
        }
        return left;
    }

    /**
     * @return This bounded list filtered to only the elements that match the given matcher
     */
    default Indexable<Value> matching(Matcher<Value> matcher)
    {
        var filtered = newInstance();
        filtered.addAll(asIterable(matcher));
        return filtered;
    }

    /**
     * @return The values in this object to the right of the index, exclusive
     */
    default Indexable<Value> rightOf(int index)
    {
        var right = newInstance();
        for (var i = index + 1; i < size(); i++)
        {
            right.add(get(i));
        }
        return right;
    }

    /**
     * @return This list without the matching elements
     */
    default Indexable<Value> without(Matcher<Value> matcher)
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
