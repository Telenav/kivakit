////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging;

import com.telenav.kivakit.core.kernel.KivaKit;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.kernel.language.patterns.Pattern;
import com.telenav.kivakit.core.kernel.language.patterns.SimplifiedPattern;
import com.telenav.kivakit.core.kernel.language.strings.AsciiArt;
import com.telenav.kivakit.core.kernel.language.threading.context.CallStack;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Note</b>: The <a href="https://spaces.telenav.com:8443/x/PSZECw">KivaKit wiki</a> has additional information
 * about KivaKit's debugging facilities.
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
 * If debugging is enabled, the trace*(), warning*() and quibble*() methods will operate. When debugging is disabled,
 * they will not. In the case where a block of code is conditional on debugging or the parameters passed to trace,
 * warning or quibble are expensive to construct, the code can be made conditional with {@link #isOn()}.
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
 * @see SimplifiedPattern
 * @see <a href="https://spaces.telenav.com:8443/x/PSZECw">KivaKit wiki</a>
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
public final class Debug implements Transceiver
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

    public static Debug of(final Class<?> type, final Transceiver transceiver)
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

    public static void unregister(final Class<?> type)
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

    public Debug(final Transceiver transceiver)
    {
        // The class where debug was constructed is the most immediate caller of the class Debug
        this(CallStack.callerOf(CallStack.Proximity.IMMEDIATE, CallStack.Matching.EXACT, Debug.class).type(), transceiver);
    }

    private Debug(final Class<?> type, final Transceiver transceiver)
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
        return Listener.none();
    }

    @Override
    public void onHandle(final Transmittable message)
    {
        transceiver.handle(message);
    }

    public void println(final Object object)
    {
        if (debugOn)
        {
            System.out.println(object.toString());
        }
    }

    /**
     * @return Boolean.TRUE if the class is enabled by KIVAKIT_DEBUG, Boolean.FALSE if it is explicitly disabled and
     * null if the class is simply available for enabling.
     */
    private static Boolean debugEnableState(final Class<?> type)
    {
        // Debugging is unknown by default
        Boolean enabled = null;

        // Get KIVAKIT_DEBUG property
        final var patternList = property("KIVAKIT_DEBUG");
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
                final var extendsPrefix = "extends ";
                final var checkParents = pattern.startsWith(extendsPrefix);
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
     * @return Boolean.TRUE if the class is enabled for debugging, Boolean.FALSE if it is explicitly disabled and null
     * if the class is simply available for enabling.
     */
    private static boolean isDebugOn(final Class<?> type)
    {
        // If debugging hasn't been explicitly turned off
        if (debugging != Boolean.FALSE)
        {
            // and we haven't initialized yet,
            if (!initialized)
            {
                // show help message
                initialized = true;
                final var debug = property("KIVAKIT_DEBUG");
                final var log = property("KIVAKIT_LOG");
                final var title = "KivaKit " + KivaKit.get().version() + " (" + KivaKit.get().build() + ")";
                LOGGER.information(AsciiArt.textBox(title, "  Logging: https://spaces.telenav.com:8443/x/OyZECw\n"
                                + "Debugging: https://spaces.telenav.com:8443/x/PSZECw\n"
                                + "  KIVAKIT_LOG: $\n"
                                + "KIVAKIT_DEBUG: $",
                        log == null ? "Console" : log,
                        debug == null ? "Disabled" : debug));
            }

            // Get the enable state for the type parameter
            final var enabled = debugEnableState(type);

            // and pick a description of the state
            final String state;
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

            // then show the enable state to the user
            if (debugging == Boolean.TRUE)
            {
                LOGGER.information("Debug output is $ for $ ($)", state, type.getSimpleName(), type.getPackage().getName());
            }
            return enabled == Boolean.TRUE;
        }

        // We are not debugging at all
        return false;
    }

    private static boolean matches(final Class<?> type, final String simplifiedPattern, final boolean checkParent)
    {
        final Pattern pattern = new SimplifiedPattern(simplifiedPattern);
        if (checkParent)
        {
            for (var at = type; at != null; at = at.getSuperclass())
            {
                if (pattern.matches(at.getSimpleName()) || pattern.matches(at.getName()))
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return pattern.matches(type.getSimpleName()) || pattern.matches(type.getName());
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static String property(final String key)
    {
        var value = System.getProperty(key);
        if (value == null)
        {
            value = System.getenv(key);
        }
        return value;
    }
}
