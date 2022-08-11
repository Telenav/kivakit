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

package com.telenav.kivakit.core.language.module;

import com.telenav.kivakit.core.internal.lexakai.DiagramModule;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.interfaces.comparison.Matcher;
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
 * Provides easy access to module resources. The {@link #moduleResource(Listener listener, StringPath)}, {@link
 * #moduleResources(Listener listener, PackageReference)}, {@link #nestedModuleResources(Listener listener,
 * PackageReference)} and {@link #nestedModuleResources(Listener listener, PackageReference, Matcher)} methods find
 * resources and return them as {@link ModuleResource} object(s).
 *
 * @author jonathanl (shibo)
 * @see ModuleResource
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramModule.class)
public class Modules
{
    /**
     * All module references in the boot module layer
     */
    private static List<ModuleReference> references;

    /**
     * Cached list of module resources for each package path
     */
    private static final Map<PackageReference, List<ModuleResource>> allResourcesUnder = new HashMap<>();

    /**
     * List of all resources within the given search scopes
     */
    private static List<ModuleResource> allResources;

    /**
     * @return A list of all resources within the current search scope
     */
    static synchronized List<ModuleResource> allModuleResources(Listener listener)
    {
        listener.trace("Finding all resources");
        if (allResources == null)
        {
            allResources = new ArrayList<>();
            moduleReferences(listener).forEach(reference ->
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
                                        listener.trace("Found resource $.$", resource.packageReference(), resource.fileNameAsJavaPath());
                                        if (!resource.uri().toString().endsWith("/"))
                                        {
                                            allResources.add(resource);
                                        }
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
     * @return A list of all {@link ModuleResource}s under the given package
     */
    public static synchronized List<ModuleResource> allNestedModuleResources(Listener listener,
                                                                             PackageReference packageReference)
    {
        listener.trace("Finding all nested resources in $", packageReference);
        var all = allResourcesUnder.get(packageReference);
        if (all == null)
        {
            all = new ArrayList<>();
            for (var resource : allModuleResources(listener))
            {
                if (packageReference.containsNested(resource))
                {
                    all.add(resource);
                }
            }
            allResourcesUnder.put(packageReference, all);
        }
        return all;
    }

    /**
     * @return A single {@link ModuleResource} for the given path or null if no resource is found
     */
    public static ModuleResource moduleResource(Listener listener, StringPath path)
    {
        listener.trace("Locating module resource at $", path);

        // Go through each resolved module in the module system
        for (var reference : moduleReferences(listener))
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
    public static List<ModuleResource> moduleResources(Listener listener, PackageReference packageReference)
    {
        return nestedModuleResources(listener, packageReference, packageReference::contains);
    }

    /**
     * @return A list of {@link ModuleResource}s under the given package that match the given matcher
     */
    public synchronized static List<ModuleResource> nestedModuleResources(Listener listener,
                                                                          PackageReference packageReference,
                                                                          Matcher<ModuleResource> matcher)
    {
        return allNestedModuleResources(listener, packageReference)
                .stream()
                .filter(matcher.asPredicate())
                .collect(Collectors.toList());
    }

    /**
     * @return A list of all {@link ModuleResource}s under the given package
     */
    public synchronized static List<ModuleResource> nestedModuleResources(Listener listener, PackageReference packageReference)
    {
        return nestedModuleResources(listener, packageReference, Matcher.matchAll());
    }

    /**
     * @return A list of all available {@link ModuleReference}s
     */
    private synchronized static List<ModuleReference> moduleReferences(Listener listener)
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
