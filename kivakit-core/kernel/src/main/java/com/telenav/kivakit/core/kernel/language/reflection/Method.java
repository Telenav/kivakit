////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  © 2011-2021 Telenav, Inc.
//  Licensed under Apache License, Version 2.0
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package com.telenav.kivakit.core.kernel.language.reflection;

import com.telenav.kivakit.core.kernel.language.strings.Strings;
import com.telenav.kivakit.core.kernel.project.lexakai.diagrams.DiagramLanguageReflection;
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
