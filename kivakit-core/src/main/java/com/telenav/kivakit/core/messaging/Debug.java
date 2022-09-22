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

package com.telenav.kivakit.core.messaging;

import com.telenav.kivakit.core.KivaKit;
import com.telenav.kivakit.core.ensure.Ensure;
import com.telenav.kivakit.core.language.Patterns;
import com.telenav.kivakit.core.internal.lexakai.DiagramBroadcaster;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.LoggerFactory;
import com.telenav.kivakit.core.messaging.context.CallStack;
import com.telenav.kivakit.core.project.StartUp;
import com.telenav.kivakit.core.string.AsciiArt;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.core.project.Project.resolveProject;

/**
 * <b>Note</b>: For a detailed discussion, see <a href="https://tinyurl.com/2xycuvph">KivaKit Debugging
 * Documentation</a>
 * <p>
 * The {@link Debug} object is used to switch debugging code on and off efficiently and powerfully by way of the Java
 * system property KIVAKIT_DEBUG (see {@link System#getProperties()}). To change the debug enable state of one or more
 * classes or packages in your application at runtime, simply define KIVAKIT_DEBUG like this: java
 * -DKIVAKIT_DEBUG="[pattern]"
 * <p>
 * The Debug constructor inspects the call stack to see if the class that is constructing the {@link Debug} object (to
 * assign it to a private, static, final field, presumably) matches a list of simplified regular expression patterns in
 * the KIVAKIT_DEBUG system property. If there is a match, then debugging is enabled.
 * <p>
 * If debugging is enabled, the trace*(), warning*() and glitch*() methods will operate. When debugging is disabled,
 * they will not. In the case where a block of code is conditional on debugging or the parameters passed to trace are
 * expensive to construct, the code can be made conditional with {@link #isDebugOn()}.
 * <p>
 * A few KIVAKIT_DEBUG pattern examples:
 * <pre>
 *
 * PATTERN                    EFFECT
 * -------------------------- ----------------------------------------------------
 * "Folder"                   enable debugging in Folder
 * "Debug"                    enable classes to advertise availability of debugging
 * "not Debug"                disable Debug from showing enable states
 * "!Debug"                   disable Debug from showing enable states
 * "File,Folder"              enable debugging in File and Folder
 * "File,Folder,Country"      enable debugging in File, Folder and Country
 * "*"                        enable debugging in all classes
 * "*,not Folder"             enable debugging in all classes, except Folder
 * "Osm*"                     enable debugging in all classes starting with "Osm"
 * "*Checker"                 enable debugging in all classes ending with "Checker"
 * "extends Region"           enable debugging in all subclasses of Region
 * "*,not extends Region"     enable debugging in all except subclasses of Region
 * "*.kivakit.data.*"         enable debugging in all under the kivakit.data package
 * -------------------------- -----------------------------------------------------
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see <a href="https://tinyurl.com/2xycuvph">KivaKit Debugging Documentation</a>
 */
