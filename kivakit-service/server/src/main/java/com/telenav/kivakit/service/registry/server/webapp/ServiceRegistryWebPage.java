////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.service.registry.server.webapp;

import com.telenav.kivakit.service.registry.server.ServiceRegistryServer;
import com.telenav.kivakit.web.wicket.components.header.HeaderPanel;
import com.telenav.kivakit.web.wicket.theme.KivaKitTheme;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * Base class for web pages within the {@link ServiceRegistryWebApplication}. Gives a consistent look and feel and
 * applies a header area to each page subclass.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
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
        response.render(CssHeaderItem.forReference(KivaKitTheme.kivakitColors()));
        response.render(CssHeaderItem.forReference(KivaKitTheme.kivakitTheme()));
    }
}
