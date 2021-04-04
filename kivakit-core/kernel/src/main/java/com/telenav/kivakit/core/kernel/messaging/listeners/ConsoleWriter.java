////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.listeners;

import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageListenerType;

/**
 * Writes messages to the console. Messages that represent success are written to {@link System#out}, while messages
 * that represent failure are written to {@link System#err}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
public class ConsoleWriter implements Listener
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(final Message message)
    {
        switch (message.operationStatus())
        {
            case FAILED:
            case HALTED:
                System.err.println(message.asString());
                return;
        }
        switch (message.status())
        {
            case FAILED:
            case PROBLEM:
                System.err.println(message.asString());
                return;
        }
        System.out.println(message.toString());
    }
}
