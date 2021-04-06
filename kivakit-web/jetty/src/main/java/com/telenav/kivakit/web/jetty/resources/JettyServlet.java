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

package com.telenav.kivakit.web.jetty.resources;

import com.telenav.kivakit.web.jetty.JettyRequestHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Base class for handling servlet requests.
 *
 * @author jonathanl (shibo)
 */
public abstract class JettyServlet extends JettyRequestHandler
{
    public JettyServlet(final String name)
    {
        super(name);
    }

    /**
     * @return Jetty-specific adaptor for servlets
     */
    public abstract ServletHolder holder();
}
