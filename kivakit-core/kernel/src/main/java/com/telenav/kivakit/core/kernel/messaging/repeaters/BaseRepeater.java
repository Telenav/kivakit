////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.repeaters;

import com.telenav.kivakit.core.kernel.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.messaging.Repeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageRepeater;

/**
 * A multicasting repeater which repeats all the messages it receives to a set of listeners. A base repeater is a
 * convenient way of implementing the {@link Repeater} interface by extension.
 * <p>
 * However, if a class is already extending some other base class (and since Java does not support mix-ins), an instance
 * of {@link BaseRepeater} can be aggregated and its methods delegated to implement the {@link Repeater} interface with
 * minimal effort:
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
 *     public void broadcastTo(final Listener&lt;Message&gt; listener)
 *     {
 *         this.repeater.broadcastTo(listener);
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
 * @see Listener
 * @see Multicaster
 * @see Repeater
 */
@UmlClassDiagram(diagram = DiagramMessageRepeater.class)
public class BaseRepeater extends Multicaster implements Repeater
{
    public BaseRepeater(final String objectName, final Class<?> classContext)
    {
        super(objectName, classContext);
    }

    public BaseRepeater(final Class<?> classContext)
    {
        super(classContext);
    }

    protected BaseRepeater()
    {
    }

    protected BaseRepeater(final String objectName)
    {
        super(objectName);
    }

    @Override
    public void onMessage(final Message message)
    {
        transmit(message);
    }
}
