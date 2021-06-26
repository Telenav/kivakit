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

package com.telenav.kivakit.kernel.messaging.repeaters;

import com.telenav.kivakit.kernel.language.mixin.Mixin;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.messaging.Repeater;
import com.telenav.kivakit.kernel.messaging.broadcasters.Multicaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageRepeater;
import com.telenav.lexakai.annotations.UmlClassDiagram;

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
