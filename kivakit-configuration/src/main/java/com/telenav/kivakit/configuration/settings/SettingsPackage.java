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

package com.telenav.kivakit.configuration.settings;

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
 * <i>See the superclass {@link Settings} for details on how this works.</i>
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("ClassEscapesDefinedScope")
@UmlClassDiagram(diagram = DiagramConfiguration.class)
public class SettingsPackage extends Settings
{
    private static final Map<PackagePath, SettingsPackage> packages = new HashMap<>();

    public static SettingsPackage of(Package _package)
    {
        return of(_package.path());
    }

    public static SettingsPackage of(PackagePath path)
    {
        return packages.computeIfAbsent(path, ignored -> new SettingsPackage(path));
    }

    /** The path of this configuration package */
    private final PackagePath path;

    /**
     * @param path The path to the package where the configurations are stored
     */
    protected SettingsPackage(PackagePath path)
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
    protected Set<Entry> onLoad()
    {
        // Go through .properties files in the package
        var _package = Package.packageFrom(path);
        trace("Loading resources from $", _package);
        Set<Entry> entries = new HashSet<>();
        for (var resource : _package.resources(Extension.PROPERTIES::ends))
        {
            // load the properties file
            var configuration = internalLoadConfiguration(resource);
            if (configuration != null)
            {
                // and add the configuration
                entries.add(configuration);
            }
        }
        trace("Loaded $ resources", entries.size());
        return entries;
    }
}
