package com.telenav.kivakit.service.registry.server.webapp;

import com.telenav.kivakit.service.registry.server.ServiceRegistryServer;
import com.telenav.kivakit.web.wicket.components.header.HeaderPanel;
import com.telenav.kivakit.web.wicket.theme.KivaKitTheme;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.resource.CssResourceReference;

public class ServiceRegistryWebPage extends WebPage
{
    public ServiceRegistryWebPage()
    {
        final var network = ServiceRegistryServer.get().isNetwork();
        final var title = network ? "Network Service Registry" : "Service Registry";
        add(new Label("page-title", "KivaKit " + title));
        add(new HeaderPanel("header", ServiceRegistryServer.get().version(), title));
        add(new WebMarkupContainer("menu"));
    }

    @Override
    public void renderHead(final IHeaderResponse response)
    {
        final var colors = new CssResourceReference(KivaKitTheme.class, "KivaKitColors.css");
        response.render(CssHeaderItem.forReference(colors));

        final var theme = new CssResourceReference(KivaKitTheme.class, "KivaKitTheme.css");
        response.render(CssHeaderItem.forReference(theme));
    }
}
