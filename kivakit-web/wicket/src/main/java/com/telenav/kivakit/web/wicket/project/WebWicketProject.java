package com.telenav.kivakit.web.wicket.project;

import com.telenav.kivakit.core.kernel.language.objects.Lazy;
import com.telenav.kivakit.core.kernel.project.Project;

/**
 * @author jonathanl (shibo)
 */
public class WebWicketProject extends Project
{
    private static final Lazy<WebWicketProject> singleton = Lazy.of(WebWicketProject::new);

    public static WebWicketProject get()
    {
        return singleton.get();
    }

    protected WebWicketProject()
    {
    }
}
