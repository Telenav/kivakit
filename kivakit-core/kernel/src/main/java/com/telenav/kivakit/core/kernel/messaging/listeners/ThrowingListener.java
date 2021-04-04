////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.messaging.listeners;

import com.telenav.kivakit.core.kernel.logging.Logger;
import com.telenav.kivakit.core.kernel.logging.LoggerFactory;
import com.telenav.kivakit.core.kernel.messaging.Listener;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramMessageListenerType;

/**
 * Listens to {@link Message}s of a given type. All problems are logged. All failure messages result in an exception
 * being thrown. All other messages are ignored.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
public class ThrowingListener implements Listener
{
    private static final Logger LOGGER = LoggerFactory.newLogger();

    @Override
    public void onMessage(final Message message)
    {
        switch (message.status())
        {
            case PROBLEM:
                LOGGER.log(message);
                break;

            case FAILED:
                LOGGER.log(message);
                throw message.asException();
        }

        switch (message.operationStatus())
        {
            case FAILED:
            case HALTED:
                LOGGER.log(message);
                throw message.asException();
        }
    }
}
