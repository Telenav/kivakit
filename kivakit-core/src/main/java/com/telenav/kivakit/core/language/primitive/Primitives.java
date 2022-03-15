package com.telenav.kivakit.core.language.primitive;

public class Primitives
{
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
