package com.telenav.kivakit.core.vm;

import com.telenav.cactus.metadata.BuildMetadata;
import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.language.primitive.Booleans;
import com.telenav.kivakit.core.os.OperatingSystem;
import com.telenav.kivakit.core.project.Build;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.time.LocalTime;

import java.util.HashMap;
import java.util.Map;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_STATIC_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Provides access to system properties, environment variables and project properties
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE_STATIC_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class Properties
{
    /** A map from root package name to project properties */
    private static final Map<String, VariableMap<String>> projectProperties = new HashMap<>();

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
    public static VariableMap<String> allProperties(Class<?> projectRoot)
    {
        var packageName = projectRoot.getPackageName();
        var properties = projectProperties.get(packageName);
        if (properties == null)
        {
            var build = Build.build(projectRoot);

            properties = JavaVirtualMachine.javaVirtualMachine().systemPropertiesAndEnvironmentVariables();
            properties.addAll(VariableMap.variableMap(BuildMetadata.of(projectRoot).projectProperties()));
            properties.put("version", properties.get("project-version"));
            properties.putIfNotNull("build-name", build.name());
            properties.putIfNotNull("build-date", build.buildFormattedDate());
            properties.putIfNotNull("build-number", Integer.toString(build.buildNumber()));
            properties.put("date-and-time", LocalTime.now().asDateTimeString());

            properties = properties.expanded();

            projectProperties.put(packageName, properties);
        }

        return properties;
    }

    public static boolean isSystemPropertyOrEnvironmentVariableFalse(String key)
    {
        var value = systemPropertyOrEnvironmentVariable(key);
        return value == null || Booleans.isFalse(value);
    }

    public static boolean isSystemPropertyOrEnvironmentVariableTrue(String key)
    {
        return Booleans.isTrue(systemPropertyOrEnvironmentVariable(key));
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
