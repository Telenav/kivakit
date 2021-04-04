package com.telenav.kivakit.core.configuration.settings;

import com.telenav.kivakit.core.configuration.ConfigurationFolder;
import com.telenav.kivakit.core.configuration.ConfigurationPackage;
import com.telenav.kivakit.core.configuration.ConfigurationSet;
import com.telenav.kivakit.core.configuration.Deployment;
import com.telenav.kivakit.core.configuration.lookup.Lookup;
import com.telenav.kivakit.core.filesystem.Folder;
import com.telenav.kivakit.core.kernel.data.validation.ensure.Ensure;
import com.telenav.kivakit.core.kernel.language.paths.PackagePath;
import com.telenav.kivakit.core.kernel.language.vm.OperatingSystem;
import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Debug;
import com.telenav.kivakit.core.resource.resources.packaged.Package;

/**
 * This class simplifies access to configuration objects through the configuration API. For a detailed example showing
 * how such objects are specified, see {@link ConfigurationSet}.
 *
 * <p><b>Finding Settings</b></p>
 * <p>
 * The {@link #require(Class)} method and related overloads find settings objects, as described in {@link
 * ConfigurationSet}. To do this, the method first looks for the given settings object using the global {@link Lookup}.
 * This allows settings to be overridden by the configuration API using {@link Deployment}s or using a command line
 * variable, as described below.s If the required settings object is not already registered, all settings objects are
 * loaded from the specified package to provide a default object. Then, the lookup is retried and the result returned.
 * </p>
 *
 * <p><b>Overriding Settings from the Command Line</b></p>
 *
 * <p>
 * Settings can be overridden from the command line by specifying a comma-separated folder list using
 * KIVAKIT_SETTINGS_FOLDERS environment variable. All settings from each folder in the list will be loaded and will
 * override the default settings that might otherwise be used.
 * </p>
 *
 * <pre>
 * java -DKIVAKIT_SETTINGS_FOLDERS=my-settings-folder [...]
 * </pre>
 *
 * @author jonathanl (shibo)
 */
public class Settings
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    private static final Debug DEBUG = new Debug(LOGGER);

    /**
     * @return The settings object of the given type
     */
    public static <T> T require(final Class<T> settingsType)
    {
        return require(settingsType, PackagePath.packagePath(settingsType));
    }

    /**
     * @return The settings object of the requested type from the global {@link Lookup} or from the package of default
     * settings if it is not found there.
     */
    public static <T> T require(final Class<T> settingsType, final Package defaultSettings)
    {
        return require(settingsType, defaultSettings.path());
    }

    /**
     * @return The settings object of the requested type from the global {@link Lookup} or from the package of default
     * settings if it is not found there.
     */
    public static <T> T require(final Class<T> settingsClass, final PackagePath defaultSettingsPackage)
    {
        // Load any settings overrides from KIVAKIT_SETTINGS_FOLDERS
        loadOverrides();

        // then look in the global lookup for the settings
        var settings = Lookup.global().locate(settingsClass);

        // and if settings have not been defined
        if (settings == null)
        {
            // then load the default settings
            DEBUG.trace("Installing default settings from $", defaultSettingsPackage);
            final var defaultSettings = LOGGER.listenTo(ConfigurationPackage.of(defaultSettingsPackage));
            defaultSettings.install();

            // and try again,
            settings = Lookup.global().locate(settingsClass);

            // and finally, fail if the settings still cannot be found
            Ensure.ensureNotNull(settings, "Unable to locate settings: ${class}", settingsClass);
        }

        return settings;
    }

    /**
     * Loads settings from the list of folders specified by the KIVAKIT_SETTINGS_FOLDERS environment variable
     */
    private static void loadOverrides()
    {
        // Go through each path specified by the KIVAKIT_SETTINGS_FOLDERS environment variable
        final var settingsFolders = OperatingSystem.get().environmentVariables().getOrDefault("KIVAKIT_SETTINGS_FOLDERS", null);
        if (settingsFolders != null)
        {
            for (final var path : settingsFolders.split(",\\s*"))
            {
                // and install
                final var folder = Folder.parse(path);
                if (folder != null)
                {
                    LOGGER.listenTo(new ConfigurationFolder(folder)).install();
                }
                else
                {
                    throw new IllegalStateException("Invalid folder in KIVAKIT_SETTINGS_FOLDERS: " + path);
                }
            }
        }
    }
}
