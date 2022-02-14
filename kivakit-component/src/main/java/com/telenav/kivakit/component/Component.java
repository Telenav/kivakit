package com.telenav.kivakit.component;

import com.telenav.kivakit.component.project.lexakai.diagrams.DiagramComponent;
import com.telenav.kivakit.configuration.lookup.Registry;
import com.telenav.kivakit.configuration.lookup.RegistryTrait;
import com.telenav.kivakit.configuration.settings.Settings;
import com.telenav.kivakit.configuration.settings.SettingsStore;
import com.telenav.kivakit.configuration.settings.SettingsTrait;
import com.telenav.kivakit.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.language.traits.LanguageTrait;
import com.telenav.kivakit.kernel.language.traits.OperationTrait;
import com.telenav.kivakit.kernel.language.traits.TryTrait;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.resource.PackageTrait;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A composite interface providing functionality to components.
 *
 * <p><b>Sub-Interfaces</b></p>
 *
 * <ul>
 *     <li>{@link Repeater} - Message broadcasting, listening and repeating</li>
 *     <li>{@link RegistryTrait} - Service {@link Registry} access</li>
 *     <li>{@link SettingsTrait} - Component settings</li>
 *     <li>{@link LanguageTrait} - Enhancements that reduce language verbosity</li>
 *     <li>{@link PackageTrait} - Access to packages and packaged resources</li>
 *     <li>{@link NamedObject} - Object naming</li>
 * </ul>
 *
 * <p><b>Packaging</b></p>
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
 *     <li>{@link #requireSettings(Class)} - Gets the given settings object or fails</li>
 *     <li>{@link #requireSettings(Class, Enum)} - Gets the settings object of the given instance or fails</li>
 *     <li>{@link #registerSettingsObject(Object)}  - Adds the given settings object</li>
 *     <li>{@link #registerSettingsObject(Object, Enum)} - Adds the given settings object</li>
 *     <li>{@link #registerSettingsIn(SettingsStore)} - Adds settings objects from the given settings store</li>
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
 * @see TryTrait
 * @see PackageTrait
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
public interface Component extends
        Repeater,
        RegistryTrait,
        SettingsTrait,
        LanguageTrait,
        OperationTrait,
        PackageTrait,
        NamedObject
{
}
