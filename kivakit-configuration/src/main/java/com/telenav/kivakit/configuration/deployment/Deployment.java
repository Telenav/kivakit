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

package com.telenav.kivakit.configuration.deployment;

import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.configuration.ConfigurationFolder;
import com.telenav.kivakit.configuration.ConfigurationPackage;
import com.telenav.kivakit.configuration.ConfigurationSet;
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.data.conversion.string.BaseStringConverter;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.io.Serializable;

/**
 * A deployment is a named {@link ConfigurationSet}. The name of the set can be retrieved with {@link #name()} and a
 * description of its purpose with {@link #description()}. The example below, as well as the superclass {@link
 * ConfigurationSet}, has details on how configuration information can be loaded and queried.
 *
 * <p><b>Configuring Applications</b></p>
 *
 * <p>
 * Deployments can be added to a {@link DeploymentSet} via {@link DeploymentSet#load(Listener, Class, String)} or {@link
 * DeploymentSet#load(Listener, Folder)} and then the method SwitchParser.deployment(DeploymentSet) will create a
 * command line SwitchParser that can select among several deployments in a {@link DeploymentSet} by name.
 * </p>
 *
 * <p>
 * For example, an application might specify a deployment with the command line switch "-deployment=navteam". The
 * configuration objects in the {@link Deployment} can then be installed into the global {@link Deployment} with {@link
 * #install()}. At a later point, the application can look up those objects with {@link #get(Class)} and {@link
 * #get(Class, InstanceIdentifier)}.
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * The application below loads a set of deployments each of which is a set of configuration .properties resources in a
 * sub-package of the "configuration" package next to the Demo class exists like this:
 * <pre>
 * Demo.class
 *     configuration/
 *         navteam/
 *             DemoSettings.properties
 *             RouterSettings.properties
 *         osmteam/
 *             DemoSettings.properties
 *             RouterSettings.properties
 * </pre>
 *
 * <p>
 * It does this by using a SwitchParser from kivakit-application to allow the user to select a deployment from the
 * command line. The deployment is automatically installed in the global {@link ConfigurationSet}, where configuration
 * objects can easily be located in the code. The usage for this application then, is:
 * </p>
 *
 * <pre>
 * java -jar Demo.jar -deployment=navteam
 * </pre>
 *
 * <pre>
 * public class Demo extends Application
 * {
 *     DeploymentSet deployments = DeploymentSet.load(Demo.class, "configuration");
 *
 *     SwitchParser&lt;Deployment&gt; DEPLOYMENT = deployments.deploymentSwitchParser();
 *
 *     public static void main(final String[] arguments)
 *     {
 *         new Demo.run(arguments);
 *     }
 *
 *     public void onRun(final CommandLine commandLine)
 *     {
 *         commandLine.get(DEPLOYMENT).install();
 *
 *             [...]
 *
 *         var settings = ConfigurationSet.global().require(DemoSettings.class);
 *     }
 *
 *     public Set&lt;SwitchParser&gt; switchParsers
 *     {
 *         return Set.of(DEPLOYMENT);
 *     }
 * }
 * </pre>
 *
 * <p>
 * In the configuration.local and configuration.navteam sub-packages, there will be properties files for each deployment
 * which are used to create and populate the required configuration objects. One of the properties files in
 * configuration/navteam might look like this:
 * </p>
 *
 * <p><i>AwsServer.properties</i></p>
 *
 * <pre>
 * class=com.telenav.navigation.my.application.ServerConfiguration
 * port=aws.amazon.com:7001
 * </pre>
 *
 * <p>
 * When KivaKit loads this properties file, it will instantiate the ServerConfiguration class specified by "class" and
 * configure the resulting object. It does this by using the {@literal @}KivaKitPropertyConverter annotation to convert
 * the value for the "port" key in the properties file into an object which it then passes to the annotated port(Port)
 * method:
 * </p>
 *
 * <p><i>ServerConfiguration</i></p>
 *
 * <pre>
 * public class Configuration
 * {
 *     private Port port;
 *
 *    {@literal @}KivaKitPropertyConverter(Port.Converter.class)
 *     public void port(final Port port)
 *     {
 *         this.port = port;
 *     }
 *
 *        [...]
 * }
 * </pre>
 *
 * <p>
 * Once the deployment's configuration information is all loaded into the global configuration set, any class in the
 * project can then locate the configured object like this:
 * </p>
 *
 * <pre>
 * public class SomeOtherClass
 * {
 *     public void doIt()
 *     {
 *         var configuration = BaseDeployment.get().get(ServerConfiguration.class);
 *
 *             [...]
 *     }
 * }
 * </pre>
 *
 * <p><b>Key Methods</b></p>
 *
 * <ul>
 *     <li>{@link #addPackage(Class, String)} - Adds the package of .properties files at the path relative to the given class</li>
 *     <li>{@link #addFolder(Folder)} - Adds the folder of .properties files</li>
 *     <li>{@link #addDeployment(Deployment)} - Merges another deployment into this one</li>
 *     <li>{@link #install()} - Installs the contents of this {@link Deployment} into the global configuration set</li>
 * </ul>
 *
 * <p>
 * For more details on how configuration files are processed and how applications can locate configuration information,
 * see {@link ConfigurationSet}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see ConfigurationSet
 * @see DeploymentSet
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class, excludeSuperTypes = { Serializable.class })
public class Deployment extends ConfigurationSet implements Named, Serializable
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static SwitchParser<Deployment> deploymentSwitchParser(final DeploymentSet deployments,
                                                                  final String switchName)
    {
        return SwitchParser.builder(Deployment.class)
                .name("deployment")
                .validValues(deployments.deployments())
                .converter(new Deployment.Converter(LOGGER, deployments))
                .description("The deployment configuration to run")
                .required()
                .build();
    }

    /**
     * Converts to and from a {@link Deployment}
     *
     * @author jonathanl (shibo)
     */
    @LexakaiJavadoc(complete = true)
    public static class Converter extends BaseStringConverter<Deployment>
    {
        private final DeploymentSet deployments;

        public Converter(final Listener listener, final DeploymentSet deployments)
        {
            super(listener);
            this.deployments = deployments;
        }

        @Override
        protected Deployment onToValue(final String value)
        {
            if (value != null)
            {
                final var deployment = deployments.deployment(value);
                if (deployment != null)
                {
                    return deployment;
                }
                problem("No deployment called '$'", value);
            }
            return null;
        }
    }

    /** The name of this deployment */
    private final String name;

    /** A description of the kind of deployment */
    private final String description;

    /**
     * @param name The name of the deployment, like "osm-team"
     * @param description A description of the deployment
     */
    public Deployment(final String name, final String description)
    {
        this.name = name;
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Deployment add(final Object configuration, final InstanceIdentifier instance)
    {
        return (Deployment) super.add(configuration, instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Deployment add(final Object configuration, final Enum<?> instance)
    {
        return add(configuration, new InstanceIdentifier(instance));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Deployment add(final Object configuration)
    {
        assert !(configuration instanceof PackagePath) && !(configuration instanceof Folder) :
                "Should have called loadAll with the argument " + configuration;
        super.add(configuration);
        return this;
    }

    /**
     * Adds all of the configurations in the given deployment to this deployment
     */
    @Override
    public Deployment addDeployment(final Deployment deployment)
    {
        super.internalAddAll(deployment);
        return this;
    }

    @Override
    public Deployment addFolder(final Folder folder)
    {
        addSet(listenTo(new ConfigurationFolder(folder)));
        return this;
    }

    @Override
    public Deployment addPackage(final PackagePath path)
    {
        addSet(listenTo(ConfigurationPackage.of(path)));
        return this;
    }

    @Override
    public Deployment addPackage(final Class<?> relativeTo, final String path)
    {
        addSet(listenTo(ConfigurationPackage.of(PackagePath.parsePackagePath(relativeTo, path))));
        return this;
    }

    /**
     * @return A description of the purpose of this deployment
     */
    public String description()
    {
        return description;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (object instanceof Deployment)
        {
            final var that = (Deployment) object;
            return name.equals(that.name);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    /**
     * Installs the configuration information for this deployment into the global {@link ConfigurationSet}
     */
    @Override
    @UmlExcludeMember
    public Deployment install()
    {
        super.install();
        return this;
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
