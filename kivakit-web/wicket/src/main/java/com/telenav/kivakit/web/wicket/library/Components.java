package com.telenav.kivakit.web.wicket.library;

import com.telenav.kivakit.core.kernel.language.time.Frequency;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;

import java.util.function.Consumer;

/**
 * @author jonathanl (shibo)
 */
public class Components
{
    public static void update(final Component component, final Frequency frequency,
                              final Consumer<AjaxRequestTarget> afterUpdate)
    {
        component.setOutputMarkupId(true);
        component.setOutputMarkupPlaceholderTag(true);
        component.add(new AjaxSelfUpdatingTimerBehavior(frequency.duration().asJavaDuration())
        {
            @Override
            protected void onPostProcessTarget(final AjaxRequestTarget target)
            {
                super.onPostProcessTarget(target);
                afterUpdate.accept(target);
            }
        });
    }
}
