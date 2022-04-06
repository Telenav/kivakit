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

package com.telenav.kivakit.core.messaging.listeners;

import com.telenav.kivakit.core.lexakai.DiagramListenerType;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Listens to {@link Message}s of a given type. All problems are logged. All failure messages result in an exception
 * being thrown. All other messages are ignored.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramListenerType.class)
public class ThrowingListener implements Listener
{
    @Override
    public void onMessage(Message message)
    {
        if (message.isFailure())
        {
            throw message.asException();
        }
    }
}