@SuppressWarnings("SpellCheckingInspection")
@UmlClassDiagram(diagram = DiagramBroadcaster.class)
public final class Debug implements DebugTransceiver
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    /** True if debugging in general is enabled */
    private static final Boolean debugging;

    /** True if debugging has been initialized */
    private static boolean initialized;

    /** The debug object for each class */
    private static final Map<Class<?>, Debug> classToDebug = new HashMap<>();

    static
    {
        debugging = debugEnableState(Debug.class);
    }

    public static Debug of(Class<?> type, Transceiver transceiver)
    {
        synchronized (classToDebug)
        {
            if (!classToDebug.containsKey(type))
            {
                // Constructor of Debug adds itself to classToDebug map
                new Debug(type, transceiver);
            }
            return classToDebug.get(type);
        }
    }

    public static void unregister(Class<?> type)
    {
        synchronized (classToDebug)
        {
            classToDebug.remove(type);
        }
    }

    /** True if this particular debug instance is enabled */
    private boolean debugOn;

    /** The listener to send trace messages to */
    private final Transceiver transceiver;

    public Debug(Transceiver transceiver)
    {
        // The class where debug was constructed is the most immediate caller of the class Debug
        this(CallStack.callerOf(CallStack.Proximity.IMMEDIATE, CallStack.Matching.EXACT, Debug.class).typeClass(), transceiver);
    }

    private Debug(Class<?> type, Transceiver transceiver)
    {
        classToDebug.put(type, this);
        debugOn = (isDebugOn(type) == Boolean.TRUE);
        Ensure.ensureNotNull(transceiver);
        this.transceiver = transceiver;
    }

    @Override
    public Debug debug()
    {
        return this;
    }

    public void debugOff()
    {
        debugOn = false;
    }

    public void debugOn()
    {
        debugOn = true;
    }

    @Override
    public boolean isDebugOn()
    {
        return debugOn;
    }

    public Listener listener()
    {
        if (debugOn)
        {
            return (Listener) transceiver;
        }
        return Listener.emptyListener();
    }

    @Override
    public void onReceive(Transmittable message)
    {
        transceiver.receive(message);
    }

    /**
     * @return {@link Boolean#TRUE} if the class is enabled by KIVAKIT_DEBUG, {@link Boolean#FALSE} if it is explicitly
     * disabled and null if the class is simply available for enabling.
     */
    private static Boolean debugEnableState(Class<?> type)
    {
        // Debugging is unknown by default
        Boolean enabled = null;

        // Get KIVAKIT_DEBUG property
        var patternList = property("KIVAKIT_DEBUG");
        if (patternList != null)
        {
            // and go through each pattern in KIVAKIT_DEBUG
            for (var pattern : patternList.trim().replaceAll("\\s+", " ").split(","))
            {
                // looking for "not " at the start of the pattern
                var notPrefix = "not ";
                var not = false;
                if (pattern.startsWith(notPrefix))
                {
                    pattern = pattern.substring(notPrefix.length());
                    not = true;
                }

                // or for "!" at the start of the pattern
                notPrefix = "!";
                if (pattern.startsWith(notPrefix))
                {
                    pattern = pattern.substring(notPrefix.length());
                    not = true;
                }

                // and for "extends " at the start of the pattern
                var extendsPrefix = "extends ";
                var checkParents = pattern.startsWith(extendsPrefix);
                if (checkParents)
                {
                    pattern = pattern.substring(extendsPrefix.length());
                }

                // then match the type against the pattern
                if (matches(type, pattern, checkParents))
                {
                    // Debug is enabled if we're not in not mode
                    enabled = !not;
                }
            }
        }
        return enabled;
    }

    /**
     * @return {@link Boolean#TRUE} if the class is enabled for debugging, {@link Boolean#FALSE} if it is explicitly disabled and null
     * if the class is simply available for enabling.
     */
    private static boolean isDebugOn(Class<?> type)
    {
        // If debugging hasn't been explicitly turned off
        if (debugging != Boolean.FALSE)
        {
            // and we haven't initialized yet,
            if (!initialized)
            {
                // show help message
                initialized = true;
                var debug = property("KIVAKIT_DEBUG");
                var log = property("KIVAKIT_LOG");
                var kivakitVersion = resolveProject(KivaKit.class).kivakitVersion();
                var title = "KivaKit " + kivakitVersion + " (" + resolveProject(KivaKit.class).build() + ")";
                if (!StartUp.isEnabled(StartUp.Option.QUIET))
                {
                    LOGGER.information(AsciiArt.textBox(title, "      Logging: https://tinyurl.com/mhc3ss5s\n"
                                    + "    Debugging: https://tinyurl.com/2xycuvph\n"
                                    + "  KIVAKIT_LOG: $\n"
                                    + "KIVAKIT_DEBUG: $",
                            log == null ? "Console" : log,
                            debug == null ? "Disabled" : debug));
                }
            }

            // Get enable state for the type parameter
            var enabled = debugEnableState(type);

            // and pick a description of the state
            String state;
            if (enabled == null)
            {
                state = "available";
            }
            else if (enabled)
            {
                state = "enabled";
            }
            else
            {
                state = "disabled";
            }

            // then show enable state to the user
            if (debugging == Boolean.TRUE)
            {
                LOGGER.information("Debug output is $ for $ ($)", state, type.getSimpleName(), type.getPackage().getName());
            }
            return enabled == Boolean.TRUE;
        }

        // We are not debugging at all
        return false;
    }

    private static boolean matches(Class<?> type, String simplifiedPattern, boolean checkParent)
    {
        var pattern = Patterns.simplified(simplifiedPattern);
        if (checkParent)
        {
            for (var at = type; at != null; at = at.getSuperclass())
            {
                if (Patterns.matches(pattern, at.getSimpleName())
                        || Patterns.matches(pattern, at.getName()))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return Patterns.matches(pattern, type.getSimpleName())
                    || Patterns.matches(pattern, type.getName());
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static String property(String key)
    {
        var value = System.getProperty(key);
        if (value == null)
        {
            value = System.getenv(key);
        }
        return value;
    }
}
