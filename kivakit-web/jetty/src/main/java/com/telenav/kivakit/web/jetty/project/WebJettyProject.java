package com.telenav.kivakit.web.jetty.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class WebJettyProject extends Project
{
    private static final Lazy<WebJettyProject> singleton = Lazy.of(WebJettyProject::new);

    public static WebJettyProject get()
    {
        return singleton.get();
    }

    protected WebJettyProject()
    {
    }
}
