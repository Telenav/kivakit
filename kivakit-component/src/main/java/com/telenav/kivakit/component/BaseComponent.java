package com.telenav.kivakit.component;

import com.telenav.kivakit.component.lexakai.DiagramComponent;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Base class for KivaKit components. For details, see {@link Component}.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Repeater
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
public class BaseComponent extends BaseRepeater implements Component
{
    /** The name of this object for debugging purposes */
    private String objectName = NamedObject.syntheticName(this);

    @Override
    public void objectName(String objectName)
    {
        this.objectName = objectName;
    }

    @Override
    public String objectName()
    {
        return objectName;
    }
}
