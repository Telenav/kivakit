////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
// © 2011-2021 Telenav, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.kernel.language.reflection;

import com.telenav.kivakit.kernel.language.strings.Strings;
import com.telenav.kivakit.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
import com.telenav.lexakai.annotations.UmlClassDiagram;

/**
 * Holds a class and method name when a proper Java reflection {@link java.lang.reflect.Method} cannot be determined, as
 * in the case of the limited and poorly designed {@link StackTraceElement} class.
 *
 * @author jonathanl (shibo)
 */
@UmlClassDiagram(diagram = DiagramLanguageReflection.class)
public class Method
{
    /**
     * @return A {@link Method} instance for the given stack frame
     */
    public static Method of(final StackTraceElement frame)
    {
        try
        {
            final var type = Class.forName(frame.getClassName());
            if (!Strings.isEmpty(frame.getMethodName()))
            {
                return new Method(type, frame.getMethodName());
            }
        }
        catch (final ClassNotFoundException ignored)
        {
        }
        return null;
    }

    private final Class<?> type;

    private final String methodName;

    private Method(final Class<?> type, final String methodName)
    {
        this.type = type;
        this.methodName = methodName;
    }

    public String methodName()
    {
        return methodName;
    }

    @Override
    public String toString()
    {
        return type.getSimpleName() + "." + methodName + "()";
    }

    public Class<?> type()
    {
        return type;
    }
}