package com.telenav.kivakit.web.jersey.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class WebJerseyProject extends Project
{
    private static final Lazy<WebJerseyProject> singleton = Lazy.of(WebJerseyProject::new);

    public static WebJerseyProject get()
    {
        return singleton.get();
    }

    protected WebJerseyProject()
    {
    }
}
