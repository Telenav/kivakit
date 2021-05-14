////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.messaging.listeners;

import com.telenav.kivakit.kernel.logging.Logger;
import com.telenav.kivakit.kernel.logging.LoggerFactory;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageListenerType;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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
