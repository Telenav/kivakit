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

import com.telenav.kivakit.web.jetty.BaseJettyRequestHandler;
import org.eclipse.jetty.servlet.FilterHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Base class for request handlers that filter requests and then pass them on to other request handlers and finally
 * servlets. The kind of requests that the filter intercepts is determined by {@link #dispatchers()}.
 *
 * @author jonathanl (shibo)
 */
public abstract class BaseJettyFilter extends BaseJettyRequestHandler
{
    public BaseJettyFilter(final String name)
    {
        super(name);
    }

    /**
     * @return The set of request types that this filter handles
     */
    public abstract EnumSet<DispatcherType> dispatchers();

    /**
     * @return Jetty-specific adaptor for filters
     */
    public abstract FilterHolder holder();
}
