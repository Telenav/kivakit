package com.telenav.kivakit.core.language.primitive;

import com.telenav.kivakit.annotations.code.CodeQuality;

import static com.telenav.kivakit.annotations.code.CodeStability.CODE_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;

/**
 * Methods for working with primitive types
 *
 * @author jonathanl (shibo)
 */
@CodeQuality(stability = CODE_STABLE_EXTENSIBLE,
             testing = TESTING_NONE,
             documentation = DOCUMENTATION_COMPLETE)
public class Primitives
{
    /**
     * Returns true if the given type is a primitive class.
     */
    public static boolean isPrimitive(Class<?> type)
    {
        return Long.TYPE.equals(type)
                || Integer.TYPE.equals(type)
                || Short.TYPE.equals(type)
                || Character.TYPE.equals(type)
                || Byte.TYPE.equals(type)
                || Boolean.TYPE.equals(type)
                || Double.TYPE.equals(type)
                || Float.TYPE.equals(type);
    }

    /**
     * Returns true if the given object is a primitive wrapper type, like Integer or Byte.
     */
    public static boolean isPrimitiveWrapper(Object object)
    {
        return object instanceof Long
                || object instanceof Integer
                || object instanceof Short
                || object instanceof Character
                || object instanceof Byte
                || object instanceof Boolean
                || object instanceof Float;
    }
}
