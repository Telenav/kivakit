package com.telenav.kivakit.component;

import com.telenav.kivakit.component.project.lexakai.DiagramComponent;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.mixins.Mixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A mixin for {@link Component} which can be implemented by a class that already extends another base class.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Mixin
 * @see RepeaterMixin
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
public interface ComponentMixin extends Component, Mixin, RepeaterMixin
{
    /**
     * @return The {@link BaseComponent} implementation associated with this mixin
     */
    default BaseComponent component()
    {
        return mixin(ComponentMixin.class, BaseComponent::new);
    }

    @Override
    default String objectName()
    {
        return component().objectName();
    }

    @Override
    default void objectName(String objectName)
    {
        component().objectName(objectName);
    }

    @Override
    default Repeater repeater()
    {
        return component();
    }
}
