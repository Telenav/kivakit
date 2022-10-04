package com.telenav.kivakit.interfaces.numeric;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.value.LongValued;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NOT_NEEDED;

/**
 * Utility methods to cast numbers, and to discover the minimum and maximum values for the type of number
 *
 * @author jonathanl (shibo)
 */
@ApiQuality(stability = API_STABLE,
            testing = TESTING_NOT_NEEDED,
            documentation = DOCUMENTATION_COMPLETE)
public interface CastTrait
{
    /**
     * Returns the given value cast to the given number type
     */
    @SuppressWarnings("unchecked")
    default <T extends Number> T cast(long value, Class<T> type)
    {
        switch (type.getSimpleName())
        {
            case "Long":
                return (T) Long.valueOf(value);

            case "Integer":
                return (T) Integer.valueOf((int) value);

            case "Short":
                return (T) Short.valueOf((short) value);

            case "Byte":
                return (T) Byte.valueOf((byte) value);

            case "Double":
                return (T) Double.valueOf((double) value);

            case "Float":
                return (T) Float.valueOf((float) value);

            default:
                throw new IllegalStateException("Unsupported numeric type: " + type);
        }
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
        switch (type.getSimpleName())
        {
            case "Double":
            case "Float":
            case "Long":
                return Long.MAX_VALUE;

            case "Integer":
                return Integer.MAX_VALUE;

            case "Short":
                return Short.MAX_VALUE;

            case "Byte":
                return Byte.MAX_VALUE;

            default:
                throw new IllegalStateException("Unsupported numeric type: " + type);
        }
    }

    /**
     * Returns the minimum value for the given number typee
     */
    default <T extends Number> long minimum(Class<T> type)
    {
        switch (type.getSimpleName())
        {
            case "Double":
            case "Float":
            case "Long":
                return Long.MIN_VALUE;

            case "Integer":
                return Integer.MIN_VALUE;

            case "Short":
                return Short.MIN_VALUE;

            case "Byte":
                return Byte.MIN_VALUE;

            default:
                throw new IllegalStateException("Unsupported numeric type: " + type);
        }
    }
}
