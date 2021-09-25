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

package com.telenav.kivakit.kernel.messaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.interfaces.naming.NamedObject;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.kernel.language.time.Frequency;
import com.telenav.kivakit.kernel.messaging.messages.lifecycle.OperationHalted;
import com.telenav.kivakit.kernel.messaging.messages.status.Announcement;
import com.telenav.kivakit.kernel.messaging.messages.status.FatalProblem;
import com.telenav.kivakit.kernel.messaging.messages.status.Glitch;
import com.telenav.kivakit.kernel.messaging.messages.status.Information;
import com.telenav.kivakit.kernel.messaging.messages.status.Narration;
import com.telenav.kivakit.kernel.messaging.messages.status.Problem;
import com.telenav.kivakit.kernel.messaging.messages.status.Quibble;
import com.telenav.kivakit.kernel.messaging.messages.status.Trace;
import com.telenav.kivakit.kernel.messaging.messages.status.Warning;
import com.telenav.kivakit.kernel.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLogging;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListener;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.UmlNote;
import com.telenav.lexakai.annotations.associations.UmlRelation;
import com.telenav.lexakai.annotations.visibility.UmlExcludeMember;
import com.telenav.lexakai.annotations.visibility.UmlExcludeSuperTypes;

/**
 * Functionality that is common to {@link Broadcaster}s, {@link Listener}s, {@link Repeater}s and potentially other
 * classes that are involved in handling messages. The {@link #isOn()} method returns true if the transceiver is
 * enabled. When enabled, the {@link #handle(Transmittable)} method calls {@link #onHandle(Transmittable)} to allow the
 * subclass to handle the message.
 *
 * <p><b>Convenience Methods</b></p>
 * <p>
 * The following convenience methods call {@link #handle(Transmittable)} with the appropriate class of message.
 * </p>
 * <ul>
 *     <li>trace*() - The listener handles a {@link Trace} message</li>
 *     <li>information*() - The listener handles an {@link Information} message</li>
 *     <li>narrate() - The listener handles a {@link Narration} message</li>
 *     <li>warning*() - The listener handles a {@link Warning} message</li>
 *     <li>glitch*() - The listener handles a {@link Glitch} message</li>
 *     <li>problem*() - The listener handles a {@link Problem} message</li>
 *     <li>halt*() - The listener handles an {@link OperationHalted} message</li>
 * </ul>
 *
 * <p><b>Debugging</b></p>
 * <p>
 * A {@link Transceiver} provides access to a {@link Debug} object which has the class context (also {@link
 * CodeContext}) provided by the method {@link #debugClassContext()}.
 * <p>
 * The {@link #isDebugOn()} provides convenient access to {@link Debug#isDebugOn()} and the {@link #ifDebug(Runnable)}
 * executes the given code if debugging is on. Several convenience methods also provide tracing which originates {@link
 * Trace} messages only if debugging is on.
 * </p>
 * <p>
 * Because {@link Broadcaster}s, {@link Listener}s and {@link Repeater}s are debug transceivers, they inherit all the
 * methods in this class. This means that a subclass of {@link BaseRepeater} can simply call a trace() or glitch()
 * method and it will automatically be gated by the functionality of {@link Debug}. This makes it especially easy to
 * declare and control debug tracing. In the example below, the trace() statement can be enabled by running the
 * application with -DKIVAKIT_DEBUG=EmployeeLoader. See @{@link Debug} for more details on the KIVAKIT_DEBUG syntax.
 * </p>
 * <p>
 * <b>Debug Tracing Example</b>
 * </p>
 * <pre>
 * class EmployeeLoader extends BaseRepeater
 * {
 *
 *     [...]
 *
 *     trace("Loaded $ employees", employees.size());
 *
 * }
 * </pre> * @author jonathanl (shibo)
 *
 * @see Listener
 * @see Broadcaster
 * @see Message
 * @see Trace
 * @see Information
 * @see Warning
 * @see Glitch
 * @see Problem
 * @see OperationHalted
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramLogging.class)
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
@UmlClassDiagram(diagram = DiagramMessageListener.class)
@UmlRelation(label = "delegates to", referent = Debug.class)
@UmlExcludeSuperTypes({ NamedObject.class })
@UmlNote(text = "Functionality common to transmitters and receivers")
public interface Transceiver extends NamedObject
{
    default Announcement announce(final String text, final Object... arguments)
    {
        return handle(new Announcement(text, arguments));
    }

    /**
     * @return Debug object for this
     */
    default Debug debug()
    {
        return Debug.of(debugClassContext(), this);
    }

    /**
     * @return The class where this transceiver is
     */
    @UmlExcludeMember
    default Class<?> debugClassContext()
    {
        return getClass();
    }

    /**
     * @return The context of this broadcaster in code
     */
    @UmlExcludeMember
    default CodeContext debugCodeContext()
    {
        return new CodeContext(debugClassContext());
    }

    /**
     * @param context The context in code
     */
    @UmlExcludeMember
    default void debugCodeContext(final CodeContext context)
    {
    }

    default <T> T fatal(final String text, final Object... arguments)
    {
        final var problem = new FatalProblem(text, arguments);
        handle(problem);
        problem.throwAsIllegalStateException();
        return null;
    }

    default <T> T fatal(final Throwable cause, final String text, final Object... arguments)
    {
        final var problem = new FatalProblem(cause, text, arguments);
        handle(problem);
        problem.throwAsIllegalStateException();
        return null;
    }

    default Glitch glitch(final Frequency maximumFrequency, final String text, final Object... arguments)
    {
        return (Glitch) handle(new Glitch(text, arguments).maximumFrequency(maximumFrequency));
    }

    default Glitch glitch(final Frequency maximumFrequency, final Throwable cause, final String text,
                          final Object... arguments)
    {
        return (Glitch) handle(new Glitch(cause, text, arguments).maximumFrequency(maximumFrequency));
    }

    default Glitch glitch(final String text, final Object... arguments)
    {
        return handle(new Glitch(text, arguments));
    }

    default Glitch glitch(final Throwable cause, final String text, final Object... arguments)
    {
        if (isDebugOn())
        {
            return handle(new Glitch(cause, text, arguments));
        }
        return null;
    }

    default OperationHalted halt(final String text, final Object... arguments)
    {
        return handle(new OperationHalted(text, arguments));
    }

    default OperationHalted halt(final Throwable cause, final String text, final Object... arguments)
    {
        return handle(new OperationHalted(cause, text, arguments));
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Allows the subclass to handle the messages originating from the convenience methods in this class via the {@link
     * #onHandle(Transmittable)} functional method.
     * </p>
     */
    @UmlExcludeMember
    default <T extends Transmittable> T handle(final T message)
    {
        if (isOn())
        {
            onHandle(message);
        }
        return message;
    }

    default void ifDebug(final Runnable code)
    {
        if (isDebugOn())
        {
            code.run();
        }
    }

    default <T> T illegalArgument(final String text, final Object... arguments)
    {
        final var problem = new Problem(text, arguments);
        handle(problem);
        problem.throwAsIllegalArgumentException();
        return null;
    }

    default <T> T illegalState(final String text, final Object... arguments)
    {
        final var problem = new Problem(text, arguments);
        handle(problem);
        problem.throwAsIllegalStateException();
        return null;
    }

    default <T> T illegalState(final Throwable e, final String text, final Object... arguments)
    {
        final var problem = new Problem(e, text, arguments);
        handle(problem);
        problem.throwAsIllegalStateException();
        return null;
    }

    default Information information(final String text, final Object... arguments)
    {
        return handle(new Information(text, arguments));
    }

    @JsonIgnore
    default boolean isDebugOn()
    {
        return debug().isDebugOn();
    }

    /**
     * @return True if this transceiver is enabled
     */
    @JsonIgnore
    default boolean isOn()
    {
        return true;
    }

    default Narration narrate(final String text, final Object... arguments)
    {
        return handle(new Narration(text, arguments));
    }

    /**
     * <b>Not public API</b>
     * <p>
     * Functional method that allows the subclass to handle a message given to this receiver via {@link
     * #handle(Transmittable)}.
     * </p>
     */
    @UmlExcludeMember
    void onHandle(final Transmittable message);

    default Problem problem(final String text, final Object... arguments)
    {
        return handle(new Problem(text, arguments));
    }

    default Problem problem(final Frequency maximumFrequency, final String text, final Object... arguments)
    {
        return (Problem) handle(new Problem(text, arguments).maximumFrequency(maximumFrequency));
    }

    default Problem problem(final Frequency maximumFrequency, final Throwable cause, final String text,
                            final Object... arguments)
    {
        return (Problem) handle(new Problem(cause, text, arguments).maximumFrequency(maximumFrequency));
    }

    default Problem problem(final Throwable cause, final String text, final Object... arguments)
    {
        return handle(new Problem(cause, text, arguments));
    }

    default Quibble quibble(final Frequency maximumFrequency, final String text, final Object... arguments)
    {
        return (Quibble) handle(new Quibble(text, arguments).maximumFrequency(maximumFrequency));
    }

    default Quibble quibble(final Frequency maximumFrequency, final Throwable cause, final String text,
                            final Object... arguments)
    {
        return (Quibble) handle(new Quibble(cause, text, arguments).maximumFrequency(maximumFrequency));
    }

    default Quibble quibble(final String text, final Object... arguments)
    {
        return handle(new Quibble(text, arguments));
    }

    default Quibble quibble(final Throwable cause, final String text, final Object... arguments)
    {
        if (isDebugOn())
        {
            return handle(new Quibble(cause, text, arguments));
        }
        return null;
    }

    default Trace trace(final String text, final Object... arguments)
    {
        if (isDebugOn())
        {
            return handle(new Trace(text, arguments));
        }
        return null;
    }

    default Trace trace(final Throwable cause, final String text, final Object... arguments)
    {
        if (isDebugOn())
        {
            return handle(new Trace(cause, text, arguments));
        }
        return null;
    }

    default Trace trace(final Frequency maximumFrequency, final String text, final Object... arguments)
    {
        if (isDebugOn())
        {
            return (Trace) handle(new Trace(text, arguments).maximumFrequency(maximumFrequency));
        }
        return null;
    }

    default Trace trace(final Frequency maximumFrequency, final Throwable cause, final String text,
                        final Object... arguments)
    {
        if (isDebugOn())
        {
            return (Trace) handle(new Trace(cause, text, arguments).maximumFrequency(maximumFrequency));
        }
        return null;
    }

    default Warning warning(final Frequency maximumFrequency, final String text, final Object... arguments)
    {
        return (Warning) handle(new Warning(text, arguments).maximumFrequency(maximumFrequency));
    }

    default Warning warning(final Frequency maximumFrequency, final Throwable cause, final String text,
                            final Object... arguments)
    {
        return (Warning) handle(new Warning(cause, text, arguments).maximumFrequency(maximumFrequency));
    }

    default Warning warning(final String text, final Object... arguments)
    {
        return handle(new Warning(text, arguments));
    }

    default Warning warning(final Throwable cause, final String text, final Object... arguments)
    {
        return handle(new Warning(cause, text, arguments));
    }
}
