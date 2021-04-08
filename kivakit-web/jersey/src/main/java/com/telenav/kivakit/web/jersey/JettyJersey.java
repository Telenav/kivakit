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

package com.telenav.kivakit.web.jersey;

import com.telenav.kivakit.web.jetty.JettyServer;
import com.telenav.kivakit.web.jetty.resources.BaseJettyServlet;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * {@link BaseJettyServlet} plugin that can be added to {@link JettyServer} to serve REST resources from the {@link
 * ResourceConfig} application passed to the constructor.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class JettyJersey extends BaseJettyServlet
{
    private final ResourceConfig application;

    /**
     * @param application The REST application
     */
    public JettyJersey(final ResourceConfig application)
    {
        super("[Jersey application = " + application.getClass().getSimpleName() + "]");

        this.application = application;
    }

    @Override
    public ServletHolder holder()
    {
        // Get the fully qualified class name of the JAX-RS application,
        final var name = application.getClass().getName();

        // create a "ServletContainer" for the application (this object may look like it's part of
        // the Servlet API, but it would be better named "RestServlet"),
        final var restServlet = new ServletContainer(application);

        // create a Jetty-specific "ServletHolder" for the servlet
        final var jersey = new ServletHolder(name, restServlet);

        // and initialize it with the name of the JAX-RS application.
        jersey.setInitOrder(0);
        jersey.setInitParameter("javax.ws.rs.Application", name);

        return jersey;
    }
}
