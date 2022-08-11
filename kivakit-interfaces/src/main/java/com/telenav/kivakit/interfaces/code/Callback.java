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

package com.telenav.kivakit.interfaces.code;

import com.telenav.kivakit.interfaces.internal.lexakai.DiagramCode;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * A simple callback interface. There are other similar interfaces, but sometimes the best name is callback.
 *
 * @param <Value> The type of object to be passed to the callback
 * @author jonathanl (shibo)
 */
@FunctionalInterface
@UmlClassDiagram(diagram = DiagramCode.class)
public interface Callback<Value>
{
    default void callback(Value value)
    {
        onCallback(value);
    }

    /**
     * The callback implementation
     *
     * @param value The value passed to the callback code
     */
    void onCallback(Value value);
}
