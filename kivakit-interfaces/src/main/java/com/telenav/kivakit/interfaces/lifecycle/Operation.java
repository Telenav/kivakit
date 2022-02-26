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

package com.telenav.kivakit.interfaces.lifecycle;

import com.telenav.kivakit.interfaces.project.lexakai.diagrams.DiagramInterfaceLifeCycle;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * An operation that can be executing. Calling {@link #isRunning()} will return true if the operation is running.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfaceLifeCycle.class)
public interface Operation
{
    /**
     * @return True if this operation is in progress, false if it has not started yet or if it has been stopped.
     */
    boolean isRunning();
}