package com.telenav.kivakit.web.wicket.components.refresh;

import com.telenav.kivakit.core.kernel.language.time.Frequency;
import com.telenav.kivakit.web.wicket.library.Components;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.util.function.Consumer;

/**
 * @author jonathanl (shibo)
 */
public class UpdatingContainer extends WebMarkupContainer
{
    public UpdatingContainer(final String id, final Frequency frequency)
    {
        this(id, frequency, target ->
        {
        });
    }

    public UpdatingContainer(final String id, final Frequency frequency, final Consumer<AjaxRequestTarget> target)
    {
        super(id);
        Components.update(this, frequency, target);
    }
}
