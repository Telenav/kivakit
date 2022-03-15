package com.telenav.kivakit.core.language.reflection;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class Member
{
    public <T extends Annotation> T annotation(Class<T> annotationClass)
    {
        return type().annotation(annotationClass);
    }

    /**
     * Gets this member's element type, if it is an array. The element type for arrays is the component type, and for
     * methods, it is the return type.
     *
     * @return The element type for this array, or null if this member is not an array
     */
    public abstract <T> List<Type<T>> arrayElementType();

    /**
     * Gets this member's type parameters, if any. The member's type for arrays is the component type, and for methods,
     * it is the return type.
     *
     * @return The generic type parameters for this member, or null if this member is not parameterized
     */
    public abstract <T> List<Type<T>> genericTypeParameters();

    public abstract String name();

    public abstract Type<?> type();
}
