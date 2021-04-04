package com.telenav.kivakit.web.wicket.components.feedback;

import com.telenav.kivakit.web.wicket.theme.KivaKitTheme;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.CssResourceReference;

/**
 * @author jonathanl (shibo)
 */
public class FeedbackPanel extends org.apache.wicket.markup.html.panel.FeedbackPanel
{
    public FeedbackPanel(final String id)
    {
        super(id);
    }

    @Override
    public void renderHead(final IHeaderResponse response)
    {
        final var colors = new CssResourceReference(KivaKitTheme.class, "KivaKitColors.css");
        response.render(CssHeaderItem.forReference(colors));

        final var css = new CssResourceReference(FeedbackPanel.class, "FeedbackPanel.css");
        response.render(CssHeaderItem.forReference(css));
    }
}
