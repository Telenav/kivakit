package com.telenav.kivakit.settings;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;

/**
 * Converts to and from a {@link Deployment}
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class DeploymentConverter extends BaseStringConverter<Deployment>
{
    /** Set of deployments to choose from */
    private final DeploymentSet deployments;

    public DeploymentConverter(Listener listener, DeploymentSet deployments)
    {
        super(listener, Deployment.class);
        this.deployments = deployments;
    }

    @Override
    protected Deployment onToValue(String value)
    {
        if (value != null)
        {
            var deployment = deployments.deployment(value);
            if (deployment != null)
            {
                return deployment;
            }
            problem("No deployment called '$'", value);
        }
        return null;
    }
}
