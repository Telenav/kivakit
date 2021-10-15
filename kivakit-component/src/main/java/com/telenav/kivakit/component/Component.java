package com.telenav.kivakit.component;

import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.configuration.settings.SettingsTrait;
import com.telenav.kivakit.configuration.settings.deployment.Deployment;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.kernel.interfaces.code.Unchecked;
import com.telenav.kivakit.kernel.interfaces.code.UncheckedMethod;
import com.telenav.kivakit.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.language.paths.PackagePathTrait;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.resource.resources.packaged.PackageResource;

/**
 * Interface to KivaKit component functionality, including easy access to settings (see {@link Settings}) and object
 * registries (see {@link Registry}).
 *
 * <p><b>Packaging Methods</b></p>
 *
 * <p>
 * Classes implementing this interface are provided with easy access to packages and package resources relative to the
 * class' package:
 * </p>
 *
 * <ul>
 *     <li>{@link #packageResource(String)} - Returns a {@link Resource} for the given path relative to the class implementing this interface</li>
 *     <li>{@link #relativePackage(String)} - Returns a KivaKit {@link Package} for the given path relative to the class implementing this interface</li>
 * </ul>
 *
 * <p><b>Settings Registry Methods</b></p>
 *
 * <p>
 * Settings for a component can be retrieved with {@link #settingsRegistry()}. This provides a simplified interface to load
 * settings objects specified by the user while also allowing for default settings when they are not specified. See
 * {@link Settings} for details.
 * </p>
 *
 * <ul>
 *     <li>{@link #settingsRegistry()} - Retrieves the {@link Settings} registry for this component</li>
 *     <li>{@link #lookupSettings(Class)} - A settings object of the specified class or null if none exists</li>
 *     <li>{@link #lookupSettings(Class, Enum)} - Get any given instance of the given settings object type</li>
 *     <li>{@link #lookupSettings(Class, String)} - Get any given instance of the given settings object type</li>
 *     <li>{@link #requireSettings(Class)} - Gets the given settings object or fails</li>
 *     <li>{@link #requireSettings(Class, Enum)} - Gets the settings object of the given instance or fails</li>
 *     <li>{@link #requireSettings(Class, String)} - Gets the settings object of the given instance or fails</li>
 *     <li>{@link #registerDeployment(Deployment)} - Adds the settings objects for the given deployment</li>
 *     <li>{@link #registerSettings(Object)}  - Adds the given settings object</li>
 *     <li>{@link #registerAllSettingsIn(Settings)} - Adds the settings objects in the given settings registry</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Folder)} - Adds settings objects from the given folder</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Package)} - Adds settings objects from the given package</li>
 *     <li>{@link #registerAllSettingsIn(Listener, Class, String)} - Adds settings object from the package relative to the given class</li>
 * </ul>
 *
 * <p><b>Object Lookup Registry</b></p>
 *
 * <p>
 * Access to this component's lookup {@link Registry} is provided and fulfills basic needs for object wiring:
 * </p>
 *
 * <ul>
 *     <li>{@link #lookup(Class)} - Lookup the registered object of the given type, if any</li>
 *     <li>{@link #lookup(Class, String)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #lookup(Class, Enum)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #register(Object)} - Register the given singleton object for lookup</li>
 *     <li>{@link #register(Object, String)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #register(Object, Enum)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #registry()} - Retrieves the lookup {@link Registry} for this component</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 * @see Registry
 * @see Settings
 * @see Repeater
 * @see SettingsTrait
 * @see RegistryTrait
 */
public interface Component extends Repeater, NamedObject, SettingsTrait, RegistryTrait, PackagePathTrait
{
    class UncheckedVoid implements Unchecked<Void>
    {
        @Override
        public Void run() throws Exception
        {
            return null;
        }
    }

    /**
     * @return The resource at the given path relative to this component's class
     */
    default Resource packageResource(final String path)
    {
        return PackageResource.of(getClass(), path);
    }

    default Package relativePackage(final String path)
    {
        return Package.of(getClass(), path);
    }

    default <T> T tryCatch(Unchecked<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(message, arguments);
            return null;
        }
    }

    default <T> T tryCatchDefault(Unchecked<T> code, T defaultValue)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    default <T> T tryCatchThrow(Unchecked<T> code, String message, Object... arguments)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(message, arguments).throwAsIllegalStateException();
            return null;
        }
    }

    default void tryFinally(UncheckedMethod code, Runnable after)
    {
        try
        {
            code.run();
        }
        catch (Exception e)
        {
            problem(e, "Code threw exception");
        }
        finally
        {
            after.run();
        }
    }

    default <T> T tryFinallyFunction(Unchecked<T> code, Runnable after)
    {
        try
        {
            return code.run();
        }
        catch (Exception e)
        {
            problem(e, "Code threw exception");
            return null;
        }
        finally
        {
            after.run();
        }
    }
}
