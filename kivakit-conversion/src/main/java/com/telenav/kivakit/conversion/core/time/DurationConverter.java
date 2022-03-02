package com.telenav.kivakit.conversion.core.time;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Duration;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.string.Strings.isOneOf;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Converts the given <code>String</code> to a new <code>Duration</code> object. The string can take the form of a
 * floating point number followed by a number of milliseconds, seconds, minutes, hours or days. For example "6 hours" or
 * "3.4 days". Parsing is case-insensitive.
 *
 * @author jonathanl (shibo)
 */
public class DurationConverter extends BaseStringConverter<Duration>
{
    /** Pattern to match strings */
    private final Pattern PATTERN = Pattern.compile(
            "(?x) (?<quantity> [0-9]+ ([.,] [0-9]+)?) "
                    + "(\\s+ | - | _)?"
                    + "(?<units> d | h | m | s | ms | ((millisecond | second | minute | hour | day | week | year) s?))", CASE_INSENSITIVE);

    public DurationConverter(Listener listener)
    {
        super(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Duration onToValue(String value)
    {
        var matcher = PATTERN.matcher(value);
        if (matcher.matches())
        {
            var quantity = Double.parseDouble(matcher.group("quantity"));
            var units = matcher.group("units");
            if (isOneOf(units, "milliseconds", "millisecond", "ms"))
            {
                return Duration.milliseconds(quantity);
            }
            else if (isOneOf(units, "seconds", "second", "s"))
            {
                return Duration.seconds(quantity);
            }
            else if (isOneOf(units, "minutes", "minute", "m"))
            {
                return Duration.minutes(quantity);
            }
            else if (isOneOf(units, "hours", "hour", "h"))
            {
                return Duration.hours(quantity);
            }
            else if (isOneOf(units, "days", "day", "d"))
            {
                return Duration.days(quantity);
            }
            else if (isOneOf(units, "weeks", "week"))
            {
                return Duration.weeks(quantity);
            }
            else if (isOneOf(units, "years", "year"))
            {
                return Duration.years(quantity);
            }
            else
            {
                problem("Unrecognized units: ${debug}", value);
                return null;
            }
        }
        else
        {
            problem("Unable to parse: ${debug}", value);
            return null;
        }
    }
}
