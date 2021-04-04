////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.web.jetty;

import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.core.test.UnitTest;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebUnitTest extends UnitTest
{
    @SuppressWarnings("SameParameterValue")
    protected boolean startWebServer(final int portNumber)
    {
        final var war = Folder.parse("eclipse-build/main/test");
        if (war != null && war.exists())
        {
            final HttpConfiguration http = new HttpConfiguration();
            final Server server = new Server();
            final ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(http));
            connector.setPort(portNumber);
            server.addConnector(connector);

            final WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/");
            webapp.setWar("eclipse-build/main/test");
            server.setHandler(webapp);

            try
            {
                server.start();
            }
            catch (final Exception e)
            {
                throw new Problem(e, "Couldn't start embedded Jetty web server").asException();
            }
            return true;
        }
        return false;
    }
}
