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

package com.telenav.kivakit.interfaces.loading;

import com.telenav.kivakit.interfaces.project.lexakai.diagrams.DiagramInterfacePersistence;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A lazy-loaded object is able to clear references to reloadable data, reducing its memory footprint. Unloading an
 * object shouldn't affect its data, just whether it is present in memory.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramInterfacePersistence.class)
public interface Unloadable
{
    /**
     * Getting rid of re-loadable data
     */
    void unload();
}