package com.telenav.kivakit.core.kernel.language.collections.set;

import com.telenav.kivakit.core.kernel.language.values.count.Maximum;

import java.util.Set;

/**
 * @author jonathanl (shibo)
 */
public class ObjectSet<T> extends BaseSet<T>
{
    public ObjectSet(final Maximum maximumSize)
    {
        super(maximumSize);
    }

    public ObjectSet(final Maximum maximumSize, final Set<T> set)
    {
        super(maximumSize, set);
    }

    protected ObjectSet()
    {
        super();
    }

    @Override
    public ObjectSet<T> onNewInstance()
    {
        return new ObjectSet<T>();
    }
}
