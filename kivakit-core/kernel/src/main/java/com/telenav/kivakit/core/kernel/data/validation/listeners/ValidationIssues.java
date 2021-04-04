////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.data.validation.listeners;

import com.telenav.kivakit.core.kernel.messaging.listeners.MessageCounter;
import com.telenav.kivakit.core.kernel.messaging.listeners.MessageList;
import com.telenav.kivakit.core.kernel.messaging.Message;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramDataValidation;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A {@link MessageList} and {@link MessageCounter} that captures validation issues. The number of issues of different
 * types can be retrieved with {@link #count(Class)}, passing in the type of message for which a count is desired. The
 * method {@link #countWorseThanOrEqualTo(Message.Status)} gives a count of all messages that are at least as bad or
 * worse than the given message status value. For example, <i>countWorseThanOrEqualTo(Status.PROBLEM)</i>.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramDataValidation.class)
public class ValidationIssues extends MessageList
{
    public ValidationIssues()
    {
        // Keep anything that doesn't represent outright success
        super(message -> !message.status().succeeded());
    }
}
