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

package com.telenav.kivakit.core.messaging.broadcasters;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramRepeater;
import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.logging.Logger;
import com.telenav.kivakit.core.logging.loggers.ConsoleLogger;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.context.CodeContext;
import com.telenav.kivakit.core.messaging.messages.OperationMessage;
import com.telenav.kivakit.core.string.IndentingStringBuilder;
import com.telenav.kivakit.core.string.IndentingStringBuilder.Indentation;
import com.telenav.kivakit.core.thread.locks.ReadWriteLock;
import com.telenav.kivakit.core.vm.Properties;
import com.telenav.kivakit.interfaces.comparison.Filter;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.interfaces.naming.NamedObject;
import com.telenav.kivakit.mixins.Mixins;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlAggregation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.string.IndentingStringBuilder.Style.TEXT;

/**
 * A multicaster is a broadcaster which can have more than one listener. As with any broadcaster, listeners can be added
 * to a multicaster with {@link #addListener(Listener)} and can be cleared with {@link #clearListeners()}.
 * MessageTransceiver can be broadcast to all listeners with {@link #transmit(Transmittable)}.
 * <p>
 * If a class is already extending some other base class (and since Java does not support mix-ins) an instance of
 * {@link Multicaster} can be aggregated and its methods delegated to implement the {@link Broadcaster} interface This
 * is not automatic in Java, but requires a minimal amount of code:
 * <pre>
 * public class A extends B implements Repeater&lt;Message&gt;
 * {
 *     private final BaseRepeater&lt;Message&gt; repeater = new BaseRepeater&lt;&gt;();
 *
 *     public void broadcast(Message message)
 *     {
 *         this.repeater.broadcast(message);
 *     }
 *
 *     public void addListener(Listener&lt;Message&gt; listener)
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
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramRepeater.class)
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public class Multicaster implements Broadcaster
{
    /** Console logger for serious messaging problems */
    private static final Logger LOGGER = new ConsoleLogger();

    /** This multi-caster audience */
    @UmlAggregation
    private final transient List<AudienceMember> audience = new ArrayList<>();

    /** The class context for any debug object associated with this multicaster */
    private final transient Class<?> debugClassContext;

    /** The code context for any debug object associated with this multicaster */
    private transient CodeContext debugCodeContext;

    /** Lock for synchronizing access to the audience across threads */
    private transient ReadWriteLock lock;

    /** The name of this object */
    private final transient String objectName;

    /** Any broadcaster that is sending messages through this multicaster */
    private transient Broadcaster source;

    /** True if this multicaster is enabled to transmit */
    private transient boolean transmitting;

    public Multicaster(String objectName, Class<?> debugClassContext)
    {
        this.objectName = objectName;
        this.debugClassContext = debugClassContext;
        debugCodeContext(new CodeContext(debugClassContext));
    }

    public Multicaster(Class<?> debugClassContext)
    {
        objectName = NamedObject.syntheticName(this);
        this.debugClassContext = debugClassContext;
        debugCodeContext(new CodeContext(debugClassContext));
    }

    protected Multicaster(String objectName)
    {
        this.objectName = objectName;
        debugClassContext = getClass();
        debugCodeContext(new CodeContext(getClass()));
    }

    protected Multicaster()
    {
        objectName = NamedObject.syntheticName(this);
        debugClassContext = getClass();
        debugCodeContext(new CodeContext(getClass()));
    }

    protected Multicaster(Multicaster that)
    {
        objectName = that.objectName;
        source = that.source;
        debugCodeContext = that.debugCodeContext;
        debugClassContext = that.debugClassContext;
        lock = that.lock;
        transmitting = that.transmitting;

        audience.addAll(that.audience);

        assert debugCodeContext != null;
        assert debugCodeContext.typeName() != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(Listener listener, Filter<Transmittable> filter)
    {
        ensure(listener != this, "Cannot listen to yourself");

        // If the listener to this multicaster is a broadcaster,
        if (listener instanceof Broadcaster)
        {
            // then we are the parent of that broadcaster. This information is used to provide the listener
            // chain when it doesn't terminate correctly.
            ((Broadcaster) listener).messageSource(this);
        }

        ensureNotNull(listener);
        lock().write(() ->
        {
            var receiver = new AudienceMember(listener, filter);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> debugClassContext()
    {
        return debugClassContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CodeContext debugCodeContext()
    {
        return debugCodeContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void debugCodeContext(CodeContext context)
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
    public boolean isDeaf()
    {
        return lock().read(() ->
        {
            for (var receiver : audience)
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
     * {@inheritDoc}
     */
    @Override
    public boolean isTransmitting()
    {
        return transmitting;
    }

    /**
     * @return The chain of broadcasters that leads to this {@link Multicaster}.
     */
    @NotNull
    public List<String> listenerChain()
    {
        var chain = new ArrayList<String>();
        for (var at = (Broadcaster) this; at.messageSource() != null; at = at.messageSource())
        {
            var owner = Mixins.owner(at);
            if (owner != null)
            {
                at = (Broadcaster) owner;
            }
            chain.add(Classes.simpleName(at.getClass()) + (at.listeners().isEmpty() ? " [no listener]" : ""));
        }
        return chain;
    }

    /**
     * @return An indented tree of listeners to this multi-caster
     */
    public String listenerTree()
    {
        var builder = new IndentingStringBuilder(TEXT, Indentation.indentation(4));
        listenerTree(builder);
        return builder.toString();
    }

    /**
     * @return The listener or listener chain
     */
    @Override
    public List<Listener> listeners()
    {
        return lock().read(() -> audience
                .stream()
                .map(AudienceMember::listener)
                .collect(Collectors.toList()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Broadcaster messageSource()
    {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void messageSource(Broadcaster source)
    {
        this.source = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String objectName()
    {
        return objectName;
    }

    /**
     * Removes the given listener from receiving broadcast messages
     */
    @Override
    public void removeListener(Listener listener)
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
    public <M extends Transmittable> M transmit(M message)
    {
        lock().read(() ->
        {
            if (!audience.isEmpty())
            {
                // Add this broadcaster's context to the message
                if (message instanceof OperationMessage)
                {
                    ((OperationMessage) message).context(debugCodeContext);
                }

                // then send to members of the audience
                for (var member : audience)
                {
                    try
                    {
                        member.receive(message);
                    }
                    catch (Exception e)
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
                if (Properties.isSystemPropertyOrEnvironmentVariableFalse("KIVAKIT_IGNORE_MISSING_LISTENERS"))
                {
                    var text = new IndentingStringBuilder();
                    for (var at : listenerChain())
                    {
                        text.appendLine(at);
                    }
                    LOGGER.warning("Broken listener chain:\n$", text.numbered().toString());
                }
            }
        });

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transmitting(boolean transmitting)
    {
        this.transmitting = transmitting;
    }

    private void listenerTree(IndentingStringBuilder builder)
    {
        lock().read(() ->
        {
            builder.appendLine(objectName());
            builder.indent();
            for (var receiver : audience)
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
