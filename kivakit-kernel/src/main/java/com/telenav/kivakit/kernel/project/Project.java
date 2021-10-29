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

package com.telenav.kivakit.kernel.project;

import com.telenav.cactus.build.metadata.Metadata;
import com.telenav.kivakit.kernel.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.kernel.interfaces.naming.Named;
import com.telenav.kivakit.kernel.language.collections.map.string.VariableMap;
import com.telenav.kivakit.kernel.language.collections.set.ObjectSet;
import com.telenav.kivakit.kernel.language.objects.Lazy;
import com.telenav.kivakit.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.kernel.language.time.LocalTime;
import com.telenav.kivakit.kernel.language.values.version.Version;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Debug;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramProject;
import com.telenav.lexakai.annotations.LexakaiJavadoc;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for KivaKit projects, enabling run-time dependency management and initialization.
 *
 * <p><b>Dependencies and Initialization</b></p>
 *
 * <p>
 * Projects may declare dependencies on other projects that can be retrieved with {@link #dependencies()}. These
 * dependent projects can be visited with {@link #visitDependencies(Visitor)}, and initialization of a project with
 * {@link #initialize()} will cause transitive initialization of all dependencies in depth-first order. Subclasses of
 * {@link Project} can provide initialization behavior by overriding {@link #onInitialize()}.
 * </p>
 *
 * <p><b>Important Note: Project Initialization</b></p>
 *
 * <p>
 * <i>Projects that define a {@link Project} subclass, normally in the 'project' package of the project, should be
 * initialized by calling {@link Project#initialize()} before being used or undefined behavior can result. All KivaKit
 * projects define a {@link Project} subclass. Project initialization occurs automatically for Applications.</i>
 * </p>
 *
 * <p><b>Properties</b></p>
 *
 * <p>
 * Projects have several properties that can be queried:
 * </p>
 *
 * <ul>
 *     <li>{@link #build()} - Build information</li>
 *     <li>{@link #projectVersion()} - The project version</li>
 *     <li>{@link #kivakitVersion()} - The version of KivaKit used by the project</li>
 *     <li>{@link #properties()} - System properties, environment variables and build properties for this project</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Version
 */
@UmlClassDiagram(diagram = DiagramProject.class)
@UmlExcludeSuperTypes({ Named.class })
public abstract class Project extends BaseRepeater implements Initializable, Named
{
    private static final Lazy<Logger> LOGGER = Lazy.of(LoggerFactory::newLogger);

    private static final Lazy<Debug> DEBUG = Lazy.of(() -> new Debug(LOGGER.get()));

    /**
     * Visitor interface for use with {@link #visitDependencies(Visitor)}
     */
    @FunctionalInterface
    @LexakaiJavadoc(complete = true)
    public interface Visitor
    {
        /**
         * @param at The project we are at
         * @param level The level of recursion from the root
         */
        void at(Project at, int level);
    }

    /** True if this project has been initialized */
    private boolean initialized;

    /** The properties for this project */
    private VariableMap<String> properties;

    /**
     * @return The maven artifactId of this project
     */
    public String artifactId()
    {
        return property("project-artifact-id");
    }

    /**
     * @return Build information about this project
     */
    public Build build()
    {
        return Build.build(getClass());
    }

    /**
     * @return The set of dependent projects for this project
     */
    @UmlRelation(label = "depends on")
    public ObjectSet<Project> dependencies()
    {
        return ObjectSet.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object object)
    {
        if (object instanceof Project)
        {
            var that = (Project) object;
            return getClass().equals(that.getClass());
        }
        return false;
    }

    /**
     * @return The maven groupId for this project
     */
    public String groupId()
    {
        return property("project-group-id");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode()
    {
        return getClass().hashCode();
    }

    /**
     * Initialize this project for use. This project and all dependent projects will be initialized. This method must be
     * called before using any classes or methods in the project.
     */
    @Override
    public final void initialize()
    {
        // If we haven't already initialized,
        if (!initialized)
        {
            // signal we are about to initialize,
            initialized = true;
            onInitializing();

            // then initialize all dependencies,
            visitDependencies((project, level) -> project.initialize());

            // initialize the project
            DEBUG.get().trace("Initializing ${class}", getClass());
            onInitialize();

            // and signal that we are done initializing.
            onInitialized();
        }
    }

    /**
     * @return The KivaKit version in use
     */
    public final Version kivakitVersion()
    {
        return Version.parse(property("kivakit-version"));
    }

    /**
     * @return The maven project name for this project
     */
    @Override
    public String name()
    {
        return property("project-name");
    }

    /**
     * This may be overridden to initialize a project
     */
    public void onInitialize()
    {
    }

    public void onInitialized()
    {
    }

    public void onInitializing()
    {
    }

    /**
     * @return The version of this project
     */
    public Version projectVersion()
    {
        return Version.parse(property("project-version"));
    }

    /**
     * @return System properties, environment variables and build properties for this project
     */
    public VariableMap<String> properties()
    {
        if (properties == null)
        {
            var projectProperties = Metadata.of(getClass()).projectProperties();

            var properties = JavaVirtualMachine.local().variables();
            properties.addAll(VariableMap.of(projectProperties));
            properties.put("kivakit-version", properties.get("project-version"));
            properties.put("version", properties.get("project-version"));
            properties.putIfNotNull("build-name", build().name());
            properties.putIfNotNull("build-date", build().formattedDate());
            properties.putIfNotNull("build-number", Integer.toString(build().number()));
            properties.put("date-and-time", LocalTime.now().asDateTimeString());

            this.properties = properties.expanded();
        }

        return properties;
    }

    /**
     * @return The project property for the given key
     */
    public String property(String key)
    {
        return properties().get(key);
    }

    @Override
    public String toString()
    {
        var builder = new StringBuilder();
        visitDependencies((project, level) -> builder
                .append(AsciiArt.repeat(level * 2, ' '))
                .append(project.name())
                .append("\n"));
        return builder.toString();
    }

    /**
     * Visits this project and all its dependencies in depth-first order
     *
     * @param visitor The visitor called for each project
     */
    public void visitDependencies(Visitor visitor)
    {
        visitDependencies(this, new HashSet<>(), visitor, 0);
    }

    private void visitDependencies(Project project,
                                   Set<Project> visited,
                                   Visitor visitor,
                                   int level)
    {
        // If we haven't already visited the given project,
        if (!visited.contains(project))
        {
            // prevent loops
            visited.add(project);

            // then go through each dependency
            for (var dependency : dependencies())
            {
                // and visit it,
                dependency.visitDependencies(dependency, visited, visitor, level + 1);
            }

            // then call the visitor.
            visitor.at(project, level);
        }
    }
}
