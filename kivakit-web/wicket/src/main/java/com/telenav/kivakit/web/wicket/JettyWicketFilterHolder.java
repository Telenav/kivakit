package com.telenav.kivakit.web.wicket;

import org.apache.wicket.protocol.http.ContextParamWebApplicationFactory;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.eclipse.jetty.servlet.FilterHolder;

/**
 * @author jonathanl (shibo)
 */
public class JettyWicketFilterHolder extends FilterHolder
{
    public JettyWicketFilterHolder(final Class<? extends WebApplication> applicationClass)
    {
        super(new WicketFilter());

        // Set expected Wicket initialization parameters
        setInitParameter(ContextParamWebApplicationFactory.APP_CLASS_PARAM, applicationClass.getName());
        setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
    }
}
