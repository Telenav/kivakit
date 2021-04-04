package com.telenav.kivakit.web.swagger.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class WebSwaggerProject extends Project
{
    private static final Lazy<WebSwaggerProject> singleton = Lazy.of(WebSwaggerProject::new);

    public static WebSwaggerProject get()
    {
        return singleton.get();
    }

    protected WebSwaggerProject()
    {
    }
}
