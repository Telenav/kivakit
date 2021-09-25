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

package com.telenav.kivakit.kernel.messaging.broadcasters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telenav.kivakit.kernel.interfaces.comparison.Filter;
import com.telenav.kivakit.kernel.interfaces.messaging.Transmittable;
import com.telenav.kivakit.kernel.language.collections.list.StringList;
import com.telenav.kivakit.kernel.language.paths.PackagePathed;
import com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder;
import com.telenav.kivakit.kernel.language.threading.context.CodeContext;
import com.telenav.kivakit.kernel.language.threading.locks.ReadWriteLock;
import com.telenav.kivakit.kernel.language.values.name.Name;
import com.telenav.kivakit.kernel.language.vm.JavaVirtualMachine;
import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.loggers.ConsoleLogger;
import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.messages.OperationMessage;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensure;
import static com.telenav.kivakit.kernel.data.validation.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder.Indentation;
import static com.telenav.kivakit.kernel.language.strings.formatting.IndentingStringBuilder.Style.TEXT;

/**
 * A multicaster is a broadcaster which can have more than one listener. As with any broadcaster, listeners can be added
 * to a multicaster with {@link #addListener(Listener)} and can be cleared with {@link #clearListeners()}. Messages can
 * be broadcast to all listeners with {@link #transmit(Transmittable)}.
 * <p>
 * If a class is already extending some other base class (and since Java does not support mix-ins) an instance of {@link
 * Multicaster} can be aggregated and its methods delegated to implement the {@link Broadcaster} interface This is not
 * automatic in Java, but requires a minimal amount of code:
 * <pre>
 * public class A extends B implements Repeater&lt;Message&gt;
 * {
 *     private final BaseRepeater&lt;Message&gt; repeater = new BaseRepeater&lt;&gt;();
 *
 *     public void broadcast(final Message message)
 *     {
 *         this.repeater.broadcast(message);
 *     }
 *
 *     public void addListener(final Listener&lt;Message&gt; listener)
 *     {
 *         this.repeater.addListener(listener);
 *     }
 *
 *     public void clearListeners()
 *     {
 *         this.repeater.clearListeners();
 *     }
 *
 *     public boolean hasListeners()
 *     {
 *         return this.repeater.hasListeners();
 *     }
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Broadcaster
 * @see Listener
 */
@UmlClassDiagram(diagram = DiagramMessageRepeater.class)
public class Multicaster implements Broadcaster, PackagePathed
{
    private static final Logger LOGGER = new ConsoleLogger();

    /** This multicaster audience */
    @UmlAggregation
    private final transient List<AudienceMember> audience = new ArrayList<>();

    private final String objectName;

    private CodeContext debugCodeContext;

    private final Class<?> debugClassContext;

    private transient ReadWriteLock lock;

    private Broadcaster source;

    public Multicaster(final String objectName, final Class<?> debugClassContext)
    {
        this.objectName = objectName;
        this.debugClassContext = debugClassContext;
        debugCodeContext(new CodeContext(debugClassContext));
    }

    public Multicaster(final Class<?> debugClassContext)
    {
        objectName = Name.synthetic(this);
        this.debugClassContext = debugClassContext;
        debugCodeContext(new CodeContext(debugClassContext));
    }

    protected Multicaster(final String objectName)
    {
        this.objectName = objectName;
        debugClassContext = getClass();
        debugCodeContext(new CodeContext(getClass()));
    }

    protected Multicaster()
    {
        objectName = Name.synthetic(this);
        debugClassContext = getClass();
        debugCodeContext(new CodeContext(getClass()));
    }

