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

package com.telenav.kivakit.core.messaging.repeaters;

import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.internal.lexakai.DiagramRepeater;
import com.telenav.kivakit.interfaces.messaging.Transmittable;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.mixins.Mixin;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

/**
 * A multicasting repeater which repeats all the messages it receives to a set of listeners. A base repeater is a
 * convenient way of implementing the {@link Repeater} interface by extension.
 *
 * <p>
 * If a class is already extending some other base class a stateful {@link RepeaterMixin} can be used:
 * </p>
 *
 * <pre>
 * public class A extends B implements RepeaterTrait
 * {
 * }
 * </pre>
 *
 * @author jonathanl (shibo)
 * @see Listener
 * @see Multicaster
 * @see Repeater
 * @see RepeaterMixin
 * @see Mixin
 */
@UmlClassDiagram(diagram = DiagramRepeater.class)
public class BaseRepeater extends Multicaster implements Repeater
{
    public BaseRepeater(String objectName, Class<?> classContext)
    {
        super(objectName, classContext);
        checkInheritance();
    }

    public BaseRepeater(Class<?> classContext)
    {
        super(classContext);
        checkInheritance();
    }

    protected BaseRepeater()
    {
        checkInheritance();
    }

    protected BaseRepeater(String objectName)
    {
        super(objectName);
        checkInheritance();
    }

    @Override
    public void onMessage(Message message)
    {
    }

    @Override
    public void onReceive(Transmittable message)
    {
        onMessage((Message) message);

        if (isRepeating())
        {
            transmit(message);
        }
    }

    private void checkInheritance()
    {
        ensure(!(this instanceof RepeaterMixin), "A class extending BaseRepeater cannot also have a RepeaterMixin");
    }
}
