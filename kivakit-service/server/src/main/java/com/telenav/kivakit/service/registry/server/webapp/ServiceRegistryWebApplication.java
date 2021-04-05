package com.telenav.kivakit.service.registry.server.webapp;

import com.telenav.kivakit.service.registry.server.webapp.pages.home.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.settings.ExceptionSettings;

import static org.apache.wicket.RuntimeConfigurationType.DEVELOPMENT;

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
