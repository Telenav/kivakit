package com.telenav.kivakit.component;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.component.internal.lexakai.DiagramComponent;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Base class for KivaKit components. For details, see {@link Component}.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Repeater
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class BaseComponent extends BaseRepeater implements Component
{
    /** The name of this object for debugging purposes */
    private String objectName = NamedObject.syntheticName(this);

    /**
     * Sets the object name for this component
     *
     * @param objectName The object's new name
     */
    @Override
    public void objectName(String objectName)
    {
        this.objectName = objectName;
    }

    /**
     * Returns this component's object name
     */
    @Override
    public String objectName()
    {
        return objectName;
    }
}
