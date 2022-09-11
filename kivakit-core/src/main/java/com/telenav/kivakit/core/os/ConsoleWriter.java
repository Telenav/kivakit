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

package com.telenav.kivakit.core.os;

import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.internal.lexakai.DiagramOs;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Writes messages to the console. Messages that represent success are written to {@link System#out}, while messages
 * that represent failure are written to {@link System#err}.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("AccessStaticViaInstance")
@UmlClassDiagram(diagram = DiagramOs.class)
public class ConsoleWriter implements Listener
{
    private final Console console = new Console();

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(Message message)
    {
        switch (message.operationStatus())
        {
            case FAILED:
            case HALTED:
                console.print(Console.OutputType.ERROR, message.asString());
                return;
        }

        switch (message.status())
        {
            case FAILED:
            case PROBLEM:
                console.print(Console.OutputType.ERROR, message.asString());
                return;
        }

        console.print(Console.OutputType.NORMAL, message.asString());
    }
}
