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

import com.telenav.kivakit.kernel.interfaces.value.Source;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramInterfaceMessaging;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessageBroadcaster;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramMessaging;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import com.telenav.lexakai.annotations.associations.UmlRelation;

import java.util.function.Supplier;

/**
 * A transmitter of values with similar function to a {@link Source}, or a {@link Supplier}. {@link Transmitter}s are
 * used in the messaging framework to transmit {@link Transmittable} values.
 *
 * @author jonathanl (shibo)
 * @see Transmittable
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramInterfaceMessaging.class)
@UmlClassDiagram(diagram = DiagramMessaging.class)
@UmlClassDiagram(diagram = DiagramMessageBroadcaster.class)
@UmlRelation(label = "transmits", referent = Transmittable.class)
public interface Transmitter<T>
{
    /**
     * Receives the given value
     */
    void transmit(T value);
}
