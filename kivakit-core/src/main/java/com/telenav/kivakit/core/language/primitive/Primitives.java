package com.telenav.kivakit.core.language.primitive;

import com.telenav.kivakit.annotations.code.ApiQuality;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE_STATIC_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;

/**
 * Methods for working with primitive types
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = STABLE_STATIC_EXTENSIBLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
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
