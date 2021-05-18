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

package com.telenav.kivakit.kernel.interfaces.value;

import com.telenav.kivakit.kernel.interfaces.factory.Factory;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramExampleBaseList;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Interface implemented by subclassed objects which need to operate on instances created by the subclass. Unlike {@link
 * Factory}, this interface does not subclass {@link Source}.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramExampleBaseList.class)
public interface NewInstance<T>
{
    /**
     * @return Creates a new object instance
     */
    default T newInstance()
    {
        return onNewInstance();
    }

    /**
     * @return A new instance of type type
     */
    T onNewInstance();
}
