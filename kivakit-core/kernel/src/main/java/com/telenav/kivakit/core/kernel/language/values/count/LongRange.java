package com.telenav.kivakit.core.kernel.language.values.count;

import com.telenav.kivakit.core.kernel.interfaces.collection.Contains;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageValue;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageValue.class)
public class LongRange implements Contains<Long>
{
    private long minimum;

    private long maximum;

    public LongRange(final long minimum, final long maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    protected LongRange()
    {
    }

    public Long constrainTo(Long value)
    {
        value = Math.min(maximum, value);
        value = Math.max(minimum, value);
        return value;
    }

    @Override
    public boolean contains(final Long value)
    {
        return value >= minimum && value <= maximum;
    }

    public Long maximum(final Long that)
    {
        return Math.max(maximum, that);
    }

    public Long maximum()
    {
        return maximum;
    }

    public Long minimum(final Long that)
    {
        return Math.min(minimum, that);
    }

    public Long minimum()
    {
        return minimum;
    }
}
