package com.telenav.kivakit.core.vm;

import com.telenav.cactus.metadata.BuildMetadata;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.project.Project;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.cactus.metadata.BuildMetadata.buildMetaData;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.UNTESTED;
import static com.telenav.kivakit.core.collections.map.VariableMap.variableMap;
import static com.telenav.kivakit.core.language.primitive.Booleans.isFalse;
import static com.telenav.kivakit.core.language.primitive.Booleans.isTrue;
import static com.telenav.kivakit.core.project.Build.build;
import static com.telenav.kivakit.core.time.LocalTime.now;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;

/**
 * Provides access to system properties, environment variables and project properties
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE_EXTENSIBLE,
             testing = UNTESTED,
             documentation = DOCUMENTED)
public class Properties
{
    /** A map from root package name to project properties */
    private static final Map<Class<?>, VariableMap<String>> projectProperties = new HashMap<>();

    /**
     * Returns a set of properties, including:
     * <ul>
     *     <li>{@link JavaVirtualMachine} properties</li>
     *     <li>{@link OperatingSystem} environment variables</li>
     *     <li>{@link BuildMetadata} in <i>build.properties</i></li>
     *     <li>Project properties in <i>project.properties</i></li>
     * </ul>
     *
     * @return All relevant properties for the given project root class (normally a {@link Project} or Application
     * class).
     */
    public static synchronized VariableMap<String> allProperties(Class<?> type)
    {
        var properties = projectProperties.get(type);
        if (properties == null)
        {
            var build = build(type);

            properties = javaVirtualMachine().systemPropertiesAndEnvironmentVariables();
            properties.addAll(variableMap(buildMetaData(type).projectProperties()));
            properties.put("version", properties.get("project-version"));
            properties.putIfNotNull("build-name", build.name());
            properties.putIfNotNull("build-date", build.buildFormattedDate());
            properties.putIfNotNull("build-number", Integer.toString(build.buildNumber()));
            properties.put("date-and-time", now().asDateTimeString());

            properties = properties.expanded();

            projectProperties.put(type, properties);
        }

        return properties;
    }

    public static boolean isSystemPropertyOrEnvironmentVariableFalse(String key)
    {
        var value = systemPropertyOrEnvironmentVariable(key);
        return value == null || isFalse(value);
    }

    public static boolean isSystemPropertyOrEnvironmentVariableTrue(String key)
    {
        return isTrue(systemPropertyOrEnvironmentVariable(key));
    }

    public static String systemPropertyOrEnvironmentVariable(String key)
    {
        var value = System.getProperty(key);
        if (value == null)
        {
            value = System.getenv(key);
        }
        return value;
    }

    public static String systemPropertyOrEnvironmentVariable(String key, String defaultValue)
    {
        var value = systemPropertyOrEnvironmentVariable(key);
        return value == null ? defaultValue : value;
    }
}
