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

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.core.collections.set.ConcurrentHashSet;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.settings.internal.lexakai.DiagramSettings;
import com.telenav.kivakit.settings.stores.ResourceFolderSettingsStore;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;

import java.util.Collection;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;
import static com.telenav.kivakit.properties.PropertyMap.loadPropertyMap;
import static com.telenav.kivakit.properties.PropertyMap.propertyMap;
import static com.telenav.kivakit.resource.packages.Package.parsePackage;
import static com.telenav.kivakit.settings.Deployment.deploymentSwitchParser;

/**
 * A set of {@link Deployment} objects, each being a set of settings objects. Deployments can be added to the set from a
 * folder with {@link #addDeploymentsIn(ResourceFolder)}. A switch parser to select a deployment from the command line
 * can be retrieved with {@link #deploymentSet(Listener, Deployment, Deployment...)}.
 *
 * <p><b>Loading</b></p>
 *
 * <ul>
 *     <li>{@link #loadDeploymentSet(Listener, Class)}</li>
 * </ul>
 *
 * <p><b>Creation</b></p>
 *
 * <ul>
 *     <li>{@link #deploymentSet(Listener, Deployment, Deployment...)}</li>
 * </ul>
 *
 * <p><b>Adding Deployments</b></p>
 *
 * <ul>
 *     <li>{@link #add(Deployment)}</li>
 *     <li>{@link #addAll(Collection)}</li>
 *     <li>{@link #addDeploymentsIn(ResourceFolder)}</li>
 * </ul>
 *
 * <p><b>Accessing Deployments</b></p>
 *
 * <ul>
 *     <li>{@link #deployment(String)}</li>
 *     <li>{@link #deployments()}</li>
 *     <li>{@link #switchParser(String)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Deployment
 * @see SettingsRegistry
 */
@SuppressWarnings({ "UnusedReturnValue", "unused" })
@UmlClassDiagram(diagram = DiagramSettings.class)
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class DeploymentSet extends BaseRepeater implements RegistryTrait
{
    /**
     * Returns a deployment set with the given deployments
     *
     * @param listener The listener to call with any problems
     * @param deployment The first deployment to add
     * @param more Further deployments to add
     * @return The deployment set
     */
    public static DeploymentSet deploymentSet(Listener listener, Deployment deployment, Deployment... more)
    {
        var set = listener.listenTo(new DeploymentSet());
        set.add(deployment);
        set.addAll(list(more));
        return set;
    }

    /**
     * Loads all deployments in the package 'deployments' relative to the given class' package, and in any folder
     * specified by KIVAKIT_DEPLOYMENT_FOLDER.
     */
    @SuppressWarnings("ConstantConditions")
    public static DeploymentSet loadDeploymentSet(Listener listener, Class<?> relativeTo)
    {
        // Create an empty set of deployments,
        var deployments = listener.listenTo(new DeploymentSet());

        // and if there is a root package called 'deployments' in the application,
        var settings = parsePackage(listener, relativeTo, "deployments");
        if (settings != null)
        {
            // then add all the deployments in that package,
            deployments.addDeploymentsIn(settings);
        }

        // and if a deployment folder was specified, and it exists,
        var deploymentFolder = propertyMap(javaVirtualMachine().systemProperties())
            .asFolder("KIVAKIT_DEPLOYMENT_FOLDER");
        if (deploymentFolder != null && deploymentFolder.exists())
        {
            // then add all the deployments in that folder.
            deployments.addDeploymentsIn(deploymentFolder);
        }

        return deployments;
    }

    /** The set of deployments */
    @UmlAggregation
    private final Set<Deployment> deployments = new ConcurrentHashSet<>();

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
        deployments.forEach(this::add);
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
            var deployment = listenTo(new Deployment(this, child.path().last(), description));

            // and add the configuration information from the sub-folder,
            deployment.addAll(listenTo(new ResourceFolderSettingsStore(this, child)));

            // add it to this set of deployments.
            add(deployment);
        }
        return this;
    }

    /**
     * Returns the named deployment
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
     * Returns the deployments in this set
     */
    public Set<Deployment> deployments()
    {
        return deployments;
    }

    /**
     * Returns true if this deployment set is empty
     */
    public boolean isEmpty()
    {
        return deployments.isEmpty();
    }

    /**
     * Returns the number of deployments in this set
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
        return deploymentSwitchParser(this, this, name);
    }

    private String description(Resource resource)
    {
        var description = "'" + resource.fileName().name() + "' deployment";
        var deploymentProperties = loadPropertyMap(this, resource);
        if (deploymentProperties.containsKey("description"))
        {
            description = deploymentProperties.get("description");
        }
        return description;
    }
}
