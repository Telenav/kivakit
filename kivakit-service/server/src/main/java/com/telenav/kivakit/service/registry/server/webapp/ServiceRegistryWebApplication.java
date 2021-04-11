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

import com.telenav.kivakit.service.registry.server.webapp.pages.home.HomePage;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.ExceptionSettings;

import static org.apache.wicket.RuntimeConfigurationType.DEVELOPMENT;

/**
 * Apache Wicket web application that shows information about registered services in a web browser.
 *
 * @author jonathanl (shibo)
 */
@LexakaiJavadoc(complete = true)
public class ServiceRegistryWebApplication extends WebApplication
{
    @Override
    public RuntimeConfigurationType getConfigurationType()
    {
        return DEVELOPMENT;
    }

    @Override
    public Class<? extends Page> getHomePage()
    {
        return HomePage.class;
    }

    @Override
    public void init()
    {
        if (getConfigurationType() == DEVELOPMENT)
        {
            getDebugSettings().setDevelopmentUtilitiesEnabled(true);
            getDebugSettings().setAjaxDebugModeEnabled(true);
            getDebugSettings().setComponentUseCheck(true);
            getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_EXCEPTION_PAGE);
        }
        else
        {
            getMarkupSettings().setStripWicketTags(false);
            getMarkupSettings().setStripComments(false);
            getMarkupSettings().setCompressWhitespace(false);
            getExceptionSettings().setUnexpectedExceptionDisplay(ExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
        }

        mountPage("/home", HomePage.class);
    }
}
