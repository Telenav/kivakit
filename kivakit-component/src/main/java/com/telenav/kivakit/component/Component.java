package com.telenav.kivakit.component;

import com.telenav.kivakit.component.project.lexakai.DiagramComponent;
import com.telenav.kivakit.core.code.UncheckedCode;
import com.telenav.kivakit.core.language.trait.LanguageTrait;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.Announcement;
import com.telenav.kivakit.core.messaging.messages.status.Glitch;
import com.telenav.kivakit.core.messaging.messages.status.Information;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Trace;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.project.ProjectTrait;
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.core.vm.JavaTrait;
import com.telenav.kivakit.interfaces.code.Code;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.resource.PackageTrait;
import com.telenav.kivakit.resource.Resource;
import com.telenav.kivakit.resource.resources.packaged.Package;
import com.telenav.kivakit.settings.settings.Settings;
import com.telenav.kivakit.settings.settings.SettingsStore;
import com.telenav.kivakit.settings.settings.SettingsTrait;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.util.function.Function;

/**
 * A composite interface providing convenient functionality to KivaKit components.
 *
 * <p><b>Sub-Interfaces</b></p>
 *
 * <ul>
 *     <li>{@link PackageTrait} - Access to packages and packaged resources</li>
 *     <li>{@link SettingsTrait} - Component settings</li>
 *     <li>{@link RegistryTrait} - Service {@link Registry} access</li>
 *     <li>{@link LanguageTrait} - Enhancements that reduce language verbosity</li>
 *     <li>{@link Repeater} - Message broadcasting, listening and repeating</li>
 *     <li>{@link NamedObject} - Object naming</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
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
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Registry</b></p>
 *
 * <p>
 * Access to this component's lookup {@link Registry} is provided and fulfills basic needs for object wiring:
 * </p>
 *
 * <ul>
 *     <li>{@link #lookup(Class)} - Lookup the registered object of the given type, if any</li>
 *     <li>{@link #lookup(Class, Enum)} - Find the given instance of the given class, if any</li>
 *     <li>{@link #require(Class)} - Find an instance of the given class, throwing an exception if there is none</li>
 *     <li>{@link #require(Class, Enum)}  - Find the given instance of the given class, throwing an exception if there is none</li>
 *     <li>{@link #register(Object)} - Register the given singleton object for lookup</li>
 *     <li>{@link #register(Object, Enum)} - Register the given instance of the given object for lookup</li>
 *     <li>{@link #registry()} - Retrieves the lookup {@link Registry} for this component</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Language</b></p>
 *
 * <p>Adds methods that help in general to extend KivaKit's expressive capabilities in the Java language</p>
 *
 * <p><i>Functions</i></p>
 * <ul>
 *     <li>{@link #ifNonNullApply(Object, Function)} - Null-safe version of Function.apply()</li>
 *     <li>{@link #ifNullDefault(Object, Object)} - The given value if it is non-null, the default value otherwise.</li>
 * </ul>
 *
 * <p><i>Validation</i></p>
 *
 * <ul>
 *     <li>{@link #isFalseOr(boolean, String, Object...)} - Broadcasts a {@link Problem} if the given value is not false</li>
 *     <li>{@link #isTrueOr(boolean, String, Object...)} - Broadcasts a {@link Problem} if the given value is not true</li>
 *     <li>{@link #isNonNullOr(Object, String, Object...)} - Broadcasts a {@link Problem} if the given value is null</li>
 * </ul>
 *
 * <p><i>Exceptions</i></p>
 *
 * <ul>
 *     <li>{@link #tryCatch(UncheckedCode, String, Object...)} - Executes the given code, broadcasting a problem if it throws an exception</li>
 *     <li>{@link #tryCatchThrow(UncheckedCode, String, Object...)} - Executes the given code, rethrowing any exceptions</li>
 *     <li>{@link #tryCatchDefault(UncheckedCode, Object)} - Executes the given code, returning the given default if an exception occurs</li>
 *     <li>{@link #tryFinally(UncheckedCode, Runnable)} - Executes the given code, always running the {@link Runnable} after execution</li>
 * </ul>
 *
 * <p><i>Result</i></p>
 *
 * <ul>
 *     <li>{@link #result(Code)} - Executes the given code, capturing any messages broadcast during execution in a {@link Result} object</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Settings</b></p>
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
 * <p><br/><hr/><br/></p>
 *
 * <p><b>Repeater</b></p>
 *
 * <ul>
 *     <li>{@link #listenTo(Broadcaster)} - Adds this component as a listener to the given {@link Broadcaster}</li>
 *     <li>{@link #addListener(Listener)} - Adds the given listener to this component</li>
 *     <li>{@link #announce(String, Object...)} - Broadcasts an {@link Announcement} message</li>
 *     <li>{@link #problem(String, Object...)} - Broadcasts a {@link Problem} message</li>
 *     <li>{@link #warning(String, Object...)} - Broadcasts a {@link Warning} message</li>
 *     <li>{@link #quibble(String, Object...)} - Broadcasts a {@link Quibble} message</li>
 *     <li>{@link #glitch(String, Object...)} - Broadcasts a {@link Glitch} message</li>
 *     <li>{@link #information(String, Object...)} - Broadcasts an {@link Information} message</li>
 *     <li>{@link #trace(String, Object...)} - Broadcasts a {@link Trace} message if {@link #isDebugOn()} is true for this component</li>
 * </ul>
 *
 * <p><br/><hr/><br/></p>
 *
 * @author jonathanl (shibo)
 * @see Repeater
 * @see SettingsTrait
 * @see RegistryTrait
 * @see LanguageTrait
 * @see PackageTrait
 * @see Registry
 * @see Settings
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
public interface Component extends
        JavaTrait,
        ProjectTrait,
        PackageTrait,
        RegistryTrait,
        LanguageTrait,
        SettingsTrait,
        Repeater,
        NamedObject
{
}
