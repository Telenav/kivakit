package com.telenav.kivakit.kernel.language.reflection;

import java.lang.annotation.Annotation;
import java.util.List;

public abstract class Member
{
    public <T extends Annotation> T annotation(Class<T> annotationClass)
    {
        return type().annotation(annotationClass);
    }

    public abstract <T> List<Type<T>> genericTypeParameters();

    public abstract String name();

    public abstract Type<?> type();
}
