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

import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.servlet.FilterHolder;

/**
 * <b>Not public API</b>
 *
 * <p>
 * Installs and configures the {@link WicketFilter} required to serve the given {@link WebApplication} class.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
class JettyWicketFilterHolder extends FilterHolder
{
    public JettyWicketFilterHolder(final Class<? extends WebApplication> applicationClass)
    {
        super(new WicketFilter());

        // Set expected Wicket initialization parameters
        setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM, applicationClass.getName());
        setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    }
}
