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

package com.telenav.kivakit.core.project;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.internal.lexakai.DiagramProject;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.object.LazyMap;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.string.Align;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.core.vm.Properties;
import com.telenav.kivakit.interfaces.lifecycle.Initializable;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

import java.util.HashSet;
import java.util.Set;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

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
 * <p><b>Project Initialization</b></p>
 *
 * <ul>
 *     <li>{@link #initialize()} - Initializes this project</li>
 *     <li>{@link #onInitializing()} - Called before initialization</li>
 *     <li>{@link #onInitialize()} - Called to initialize</li>
 *     <li>{@link #onInitialized()} - Called after initializing</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <p>
 * Projects have several properties that can be queried:
 * </p>
 *
 * <ul>
 *     <li>{@link #artifactId()} - The Maven artifact id for this project</li>
 *     <li>{@link #build()} - Build information</li>
 *     <li>{@link #dependencies()} - Dependent projects</li>
 *     <li>{@link #groupId()} - The Maven group id for this project</li>
 *     <li>{@link #kivakit()} - The KivaKit project</li>
 *     <li>{@link #kivakitVersion()} - The version of KivaKit used by the project</li>
 *     <li>{@link #projectVersion()} - The project version</li>
 *     <li>{@link #properties()} - System properties, environment variables and build properties for this project</li>
 *     <li>{@link #property(String)} - The given project property, system property or environment variable</li>
 *     <li>{@link #visitDependencies(Visitor)} - Calls the given visitor for each dependency</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Version
 * @see Initializable
 * @see JavaTrait
 * @see ProjectTrait
 * @see RegistryTrait
 */
@UmlClassDiagram(diagram = DiagramProject.class)
@UmlExcludeSuperTypes({ Named.class })
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
public abstract class Project extends BaseRepeater implements
        Initializable,
        Named,
        NamedObject,
        JavaTrait,
        ProjectTrait,
        RegistryTrait
{
    /** Map from project class to project instance used by {@link #resolveProject(Class)} */
    private static final LazyMap<Class<? extends Project>, Project> projects = LazyMap.of(Project::newProject);

    /**
     * Resolves the given Project class to a singleton instance.
     *
     * @param type The project class
     * @return The singleton project object
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T extends Project> T resolveProject(Class<T> type)
    {
        return (T) projects.get(type);
    }

    /**
     * Visitor interface for use with {@link #visitDependencies(Visitor)}
     */
    @FunctionalInterface
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

    /**
     * Returns the maven artifactId of this project
     */
    public String artifactId()
    {
        return property("project-artifact-id");
    }

    /**
     * Returns build information about this project
     */
    public Build build()
    {
        return Build.build(getClass());
    }

    /**
     * Returns the set of projects that depend on this project
     *
     * @return The set of dependent projects for this project
     */
    @UmlRelation(label = "depends on")
    public ObjectSet<Class<? extends Project>> dependencies()
    {
        return ObjectSet.emptyObjectSet();
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
     * Returns the maven groupId for this project
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

            // initialize the project
            if (!StartUpOptions.isEnabled(StartUpOptions.StartupOption.QUIET))
            {
                announce("Loading$ $ build $",
                        Align.right(getClass().getSimpleName(), 40, '.'),
                        Align.left(projectVersion().toString(), 20, '.'), build());
            }
            onInitialize();

            // and signal that we are done initializing.
            onInitialized();
        }
    }

    /**
     * Returns the KivaKit version in use
     */
    @Override
    public final Version kivakitVersion()
    {
        return kivakit().projectVersion();
    }

    /**
     * Returns the maven project name for this project
     */
    @Override
    public String name()
    {
        return property("project-name");
    }

    /**
     * This may be overridden to initialize a project
     */
    @Override
    public void onInitialize()
    {
    }

    /**
     * Called after project initialization
     */
    public void onInitialized()
    {
    }

    /**
     * Called before project initialization
     */
    public void onInitializing()
    {
    }

    /**
     * Returns the version of this project
     */
    public Version projectVersion()
    {
        return Version.parseVersion(this, property("project-version"));
    }

    /**
     * Returns system properties, environment variables and build properties for this project
     */
    public VariableMap<String> properties()
    {
        return Properties.allProperties(getClass());
    }

    /**
     * Returns the project property for the given key
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

    /**
     * Creates the Project object of the given type
     *
     * @param type The project type
     */
    private static Project newProject(Class<? extends Project> type)
    {
        return Classes.newInstance(type);
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
                visitDependencies(resolveProject(dependency), visited, visitor, level + 1);
            }

            // then call the visitor.
            visitor.at(project, level);
        }
    }
}
