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

package com.telenav.kivakit.kernel.interfaces.messaging;

import com.telenav.kivakit.kernel.messaging.Broadcaster;
import com.telenav.kivakit.kernel.messaging.Listener;
import com.telenav.kivakit.kernel.messaging.Message;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceMessaging;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A message object that can be transmitted with a {@link Broadcaster} and received by a {@link Listener}. Although
 * {@link Message} is the primary implementor {@link Transmittable} any class can implement this interface and
 * participate in the broadcaster / listener framework.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceMessaging.class)
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
public interface Transmittable
{
}
