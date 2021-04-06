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

package com.telenav.kivakit.web.wicket;

import com.telenav.kivakit.web.jetty.resources.JettyFilter;
import org.apache.wicket.protocol.http.WebApplication;
import org.eclipse.jetty.servlet.FilterHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

import static javax.servlet.DispatcherType.*;

/**
 * @author jonathanl (shibo)
 */
public class JettyWicket extends JettyFilter
{
    /** Wicket-specific definition of a web application */
    private final Class<? extends WebApplication> applicationClass;

    public JettyWicket(final Class<? extends WebApplication> applicationClass)
    {
        super("[Wicket application = " + applicationClass.getSimpleName() + "]");

        this.applicationClass = applicationClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnumSet<DispatcherType> dispatchers()
    {
        return EnumSet.of(ASYNC, ERROR, FORWARD, INCLUDE, REQUEST);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FilterHolder holder()
    {
        return new JettyWicketFilterHolder(applicationClass);
    }
}
