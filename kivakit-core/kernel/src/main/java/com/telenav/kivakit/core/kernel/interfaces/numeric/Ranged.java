package com.telenav.kivakit.core.kernel.interfaces.numeric;

import com.telenav.kivakit.core.kernel.interfaces.collection.Contains;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramInterfaceNumeric;

/**
 * Something that has a numeric range of values.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceNumeric.class)
public interface Ranged<Value extends Minimizable<Value> & Maximizable<Value>> extends Contains<Value>
{
    /**
     * @return The given value constrained to this range
     */
    Value constrainTo(final Value value);

    /**
     * @return The maximum value of this range
     */
    Value maximum();

    /**
     * @return The minimum value of this range
     */
    Value minimum();
}
