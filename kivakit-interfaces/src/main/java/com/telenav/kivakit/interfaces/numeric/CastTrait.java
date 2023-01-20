package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_NOT_NEEDED;

/**
 * Utility methods to cast numbers, and to discover the minimum and maximum values for the type of number
 *
 * @author jonathanl (shibo)
 */
@TypeQuality(stability = STABLE,
             testing = TESTING_NOT_NEEDED,
             documentation = DOCUMENTED)
public interface CastTrait
{
    /**
     * Returns the given value cast to the given number type
     */
    @SuppressWarnings("unchecked")
    default <T extends Number> T cast(long value, Class<T> type)
    {
        return switch (type.getSimpleName())
            {
                case "Long" -> (T) Long.valueOf(value);
                case "Integer" -> (T) Integer.valueOf((int) value);
                case "Short" -> (T) Short.valueOf((short) value);
                case "Byte" -> (T) Byte.valueOf((byte) value);
                case "Double" -> (T) Double.valueOf((double) value);
                case "Float" -> (T) Float.valueOf((float) value);
                default -> throw new IllegalStateException("Unsupported numeric type: " + type);
            };
    }

    /**
     * Returns the given number cast to the given number type
     */
    default <T extends Number> T cast(LongValued number, Class<T> type)
    {
        return cast(number.asLong(), type);
    }

    /**
     * Returns the maximum value for the given number type
     */
    default <T extends Number> long maximum(Class<T> type)
    {
        return switch (type.getSimpleName())
            {
                case "Double", "Float", "Long" -> Long.MAX_VALUE;
                case "Integer" -> Integer.MAX_VALUE;
                case "Short" -> Short.MAX_VALUE;
                case "Byte" -> Byte.MAX_VALUE;
                default -> throw new IllegalStateException("Unsupported numeric type: " + type);
            };
    }

    /**
     * Returns the minimum value for the given number typee
     */
    default <T extends Number> long minimum(Class<T> type)
    {
        return switch (type.getSimpleName())
            {
                case "Double", "Float", "Long" -> Long.MIN_VALUE;
                case "Integer" -> Integer.MIN_VALUE;
                case "Short" -> Short.MIN_VALUE;
                case "Byte" -> Byte.MIN_VALUE;
                default -> throw new IllegalStateException("Unsupported numeric type: " + type);
            };
    }
}
