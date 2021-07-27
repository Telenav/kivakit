////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.configuration.settings.deployment;

import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.resources.packaged.Package;
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
 * @see Settings
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
            deployment.registerAllIn(folder);

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
            deployment.registerAllIn(subPackage);

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

    public Set<Deployment> deployments()
    {
        return deployments;
    }

    public SwitchParser<Deployment> switchParser(final String name)
    {
        return Deployment.deploymentSwitchParser(this, name);
    }
}
