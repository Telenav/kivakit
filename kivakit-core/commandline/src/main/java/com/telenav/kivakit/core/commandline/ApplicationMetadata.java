package com.telenav.kivakit.core.commandline;

import com.telenav.kivakit.core.kernel.language.values.version.Versioned;

/**
 * Application metadata used in formulating command line usage help.
 *
 * @author jonathanl (shibo)
 */
public interface ApplicationMetadata extends Versioned
{
    /**
     * @return The application description
     */
    String description();
}
