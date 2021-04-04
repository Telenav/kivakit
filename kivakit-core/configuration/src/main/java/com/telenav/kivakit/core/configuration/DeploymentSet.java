////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.configuration;

import com.telenav.kivakit.core.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of {@link Deployment} objects, each being a set of configuration objects. Deployments can be added to the set
 * from a folder with {@link #addDeployments(Folder)}. A switch parser to select a deployment from the command line can
 * be retrieved with SwitchParser.deployment(DeploymentSet).
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see ConfigurationSet
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class DeploymentSet extends BaseRepeater
{
    public static DeploymentSet load(final Listener listener, final Class<?> relativeTo, final String path)
    {
        return load(listener, PackagePath.parsePackagePath(relativeTo, path));
    }

    public static DeploymentSet load(final Listener listener, final Package _package)
    {
        return load(listener, _package.path());
    }

    public static DeploymentSet load(final Listener listener, final PackagePath path)
    {
        return listener.listenTo(new DeploymentSet()).addDeployments(path);
    }

    public static DeploymentSet load(final Listener listener, final Folder folder)
    {
        return new DeploymentSet().addDeployments(folder);
    }

    public static DeploymentSet of(final Deployment deployment, final Deployment... more)
    {
        final var set = new DeploymentSet();
        set.add(deployment);
        set.addAll(Arrays.asList(more));
        return set;
    }

    @UmlAggregation
    private final Set<Deployment> deployments = new HashSet<>();

    /**
     * Adds the given deployment to this set
     */
    public void add(final Deployment deployment)
    {
        deployments.add(deployment);
    }

    /**
     * Adds all the deployments in the given collection to this set
     */
    public void addAll(final Collection<Deployment> deployments)
    {
        this.deployments.addAll(deployments);
    }

    /**
     * Adds all the deployments from the sub-folders found in the given folder.
     */
    public DeploymentSet addDeployments(final Folder parent)
    {
        // Go through the sub-folders,
        for (final var folder : parent.folders())
        {
            // create a deployment,
            final var deployment = listenTo(new Deployment(folder.name().name(), "'" + folder.name() + "' deployment"));

            // and add the configuration information from the sub-folder,
            deployment.addFolder(folder);

            // assert that the deployment has not already been added,
            assert !deployments.contains(deployment);

            // and if we don't already have the deployment,
            if (!deployments.contains(deployment))
            {
                // add it to this set of deployments.
                add(deployment);
            }
        }
        return this;
    }

    /**
     * Adds all the deployments from the sub-packages found in the given package.
     */
    public DeploymentSet addDeployments(final PackagePath path)
    {
        // Go through the sub-packages,
        for (final var subPackage : path.subPackages())
        {
            // create a deployment,
            final var deployment = listenTo(new Deployment(subPackage.last(), "'" + subPackage.last() + "' deployment"));

            // and add the configuration information from the sub-folder,
            deployment.addPackage(subPackage);

            // add it to this set of deployments.
            deployments.add(deployment);
        }
        return this;
    }

    /**
     * @return The named deployment
     */
    public Deployment deployment(final String name)
    {
        for (final var deployment : deployments)
        {
            if (deployment.name().equalsIgnoreCase(name))
            {
                return deployment;
            }
        }
        return null;
    }
}
