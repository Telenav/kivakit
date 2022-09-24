package com.telenav.kivakit.interfaces.collection;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.factory.Factory;

import static com.telenav.kivakit.annotations.code.ApiStability.MORE_EVALUATION_NEEDED;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Retrieves different subsections from an {@link Indexable}.
 *
 * @param <Value> The value being indexed
 * @param <Section> A type that can contain subsections of an {@link Indexable}, normally a list
 * @author jonathanl (shibo)
 */
@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@ApiQuality(stability = MORE_EVALUATION_NEEDED,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED,
            reviews = 1,
            reviewers = "shibo")
public interface Sectionable<Value, Section extends Addable<Value> & Indexable<Value>> extends
        Indexable<Value>,
        Addable<Value>,
        Factory<Section>
{
    /**
     * @return The first n values in this object. If there are fewer than count values, all values are returned.
     */
    default Section first(int count)
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
    default Section last(int count)
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
    default Section leftOf(int index)
    {
        var left = newInstance();
        for (var i = 0; i < index; i++)
        {
            left.add(get(i));
        }
        return left;
    }

    /**
     * @return The values in this object to the right of the index, exclusive
     */
    default Section rightOf(int index)
    {
        var right = newInstance();
        for (var i = index + 1; i < size(); i++)
        {
            right.add(get(i));
        }
        return right;
    }
}
