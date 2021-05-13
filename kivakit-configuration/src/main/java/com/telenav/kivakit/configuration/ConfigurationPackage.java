////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.configuration;

import com.telenav.kivakit.configuration.project.lexakai.diagrams.DiagramConfiguration;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.resource.path.Extension;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A set of configuration objects stored in a package. The package contains a set of .properties files that are used to
 * instantiate and populate the configuration objects.
 *
 * <p>
 * <i>See the superclass {@link ConfigurationSet} for details on how this works.</i>
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ClassEscapesDefinedScope")
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class ConfigurationPackage extends ConfigurationSet
{
    private static final Map<PackagePath, ConfigurationPackage> packages = new HashMap<>();

    public static ConfigurationPackage of(final Package _package)
    {
        return of(_package.path());
    }

    public static ConfigurationPackage of(final PackagePath path)
    {
        return packages.computeIfAbsent(path, ignored -> new ConfigurationPackage(path));
    }

    /** The path of this configuration package */
    private final PackagePath path;

    /** The configuration entries in this package */
    private Set<ConfigurationEntry> configurations;

    /**
     * @param path The path to the package where the configurations are stored
     */
    protected ConfigurationPackage(final PackagePath path)
    {
        this.path = path;
    }

    @Override
    public String name()
    {
        return "[ConfigurationPackage package = " + path + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @UmlExcludeMember
    protected Set<ConfigurationEntry> onLoadConfigurations()
    {
        if (configurations == null)
        {
            configurations = new HashSet<>();

            // Go through .properties files in the package
            final var _package = Package.of(path);
            trace("Loading resources from $", _package);
            for (final var resource : _package.resources(Extension.PROPERTIES::matches))
            {
                // load the properties file
                final var configuration = internalLoadConfiguration(resource);
                if (configuration != null)
                {
                    // and add the configuration
                    configurations.add(configuration);
                }
            }
            trace("Loaded $ resources", configurations.size());
        }

        return configurations;
    }
}
