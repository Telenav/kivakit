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

package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.core.project.lexakai.DiagramReflection;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

@UmlClassDiagram(diagram = DiagramReflection.class)
public interface Getter extends Named
{
    <T extends Annotation> T annotation(Class<T> annotationType);

    /**
     * @param object The object to get from
     * @return The object retrieved or {@link ReflectionProblem} if something went wrong
     */
    Object get(Object object);

    Class<?> type();
}
