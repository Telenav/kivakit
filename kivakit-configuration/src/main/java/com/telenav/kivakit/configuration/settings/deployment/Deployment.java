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
import com.telenav.kivakit.configuration.lookup.InstanceIdentifier;
import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.configuration.settings.Settings;
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
 * A deployment is a named {@link Settings} registry. The name of the set can be retrieved with {@link #name()} and a
 * description of its purpose with {@link #description()}. The example below, as well as the superclass {@link
 * Settings}, has details on how configuration information can be loaded and queried.
 *
 * <p><b>Deploying Applications</b></p>
 *
 * <p>
 * Deployments can be added to a {@link DeploymentSet} via {@link DeploymentSet#addDeploymentsIn(Class, String)} or
 * {@link DeploymentSet#addDeploymentsIn(Folder)} and then the method SwitchParser.deployment(DeploymentSet) will create
 * a command line SwitchParser that can select among several deployments in a {@link DeploymentSet} by name. This is
 * handled automatically if the application places deployments in the application-relative package "deployments".
 * </p>
 *
 * <p>
 * For example, an application might specify a deployment with the command line switch "-deployment=production". The
 * configuration objects in the {@link Deployment} can then be installed into the global {@link Deployment} with {@link
 * #install()}. At a later point, the application can look up those objects with {@link #lookupSettings(Class)} and
 * {@link #lookupSettings(Class, InstanceIdentifier)}.
 * </p>
 *
 * <p><b>Example</b></p>
 *
 * <p>
 * The application below loads a set of deployments, each of which is a set of <i>.properties</i> resources in a
 * sub-package of the "deployments" package next to the Demo class:
 * <pre>
 * Demo.class
 *     deployments/
 *         development/
 *             DemoSettings.properties
 *             RouterSettings.properties
 *         production/
 *             DemoSettings.properties
 *             RouterSettings.properties
 * </pre>
 *
 * <p>
 * It does this by using a SwitchParser from kivakit-application to allow the user to select a deployment from the
 * command line. The deployment is automatically installed in the global {@link Settings}, where configuration objects
 * can easily be located in the code. The usage for this application then, is:
 * </p>
 *
 * <pre>
 * java -jar Demo.jar -deployment=production
 * </pre>
 *
 * <p>
 * In the deployments.development and deployments.production sub-packages, there will be properties files for each
 * deployment which are used to create and populate the required configuration objects. One of the properties files in
 * deployments/development might look like this:
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
 * Once the deployment's configuration information is all loaded into the global settings registry, any component in the
 * project can then locate the configured object like this:
 * </p>
 *
 * <pre>
 * public class SomeOtherClass extends BaseComponent
 * {
 *     public void doIt()
 *     {
 *         var configuration = require(ServerConfiguration.class);
 *
 *             [...]
 *     }
 * }
 * </pre>
 *
 * <p><b>Key Methods</b></p>
 *
 * <ul>
 *     <li>{@link #registerAllSettingsIn(Listener, Class, String)} - Adds the package of .properties files at the path relative to the given class</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Folder)} - Adds the folder of .properties files</li>
 *     <li>{@link #addDeployment(Deployment)} - Merges another deployment into this one</li>
 *     <li>{@link #install()} - Installs the contents of this {@link Deployment} into the global settings registry</li>
 * </ul>
 *
 * <p>
 * For more details on how configuration files are processed and how applications can locate configuration information,
 * see {@link Settings}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Settings
 * @see DeploymentSet
 */
@UmlClassDiagram(diagram = DiagramConfiguration.class, excludeSuperTypes = { Serializable.class })
public class Deployment extends Settings implements Named, Serializable
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    public static SwitchParser.Builder<Deployment> deploymentSwitchParser(final DeploymentSet deployments,
                                                                          final String switchName)
    {
        return SwitchParser.builder(Deployment.class)
                .name("deployment")
                .validValues(deployments.deployments())
                .converter(new Deployment.Converter(LOGGER, deployments))
                .description("The deployment configuration to run");
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
     * Adds all of the configurations in the given deployment to this deployment
     */
    public Deployment addDeployment(final Deployment deployment)
    {
        super.internalAddAll(deployment);
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
     * Installs the configuration information for this deployment into the global {@link Settings}
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
    public Deployment registerAllSettingsIn(final Listener listener, final Folder folder)
    {
        super.registerAllSettingsIn(listener, folder);
        return this;
    }

    @Override
    public Deployment registerAllSettingsIn(final Listener listener, final PackagePath path)
    {
        super.registerAllSettingsIn(listener, path);
        return this;
    }

    @Override
    public Deployment registerAllSettingsIn(final Listener listener, final Class<?> relativeTo, final String path)
    {
        super.registerAllSettingsIn(listener, relativeTo, path);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Deployment registerSettings(final Object settings, final InstanceIdentifier instance)
    {
        return (Deployment) super.registerSettings(settings, instance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Deployment registerSettings(final Object settings, final Enum<?> instance)
    {
        return registerSettings(settings, InstanceIdentifier.of(instance));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Deployment registerSettings(final Object settings)
    {
        assert !(settings instanceof PackagePath) && !(settings instanceof Folder) :
                "Should have called loadAll with the argument " + settings;
        super.registerSettings(settings);
        return this;
    }

    @Override
    public String toString()
    {
        return name() + " - " + description();
    }
}
