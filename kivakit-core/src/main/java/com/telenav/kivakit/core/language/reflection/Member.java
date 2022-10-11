package com.telenav.kivakit.core.language.reflection;

import com.telenav.kivakit.annotations.code.quality.CodeQuality;
import com.telenav.kivakit.core.collections.list.ObjectList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Defines methods common to {@link Field}s and {@link Method}s.
 *
 * <p><b>Annotations</b></p>
 *
 * <ul>
 *     <li>{@link #annotation(Class)}</li>
 *     <li>{@link #hasAnnotation(Class)}</li>
 * </ul>
 *
 * <p><b>Modifiers</b></p>
 *
 * <ul>
 *     <li>{@link #isFinal()}</li>
 *     <li>{@link #isPrivate()}</li>
 *     <li>{@link #isProtected()}</li>
 *     <li>{@link #isPublic()}</li>
 *     <li>{@link #isStatic()}</li>
 *     <li>{@link #isSynthetic()}</li>
 *     <li>{@link #modifiers()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #arrayElementType()}</li>
 *     <li>{@link #genericTypeParameters()}</li>
 *     <li>{@link #name()}</li>
 *     <li>{@link #parentType()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = STABLE_EXTENSIBLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTATION_COMPLETE)
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
    public abstract <T> ObjectList<Type<T>> arrayElementType();

    /**
     * Gets this member's type parameters, if any. The member's type for arrays is the component type, and for methods,
     * it is the return type.
     *
     * @return The generic type parameters for this member, or null if this member is not parameterized
     */
    public abstract <T> ObjectList<Type<T>> genericTypeParameters();

    /**
     * Returns true if this field has an annotation of the given type
     */
    public final boolean hasAnnotation(Class<? extends Annotation> type)
    {
        return annotation(type) != null;
    }

    /**
     * Returns true if this is a final member
     */
    public final boolean isFinal()
    {
        return Modifier.isFinal(modifiers());
    }

    /**
     * Returns true if this is a private member
     */
    public final boolean isPrivate()
    {
        return Modifier.isPrivate(modifiers());
    }

    /**
     * Returns true if this is a protected member
     */
    public final boolean isProtected()
    {
        return Modifier.isProtected(modifiers());
    }

    /**
     * Returns true if this is a public member
     */
    public final boolean isPublic()
    {
        return Modifier.isPublic(modifiers());
    }

    /**
     * Returns true if this is a static member
     */
    public final boolean isStatic()
    {
        return Modifier.isStatic(modifiers());
    }

    /**
     * Returns true if this is a synthetic method
     */
    public abstract boolean isSynthetic();

    /**
     * Returns any modifiers for the member
     */
    public abstract int modifiers();

    /**
     * Returns the name of this member
     */
    public abstract String name();

    /**
     * Returns the type to which this member belongs
     */
    public abstract Type<?> parentType();
}
