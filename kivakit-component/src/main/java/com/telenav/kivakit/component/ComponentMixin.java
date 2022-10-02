package com.telenav.kivakit.component;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.component.internal.lexakai.DiagramComponent;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.repeaters.RepeaterMixin;
import com.telenav.kivakit.mixins.Mixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * A mixin for {@link Component} which can be implemented by a class that already extends another base class.
 *
 * @author jonathanl (shibo)
 * @see Component
 * @see Mixin
 * @see RepeaterMixin
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public interface ComponentMixin extends
        Component,
        Mixin,
        RepeaterMixin
{
    /**
     * @return The {@link BaseComponent} implementation associated with this mixin
     */
    default BaseComponent component()
    {
        return mixin(ComponentMixin.class, BaseComponent::new);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default String objectName()
    {
        return component().objectName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void objectName(String objectName)
    {
        component().objectName(objectName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default Repeater repeater()
    {
        return component();
    }
}
