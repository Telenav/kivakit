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

package com.telenav.kivakit.settings;

import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.vm.JavaVirtualMachine;
import com.telenav.kivakit.properties.PropertyMap;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.resource.packages.Package;
import com.telenav.kivakit.settings.lexakai.DiagramSettings;
import com.telenav.kivakit.settings.stores.ResourceFolderSettingsStore;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A set of {@link Deployment} objects, each being a set of settings objects. Deployments can be added to the set from a
 * folder with {@link #addDeploymentsIn(ResourceFolder)}. A switch parser to select a deployment from the command line
 * can be retrieved with SwitchParser.deployment(DeploymentSet).
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see Settings
 */
@SuppressWarnings("UnusedReturnValue")
@UmlClassDiagram(diagram = DiagramSettings.class)
public class DeploymentSet extends BaseRepeater implements RegistryTrait
{
    /**
     * Loads all deployments in the root package 'deployments' and in any folder specified by
     * KIVAKIT_DEPLOYMENT_FOLDER.
     */
    @SuppressWarnings("ConstantConditions")
    public static DeploymentSet load(Listener listener, Class<?> relativeTo)
    {
        // Create an empty set of deployments,
        var deployments = listener.listenTo(new DeploymentSet());

        // and if there is a root package called 'deployments' in the application,
        var settings = Package.parsePackage(listener, relativeTo, "deployments");
        if (settings != null)
        {
            // then add all the deployments in that package,
            deployments.addDeploymentsIn(settings);
        }

        // and if a deployment folder was specified, and it exists,
        var deploymentFolder = PropertyMap.propertyMap(JavaVirtualMachine.local().properties())
                .asFolder("KIVAKIT_DEPLOYMENT_FOLDER");
        if (deploymentFolder != null && deploymentFolder.exists())
        {
            // then add all the deployments in that folder.
            deployments.addDeploymentsIn(deploymentFolder);
        }

        return deployments;
    }

    public static DeploymentSet of(Listener listener, Deployment deployment, Deployment... more)
    {
        var set = listener.listenTo(new DeploymentSet());
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
     * Adds all the deployments from the sub-packages found in the given package.
     */
    public DeploymentSet addDeploymentsIn(ResourceFolder<?> folder)
    {
        // Go through the sub-packages,
        for (var child : folder.folders())
        {
            // get description from deployment metadata,
            String description = description(child.resource("Deployment.metadata"));

            // create a deployment,
            var deployment = new Deployment(this, child.path().last(), description);

            // and add the configuration information from the sub-folder,
            deployment.indexAll(new ResourceFolderSettingsStore(this, child));

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

    /**
     * @return The deployments in this set
     */
    public Set<Deployment> deployments()
    {
        return deployments;
    }

    /**
     * @return True if this deployment set is empty
     */
    public boolean isEmpty()
    {
        return deployments.isEmpty();
    }

    /**
     * @return The number of deployments in this set
     */
    public int size()
    {
        return deployments.size();
    }

    /**
     * @param name The name of the switch
     * @return A switch parser with the given switch name that chooses among the deployments in this set
     */
    public SwitchParser.Builder<Deployment> switchParser(String name)
    {
        return Deployment.deploymentSwitchParser(this, this, name);
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
