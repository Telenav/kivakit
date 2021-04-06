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
