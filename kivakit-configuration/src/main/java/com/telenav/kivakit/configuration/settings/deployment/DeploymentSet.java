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
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.other.PropertyMap;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;

/**
 * A set of {@link Deployment} objects, each being a set of configuration objects. Deployments can be added to the set
 * from a folder with {@link #addDeploymentsIn(Folder)}. A switch parser to select a deployment from the command line
 * can be retrieved with SwitchParser.deployment(DeploymentSet).
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see Settings
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class DeploymentSet extends BaseRepeater
{
    public static DeploymentSet create()
    {
        return new DeploymentSet();
    }

    /**
     * Loads all deployments in the root package 'deployments' and in any folder specified by
     * KIVAKIT_DEPLOYMENT_FOLDER.
     */
    public static DeploymentSet load(Listener listener, Class<?> relativeTo)
    {
        // Create an empty set of deployments,
        var deployments = listener.listenTo(DeploymentSet.create());

        // and if there is a root package called 'deployments' in the application,
        var settings = Package.of(relativeTo, "deployments");
        if (settings != null)
        {
            // then add all the deployments in that package,
            deployments.addDeploymentsIn(settings);
        }

        // and if a deployment folder was specified, and it exists,
        var deploymentFolder = PropertyMap.of(JavaVirtualMachine.local().properties())
                .asFolder("KIVAKIT_DEPLOYMENT_FOLDER");
        if (deploymentFolder != null && deploymentFolder.exists())
        {
            // then add all the deployments in that folder.
            deployments.addDeploymentsIn(deploymentFolder);
        }

        return deployments;
    }

    public static DeploymentSet of(Deployment deployment, Deployment... more)
    {
        var set = new DeploymentSet();
        set.add(deployment);
        set.addAll(Arrays.asList(more));
        return set;
    }

    @UmlAggregation
    private final Set<Deployment> deployments = new HashSet<>();

    protected DeploymentSet()
    {
    }

    /**
     * Adds the given deployment to this set
     */
    public void add(Deployment deployment)
    {
        deployments.add(deployment);
    }

    /**
     * Adds all the deployments in the given collection to this set
     */
    public void addAll(Collection<Deployment> deployments)
    {
        this.deployments.addAll(deployments);
    }

    /**
     * Adds all the deployments from the sub-folders found in the given folder.
     */
    public DeploymentSet addDeploymentsIn(Folder parent)
    {
        // Go through the sub-folders,
        for (var folder : parent.folders())
        {
            // get description from deployment metadata,
            String description = description(folder.file("Deployment.metadata"));

            // create a deployment,
            var deployment = listenTo(new Deployment(folder.name().name(), description));

            // and add the configuration information from the sub-folder,
            deployment.registerAllSettingsIn(this, folder);

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
    public DeploymentSet addDeploymentsIn(Package package_)
    {
        return addDeploymentsIn(package_.path());
    }

    public DeploymentSet addDeploymentsIn(Class<?> relativeTo, String path)
    {
        return addDeploymentsIn(PackagePath.parsePackagePath(relativeTo, path));
    }

    /**
     * Adds all the deployments from the sub-packages found in the given package.
     */
    public DeploymentSet addDeploymentsIn(PackagePath path)
    {
        // Go through the sub-packages,
        for (var subPackage : path.subPackages())
        {
            // get description from deployment metadata,
            String description = description(Package.of(subPackage).resource("Deployment.metadata"));

            // create a deployment,
            var deployment = listenTo(new Deployment(subPackage.last(), description));

            // and add the configuration information from the sub-folder,
            deployment.registerAllSettingsIn(this, subPackage);

            // add it to this set of deployments.
            deployments.add(deployment);
        }
        return this;
    }

    /**
     * @return The named deployment
     */
    public Deployment deployment(String name)
    {
        for (var deployment : deployments)
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

    public void install(String name)
    {
        ensureNotNull(deployment(name)).install();
    }

    public boolean isEmpty()
    {
        return deployments.isEmpty();
    }

    public int size()
    {
        return deployments.size();
    }

    public SwitchParser.Builder<Deployment> switchParser(String name)
    {
        return Deployment.deploymentSwitchParser(this, name);
    }

    private String description(Resource resource)
    {
        var description = "'" + resource.fileName().name() + "' deployment";
        var deploymentProperties = PropertyMap.load(this, resource);
        if (deploymentProperties.containsKey("description"))
        {
            description = deploymentProperties.get("description");
        }
        return description;
    }
}
