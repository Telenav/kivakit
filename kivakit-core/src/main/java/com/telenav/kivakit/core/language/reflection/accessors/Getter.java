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

package com.telenav.kivakit.core.language.reflection.accessors;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.internal.lexakai.DiagramReflection;
import com.telenav.kivakit.core.language.reflection.ReflectionProblem;
import com.telenav.kivakit.core.language.reflection.Type;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.lang.annotation.Annotation;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Interface to a getter, either for a field or a method.
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramReflection.class)
@ApiQuality(stability = STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public interface Getter extends Named
{
    /**
     * Gets the annotation of the given type for this getter
     */
    <T extends Annotation> T annotation(Class<T> annotationType);

    /**
     * @param object The object to get from
     * @return The object retrieved or {@link ReflectionProblem} if something went wrong
     */
    Object get(Object object);

    /**
     * True if this setter has an annotation of the given type
     */
    default boolean hasAnnotation(Class<? extends Annotation> annotation)
    {
        return annotation(annotation) != null;
    }

    /**
     * Gets the type of value that this getter retrieves
     */
    Type<?> type();
}
