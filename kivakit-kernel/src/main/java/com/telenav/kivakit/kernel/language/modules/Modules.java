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

package com.telenav.kivakit.kernel.language.modules;

import com.telenav.kivakit.kernel.interfaces.comparison.Matcher;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.matchers.AnythingMatcher;
import com.telenav.kivakit.kernel.language.paths.PackagePath;
import com.telenav.kivakit.kernel.language.paths.StringPath;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageModules;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.io.IOException;
import java.lang.module.ModuleReference;
import java.lang.module.ResolvedModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides easy access to module resources. The {@link #resource(Listener listener, StringPath)}, {@link
 * #resources(Listener listener, PackagePath)}, {@link #nestedResources(Listener listener, PackagePath)} and {@link
 * #nestedResources(Listener listener, PackagePath, Matcher)} methods find resources and return them as {@link
 * ModuleResource} object(s).
 *
 * @author jonathanl (shibo)
 * @see ModuleResource
 */
@UmlClassDiagram(diagram = DiagramLanguageModules.class)
public class Modules
{
    /**
     * All module references in the boot module layer
     */
    private static List<ModuleReference> references;

    /**
     * Cached list of module resources for each package path
     */
    private static final Map<PackagePath, List<ModuleResource>> allResourcesUnder = new HashMap<>();

    /**
     * List of all resources within the given search scopes
     */
    private static List<ModuleResource> allResources;

    /**
     * @return A list of all {@link ModuleResource}s under the given package
     */
    public static synchronized List<ModuleResource> allNestedResources(Listener listener, PackagePath _package)
    {
        listener.trace("Finding all nested resources in $", _package);
        var all = allResourcesUnder.get(_package);
        if (all == null)
        {
            all = new ArrayList<>();
            for (var resource : allResources(listener))
            {
                if (_package.containsNested(resource))
                {
                    all.add(resource);
                }
            }
            allResourcesUnder.put(_package, all);
        }
        listener.trace("Found $ nested resources in $:\n$", all.size(), _package, new StringList(all).join("\n"));
        return all;
    }

    /**
     * @return A list of all resources within the current search scope
     */
    static synchronized List<ModuleResource> allResources(Listener listener)
    {
        listener.trace("Finding all resources");
        if (allResources == null)
        {
            allResources = new ArrayList<>();
            references(listener).forEach(reference ->
            {
                var location = reference.location().orElse(null);
                listener.trace("Looking in $", location);
                if (location != null && !"jrt".equals(location.getScheme()))
                {
                    try (var reader = reference.open())
                    {
                        reader.list().forEach(path ->
                        {
                            try
                            {
                                var optional = reader.find(path);
                                if (optional.isPresent())
                                {
                                    var uri = optional.get();
                                    var resource = ModuleResource.moduleResource(listener, reference, uri);
                                    if (resource != null)
                                    {
                                        listener.trace("Found resource $.$", resource.packagePath(), resource.fileNameAsJavaPath());
                                        allResources.add(resource);
                                    }
                                }
                            }
                            catch (IOException ignored)
                            {
                                listener.warning("Unable to read resource $ in module $", path, reference);
                            }
                        });
                    }
                    catch (IOException e)
                    {
                        listener.problem(e, "Unable to read module $", reference);
                    }
                }
                else
                {
                    listener.trace("Ignored $", location);
                }
            });
        }

        return allResources;
    }

    /**
     * @return A list of {@link ModuleResource}s under the given package that match the given matcher
     */
    public synchronized static List<ModuleResource> nestedResources(Listener listener,
                                                                    PackagePath _package,
                                                                    Matcher<ModuleResource> matcher)
    {
        return allNestedResources(listener, _package)
                .stream()
                .filter(matcher)
                .collect(Collectors.toList());
    }

    /**
     * @return A list of all {@link ModuleResource}s under the given package
     */
    public synchronized static List<ModuleResource> nestedResources(Listener listener, PackagePath _package)
    {
        return nestedResources(listener, _package, new AnythingMatcher<>());
    }

    /**
     * @return A single {@link ModuleResource} for the given path or null if no resource is found
     */
    public static ModuleResource resource(Listener listener, StringPath path)
    {
        listener.trace("Locating module resource at $", path);

        // Go through each resolved module in the module system
        for (var reference : references(listener))
        {
            var location = reference.location().orElse(null);
            listener.trace("Looking in $", location);
            if (location != null)
            {
                if (!"jrt".equals(location.getScheme()))
                {
                    try (var reader = reference.open())
                    {
                        // and if the resource can be found,
                        var uri = reader.find(path.join("/")).orElse(null);
                        if (uri != null)
                        {
                            // return it
                            listener.trace("Found module resource for $ at $", path, uri);
                            return ModuleResource.moduleResource(listener, reference, uri);
                        }
                    }
                    catch (IOException ignored)
                    {
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return A list of the {@link ModuleResource}s in the given package (but not any below it)
     */
    public static List<ModuleResource> resources(Listener listener, PackagePath _package)
    {
        return nestedResources(listener, _package, _package::contains);
    }

    /**
     * @return A list of all available {@link ModuleReference}s
     */
    private synchronized static List<ModuleReference> references(Listener listener)
    {
        if (references == null)
        {
            references = ModuleLayer.boot()
                    .configuration()
                    .modules()
                    .stream()
                    .map(ResolvedModule::reference)
                    .collect(Collectors.toList());

            references.forEach(reference -> listener.trace("Found module $", reference.descriptor().name()));
        }
        return references;
    }
}
