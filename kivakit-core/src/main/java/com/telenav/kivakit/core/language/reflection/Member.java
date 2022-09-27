package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.annotations.code.ApiQuality;

import java.lang.annotation.Annotation;
import java.util.List;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_EXPANDABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Defines methods common to {@link Field}s and {@link Method}s.
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_EXPANDABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = FULLY_DOCUMENTED)
public abstract class Member
{
    /**
     * Gets any annotation of the given type from this member
     */
    public abstract <T extends Annotation> T annotation(Class<T> annotationClass);

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

    /**
     * Returns the name of this member
     */
    public abstract String name();

    /**
     * Returns the type to which this member belongs
     */
    public abstract Type<?> type();
}
