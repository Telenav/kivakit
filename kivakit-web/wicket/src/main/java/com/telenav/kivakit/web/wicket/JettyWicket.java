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
