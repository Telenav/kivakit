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

package com.telenav.kivakit.service.registry.server.webapp.pages.home;

import com.telenav.kivakit.core.kernel.language.collections.Collections;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.messages.Result;
import com.telenav.kivakit.core.network.core.cluster.ClusterIdentifier;
import com.telenav.kivakit.service.registry.Scope;
import com.telenav.kivakit.service.registry.Service;
import com.telenav.kivakit.service.registry.client.ServiceRegistryClient;
import com.telenav.kivakit.service.registry.server.ServiceRegistryServer;
import com.telenav.kivakit.service.registry.server.webapp.ServiceRegistryWebPage;
import com.telenav.kivakit.web.wicket.components.refresh.UpdatingContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static com.telenav.kivakit.service.registry.server.ServiceRegistryServerSettings.WICKET_AJAX_REFRESH_FREQUENCY;

public class HomePage extends ServiceRegistryWebPage
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public HomePage()
    {
        final IModel<String> scope = new Model<>(isLocal() ? "localhost" : "network");

        final var services = new LoadableDetachableModel<Result<Set<Service>>>()
        {
            @Override
            protected Result<Set<Service>> load()
            {
                return services(scope.getObject());
            }
        };

        final var scopes = new LoadableDetachableModel<List<String>>()
        {
            @Override
            protected List<String> load()
            {
                final var scopes = Scope.names(services.getObject());
                scopes.add(0, "network");
                if (isLocal())
                {
                    scopes.add(0, "localhost");
                }
                return scopes;
            }
        };

        final var updatingContainer = new UpdatingContainer("updating-container", WICKET_AJAX_REFRESH_FREQUENCY,
                target -> services.detach());
        updatingContainer.setOutputMarkupId(true);

        updatingContainer.add(new WebMarkupContainer("no-services")
        {
            @Override
            public boolean isVisible()
            {
                final var result = services.getObject();
                return result.get() != null && result.get().isEmpty();
            }
        });

        updatingContainer.add(new Label("error", () -> services.getObject().why().description())
        {
            @Override
            public boolean isVisible()
            {
                return services.getObject().failed();
            }
        });

        final var scopeDropdown = new DropDownChoice<>("scope", new Model<>(isLocal() ? "localhost" : "network"), scopes);
        scopeDropdown.add(new AjaxFormComponentUpdatingBehavior("change")
        {
            @Override
            protected void onUpdate(final AjaxRequestTarget target)
            {
                scopes.detach();
                target.add(updatingContainer);
            }
        });
        updatingContainer.add(scopeDropdown);

        updatingContainer.add(new ListView<>("list", () -> list(scopeDropdown))
        {
            @Override
            protected void populateItem(final ListItem<Service> item)
            {
                item.add(new ServicePanel("panel", updatingContainer, item.getModel()));
            }
        });

        add(updatingContainer);
    }

    private boolean isLocal()
    {
        return ServiceRegistryServer.get().isLocal();
    }

    @NotNull
    private List<Service> list(final DropDownChoice<String> scopeDropdown)
    {
        final var scope = scopeDropdown.getModelObject();
        return Collections.sorted(services(scope).get());
    }

    private Result<Set<Service>> services(final String scopeString)
    {
        final Scope scope;
        switch (scopeString)
        {
            case "network":
            {
                // If we are a local registry, search the network scope remotely, otherwise we are the network
                // registry so we search our own local information.
                scope = isLocal() ? Scope.network() : Scope.localhost();
                break;
            }

            case "localhost":
            {
                // Search the local host
                scope = Scope.localhost();
                break;
            }

            default:
            {
                // Search the given cluster
                scope = Scope.cluster(new ClusterIdentifier(scopeString));
                break;
            }
        }

        // If we are looking locally,
        if (scope.isLocal())
        {
            // just directly access the registry without the client
            return ServiceRegistryServer.get().registry().discoverServices();
        }
        else
        {
            // otherwise have the client access the remote scope
            final var client = LOGGER.listenTo(new ServiceRegistryClient());
            return client.discoverServices(scope);
        }
    }
}
