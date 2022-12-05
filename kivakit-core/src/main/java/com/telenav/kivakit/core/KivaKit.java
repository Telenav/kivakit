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

package com.telenav.kivakit.core;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramProject;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.path.StringPath;
import com.telenav.kivakit.core.project.Project;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.nio.file.Path;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.ensure.Ensure.fail;
import static com.telenav.kivakit.core.logging.LoggerFactory.newLogger;
import static com.telenav.kivakit.core.path.StringPath.parseStringPath;

/**
 * This class defines a KivaKit {@link Project}. It cannot be constructed with the new operator since it has a private
 * constructor. To access the singleton instance of this class, call {@link Project#resolveProject(Class)}, or use
 * {@link com.telenav.kivakit.core.project.ProjectTrait#project(Class)}.
 *
 * <p>
 * Information about KivaKit, including the home folder, the cache folder and the framework version. Since
 * {@link KivaKit} is a {@link Project} it inherits that functionality as well.
 * </p>
 *
 * <p><b>Global Listener</b></p>
 *
 * <p>
 * The global listener is accessible via {@link #globalListener()}, and can be replaced with
 * {@link #globalListener(Listener)}. Messages can be sent to the global listener when there is no other option in the
 * current code context. By default, the global listener is the global logger, but the Application class replaces that
 * so that it gets all messages and routes them to its own logger.
 * </p>
 *
 * <p><b>Global Logger</b></p>
 *
 * <p>
 * The global logger is accessible with {@link #globalLogger()}. It can be replaced with {@link #globalLogger(Logger)}.
 * </p>
 *
 * @author jonathanl (shibo)
 * @see Project
 * @see Path
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramProject.class)
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTATION_COMPLETE)
public class KivaKit extends Project
{
    /** The global logger, for cases like collections where it's too cumbersome to require listeners */
    private static Logger globalLogger;

    /** The global listener */
    private static Listener globalListener;

    /**
     * Sets the global listener
     *
     * @param listener The new global listener
     */
    public static synchronized void globalListener(Listener listener)
    {
        KivaKit.globalListener = listener;
    }

    /**
     * Returns the global listener
     */
    public static synchronized Listener globalListener()
    {
        if (globalListener == null)
        {
            globalListener = globalLogger();
        }
        return globalListener;
    }

    /**
     * Returns a logger instance for use in contexts where it is too awkward to implement or pass in a {@link Listener}.
     * For example, some trivial classes and static methods may need to report problems, but are not important enough to
     * justify the complexity of reporting those problems an external listener.
     */
    public static synchronized Logger globalLogger()
    {
        if (globalLogger == null)
        {
            globalLogger = newLogger();
        }
        return globalLogger;
    }

    /**
     * Sets the global logger
     *
     * @param logger The logger
     */
    public static synchronized void globalLogger(Logger logger)
    {
        globalLogger = logger;
    }

    /**
     * Returns the cache folder for KivaKit
     */
    public StringPath kivakitCacheFolderPath()
    {
        var version = projectVersion();
        if (version != null)
        {
            return parseStringPath(this, System.getProperty("user.home"), ".kivakit", version.toString());
        }
        fail("Unable to get version for cache folder");
        return null;
    }

    /**
     * The easiest way to set KIVAKIT_HOME for Eclipse and other applications is to put this line in your .profile:
     *
     * <pre>
     *  launchctl setenv KIVAKIT_HOME $KIVAKIT_HOME
     * </pre>
     *
     * @return Path to KivaKit home if it's available in the environment or as a system property.
     */
    public StringPath kivakitHomeFolderPath()
    {
        var home = systemPropertyOrEnvironmentVariable("KIVAKIT_HOME");
        if (home == null)
        {
            return null;
        }
        return parseStringPath(this, home, "/");
    }
}
