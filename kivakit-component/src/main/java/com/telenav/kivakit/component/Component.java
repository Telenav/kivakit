package com.telenav.kivakit.component;

import com.telenav.kivakit.component.internal.lexakai.DiagramComponent;
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
import com.telenav.kivakit.core.registry.Registry;
import com.telenav.kivakit.core.registry.RegistryTrait;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A composite interface providing convenient functionality to KivaKit components.
 *
 * <p><b>Sub-Interfaces</b></p>
 *
 * <ul>
 *     <li>{@link RegistryTrait} - Object {@link Registry} access</li>
 *     <li>{@link Repeater} - Message broadcasting, listening and repeating</li>
 *     <li>{@link NamedObject} - Object naming</li>
 * </ul>
 *
 * <p><b>Naming</b></p>
 *
 * <ul>
 *     <li>{@link #objectName()} - The name of this object</li>
 * </ul>
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
 * <hr>
 *
 * @author jonathanl (shibo)
 * @see Repeater
 * @see RegistryTrait
 * @see Registry
 */
@UmlClassDiagram(diagram = DiagramComponent.class)
public interface Component extends
        Repeater,
        RegistryTrait,
        NamedObject
{
}
