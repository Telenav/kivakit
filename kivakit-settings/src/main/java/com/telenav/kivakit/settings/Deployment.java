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
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.registry.InstanceIdentifier;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.resource.ResourceFolder;
import com.telenav.kivakit.settings.internal.lexakai.DiagramSettings;
import com.telenav.kivakit.settings.stores.MemorySettingsStore;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.Serializable;
import java.util.Objects;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.commandline.SwitchParser.switchParser;

/**
 * <p>
 * A deployment is a {@link Named} {@link SettingsStore} containing user-defined settings {@link Objects}.
 * </p>
 *
 * <p>
 * The name of a deployment can be retrieved with {@link #name()} and a description of its purpose with
 * {@link #description()}. The example below, has details on how settings information can be loaded and queried.
 * </p>
 *
 * <p><b>Deploying Applications</b></p>
 *
 * <p>
 * Deployments can be added to a {@link DeploymentSet} via {@link DeploymentSet#addDeploymentsIn(ResourceFolder)}. The
 * method {@link #deploymentSwitchParser(Listener, DeploymentSet, String)} will create a command line SwitchParser that
 * can select among several deployments in a {@link DeploymentSet} by name. This is handled automatically if the
 * application places deployments in the application-relative package "deployments".
 * </p>
 *
 * <p>
 * An application can then specify a deployment with the command line switch:
 * </p>
 *
 * <pre>
 * -deployment=production</pre>
 *
 * <p>
 * Here, settings objects in the "production" {@link Deployment} will be installed into the global settings registry. At
 * a later point, the application can look up those objects with {@link SettingsTrait#lookupSettings(Class)} and
 * {@link SettingsTrait#lookupSettings(Class, InstanceIdentifier)}, both of which are provided by the
 * <i>Component</i> class in the <i>kivakit-component</i> project.
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * The <i>Demo</i> application here loads a set of deployments, each of which is a set of <i>.properties</i> resources
 * in a sub-package of the "deployments" package next to the Demo class:
 * <pre>
 * Demo.class
 * deployments/
 *     development/
 *         DemoSettings.properties
 *         RouterSettings.properties
 *     production/
 *         DemoSettings.properties
 *         RouterSettings.properties</pre>
 *
 * <p>
 * It does this by using a {@link SwitchParser} from <i>kivakit-application</i> to allow the user to select a deployment
 * from the command line. The deployment is automatically installed in the global {@link SettingsRegistry} registry, and
 * these settings objects can easily be located in the code. The usage for the <i>Demo</i> application then, is:
 * </p>
 *
 * <pre>
 * java -jar Demo.jar -deployment=production</pre>
 *
 * <p>
 * In the deployments.development and deployments.production sub-packages, there will be properties files for each
 * deployment which are used to create and populate the required settings objects. One of the properties files in
 * deployments/development might look like this:
 * </p>
 *
 * <p><i>AwsServer.properties</i></p>
 *
 * <pre>
 * class=com.telenav.navigation.my.application.ServerSettings
 * port=aws.amazon.com:7001</pre>
 *
 * <p>
 * When KivaKit loads this properties file, it will instantiate the ServerSettings class specified by "class" and
 * configure the resulting object. It does this by using the {@literal @}ConvertedProperty annotation to convert the
 * value for the "port" key in the properties file into an object which it then passes to the annotated port(Port)
 * method:
 * </p>
 *
 * <p><i>ServerSettings</i></p>
 *
 * <pre>
 * public class ServerSettings
 * {
 *     private Port port;
 *
 *    {@literal @}ConvertedProperty(Port.Converter.class)
 *     public void port( Port port)
 *     {
 *         this.port = port;
 *     }
 *
 *        [...]
 * }</pre>
 *
 * <p>
 * Once the deployment's settings information is all loaded into the global settings registry, any component in the
 * project can then locate the configured object like this:
 * </p>
 *
 * <pre>
 * public class SomeOtherClass extends BaseComponent
 * {
 *     public void doIt()
 *     {
 *         var settings = requireSettings(ServerSettings.class);
 *
 *             [...]
 *     }
 * }</pre>
 *
 * <p>
 * For more details on how settings are processed and how applications can locate settings information, see
 * {@link SettingsRegistry}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see SettingsRegistry
 * @see SettingsStore
 * @see SettingsObject
 * @see DeploymentSet
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramSettings.class, excludeSuperTypes = { Serializable.class })
@TypeQuality(stability = STABLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Deployment extends MemorySettingsStore implements Serializable
{
    /**
     * Returns a switch parser to select deployments
     *
     * @param listener The listener to call with problems
     * @param deployments The set of deployments to select from
     * @param switchName The name of the switch
     * @return The switch parser builder
     */
    public static SwitchParser.Builder<Deployment> deploymentSwitchParser(Listener listener,
                                                                          DeploymentSet deployments,
                                                                          String switchName)
    {
        return switchParser(Deployment.class)
            .name("deployment")
            .validValues(deployments.deployments())
            .converter(new DeploymentConverter(listener, deployments))
            .description("The deployment configuration to run");
    }

    /** A description of the kind of deployment */
    private final String description;

    /** The name of this deployment */
    private final String name;

    /**
     * @param name The name of the deployment, like "osm-team"
     * @param description A description of the deployment
     */
    public Deployment(Listener listener, String name, String description)
    {
        listener.listenTo(this);

        this.name = name;
        this.description = description;
    }

    /**
     * Returns a description of the purpose of this deployment
     */
    public String description()
    {
        return description;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Deployment that)
        {
            return name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public String name()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return name() + " - " + description();
    }
}