    protected Multicaster(final Multicaster that)
    {
        objectName = that.objectName;
        source = that.source;
        debugCodeContext = that.debugCodeContext;
        debugClassContext = that.debugClassContext;
        lock = that.lock;

        audience.addAll(that.audience);

        assert debugCodeContext != null;
        assert debugCodeContext.typeName() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(final Listener listener, final Filter<Transmittable> filter)
    {
        ensure(listener != this, "Cannot listen to yourself");

        // If the listener to this multicaster is also a multicaster,
        if (listener instanceof Broadcaster)
        {
            // then we are the parent of that multicaster. This information is used to provide the listener
            // chain when it doesn't terminate correctly.
            ((Broadcaster) listener).messageSource(this);
        }

        ensureNotNull(listener);
        lock().write(() ->
        {
            final var receiver = new AudienceMember(listener, filter);
            if (!audience.contains(receiver))
            {
                audience.add(receiver);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearListeners()
    {
        lock().write(audience::clear);
    }

    @Override
    public Class<?> debugClassContext()
    {
        return debugClassContext;
    }

    @Override
    public CodeContext debugCodeContext()
    {
        return debugCodeContext;
    }

    @Override
    public void debugCodeContext(final CodeContext context)
    {
        debugCodeContext = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasListeners()
    {
        return lock().read(() -> !audience.isEmpty());
    }

    /**
     * @return True if no listeners can hear any messages
     */
    @JsonIgnore
    public boolean isDeaf()
    {
        return lock().read(() ->
        {
            for (final var receiver : audience)
            {
                if (!receiver.listener().isDeaf())
                {
                    return false;
                }
            }
            return true;
        });
    }

    /**
     * @return The chain of broadcasters that leads to this {@link Multicaster}.
     */
    @NotNull
    public StringList listenerChain()
    {
        final var chain = new StringList();
        for (var at = (Broadcaster) this; at.messageSource() != null; at = at.messageSource())
        {
            chain.append(at.getClass().getSimpleName());
        }
        return chain.reversed();
    }

    /**
     * @return An indented tree of listeners to this multicaster
     */
    public String listenerTree()
    {
        final var builder = new IndentingStringBuilder(TEXT, Indentation.of(4));
        listenerTree(builder);
        return builder.toString();
    }

    /**
     * @return The listener or listener chain
     */
    public List<Listener> listeners()
    {
        return lock().read(() -> audience
                .stream()
                .map(AudienceMember::listener)
                .collect(Collectors.toList()));
    }

    @Override
    public Broadcaster messageSource()
    {
        return source;
    }

    @Override
    public void messageSource(final Broadcaster source)
    {
        this.source = source;
    }

    @Override
    public String objectName()
    {
        return objectName;
    }

    /**
     * Removes the given listener from receiving broadcast messages
     */
    @Override
    public void removeListener(final Listener listener)
    {
        lock().write(() ->
        {
            audience.remove(new AudienceMember(listener, null));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transmit(final Transmittable message)
    {
        lock().read(() ->
        {
            if (!audience.isEmpty())
            {
                // Add this broadcaster's context to the message
                if (message instanceof Message)
                {
                    ((OperationMessage) message).context(debugCodeContext);
                }

                // then send to members of the audience
                for (final var member : audience)
                {
                    try
                    {
                        member.receive(message);
                    }
                    catch (final Exception e)
                    {
                        LOGGER.problem(e, "Listener threw exception");
                    }
                }
            }
            else
            {
                // If there is no receiver for this message, and it can be logged,
                if (message instanceof Message)
                {
                    // then log it.
                    LOGGER.log((Message) message);
                }

                // Notify that there was nowhere to send the message.
                if (!JavaVirtualMachine.isPropertyTrue("KIVAKIT_IGNORE_MISSING_LISTENERS"))
                {
                    LOGGER.warning("Broken listener chain:\n$", listenerChain()
                            .append("[no listener]")
                            .numbered()
                            .indented(4)
                            .join("\n"));
                }
            }
        });
    }

    private void listenerTree(final IndentingStringBuilder builder)
    {
        lock().read(() ->
        {
            builder.appendLine(objectName());
            builder.indent();
            for (final var receiver : audience)
            {
                if (receiver.listener() instanceof Multicaster)
                {
                    ((Multicaster) receiver.listener()).listenerTree(builder);
                }
                else
                {
                    builder.appendLine(receiver.listener().objectName());
                }
            }
            builder.unindent();
        });
    }

    private ReadWriteLock lock()
    {
        if (lock == null)
        {
            lock = new ReadWriteLock();
        }
        return lock;
    }
}
