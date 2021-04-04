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

@UmlClassDiagram(diagram = DiagramMessageListenerType.class)
public class NullListener implements Listener
{
    @Override
    public boolean isDeaf()
    {
        return true;
    }

    @Override
    public void onMessage(final Message message)
    {
    }
}
