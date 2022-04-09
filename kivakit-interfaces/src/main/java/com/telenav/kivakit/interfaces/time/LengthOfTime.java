package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.interfaces.lexakai.DiagramTime;
import com.telenav.kivakit.interfaces.numeric.Quantizable;
import com.telenav.kivakit.interfaces.string.Stringable;
import com.telenav.lexakai.annotations.UmlClassDiagram;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Interface to an object having a length of time, measured in milliseconds.
 *
 * <p><b>Conversion</b></p>
 *
 * <p>
 * A length of time can be converted to specific time units by calling one of the following methods:
 * <ul>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 * </ul>
 *
 * <p><b>Arithmetic</b></p>
 *
 * <ul>
 *     <li>{@link #plus(LengthOfTime)}</li>
 *     <li>{@link #minus(LengthOfTime)}</li>
 *     <li>{@link #times(double)}</li>
 *     <li>{@link #dividedBy(double)}</li>
 * </ul>
 *
 * <p><b>Comparison</b></p>
 *
 * <ul>
 *     <li>{@link #compareTo(LengthOfTime)}</li>
 *     <li>{@link #isLessThan(LengthOfTime)}</li>
 *     <li>{@link #isLessThanOrEqualTo(LengthOfTime)}</li>
 *     <li>{@link #isGreaterThan(LengthOfTime)}</li>
 *     <li>{@link #isLessThanOrEqualTo(LengthOfTime)}</li>
 *     <li>{@link #isApproximately(LengthOfTime, LengthOfTime)}</li>
 *     <li>{@link #isZero()}</li>
 *     <li>{@link #isNonZero()}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
public interface LengthOfTime<T extends LengthOfTime<T>> extends
        Quantizable,
        Comparable<LengthOfTime<?>>,
        Stringable
{
    double WEEKS_PER_YEAR = 52.177457;

    /**
     * Retrieves the number of days of the current Duration.
     *
     * @return Number of days of the current Duration
     */
    default double asDays()
    {
        return asHours() / 24.0;
    }

    /**
     * Retrieves the number of hours of the current Duration.
     *
     * @return number of hours of the current Duration
     */
    default double asHours()
    {
        return asMinutes() / 60.0;
    }

    @NotNull
    default String asHumanReadableString()
    {
        if (asMilliseconds() >= 0)
        {
            if (asYears() >= 1.0)
            {
                return unitString(asYears(), "year");
            }
            if (asWeeks() >= 1.0)
            {
                return unitString(asWeeks(), "week");
            }
            if (asDays() >= 1.0)
            {
                return unitString(asDays(), "day");
            }
            if (asHours() >= 1.0)
            {
                return unitString(asHours(), "hour");
            }
            if (asMinutes() >= 1.0)
            {
                return unitString(asMinutes(), "minute");
            }
            if (asSeconds() >= 1.0)
            {
                return unitString(asSeconds(), "second");
            }
            return asMilliseconds() + " millisecond" + (asMilliseconds() != 1 ? "s" : "");
        }
        else
        {
            return "N/A";
        }
    }

    /**
     * Returns a java.time.Duration object for the number of milliseconds
     */
    default java.time.Duration asJavaDuration()
    {
        return java.time.Duration.ofMillis(milliseconds());
    }

    /**
     * Returns the number of milliseconds
     */
    default double asMilliseconds()
    {
        return milliseconds();
    }

    /**
     * Retrieves the number of minutes of the current Duration.
     *
     * @return number of minutes of the current Duration
     */
    default double asMinutes()
    {
        return asSeconds() / 60.0;
    }

    /**
     * Retrieves the number of seconds of the current Duration.
     *
     * @return number of seconds of the current Duration
     */
    default double asSeconds()
    {
        return asMilliseconds() / 1000.0;
    }

    /**
     * Retrieves the String representation of this Duration in days, hours, minutes, seconds or milliseconds, as
     * appropriate.
     *
     * @return a String representation
     */
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    default String asString(Format format)
    {
        switch (format)
        {
            case FILESYSTEM:
                return asHumanReadableString().replace(' ', '_');

            case PROGRAMMATIC:
                return String.valueOf(milliseconds());

            case COMPACT:
            {

                String dateFormatPattern;
                if (asHours() > 1.0)
                {
                    dateFormatPattern = "H:mm:ss";
                }
                else if (asMinutes() > 1.0)
                {
                    dateFormatPattern = "m:ss'm'";
                }
                else if (asSeconds() > 1.0)
                {
                    dateFormatPattern = "s's'";
                }
                else
                {
                    dateFormatPattern = "S'ms'";
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = new Date(milliseconds());
                return dateFormat.format(date);
            }

            case DEBUG:
            case USER_SINGLE_LINE:
            case USER_LABEL:
            case HTML:
            case TO_STRING:
            case LOG:
            case USER_MULTILINE:
            case TEXT:
            default:
                return asHumanReadableString();
        }
    }

    /**
     * Retrieves the number of weeks of the current Duration.
     *
     * @return Number of weeks of the current Duration
     */
    default double asWeeks()
    {
        return asDays() / 7;
    }

    /**
     * Retrieves the number of years of the current Duration.
     *
     * @return Number of years of the current Duration
     */
    default double asYears()
    {
        return asWeeks() / WEEKS_PER_YEAR;
    }

    /**
     * Waits for the given {@link Condition} variable to be true
     *
     * @param condition The condition variable
     */
    default boolean await(Condition condition)
    {
        try
        {
            return condition.await(milliseconds(), TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException ignored)
        {
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(LengthOfTime<?> that)
    {
        return Long.compare(milliseconds(), that.milliseconds());
    }

    default T dividedBy(double value)
    {
        return newInstance((long) (milliseconds() / value));
    }

    /**
     * @return Number of milliseconds in this duration
     */
    long milliseconds();

    default T minus(LengthOfTime<?> that)
    {
        return newInstance(milliseconds() - that.milliseconds());
    }

    T newInstance(long milliseconds);

    default T plus(LengthOfTime<?> that)
    {
        return newInstance(milliseconds() + that.milliseconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default long quantum()
    {
        return milliseconds();
    }

    /**
     * Calls the given callback at this fixed rate
     */
    default void repeat(Callback<Timer> onTimer)
    {
        var timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                onTimer.callback(timer);
            }
        }, 0L, milliseconds());
    }

    /**
     * Sleeps for the current Duration.
     */
    default void sleep()
    {
        if (milliseconds() > 0)
        {
            try
            {
                Thread.sleep(milliseconds());
            }
            catch (InterruptedException e)
            {
                // Ignored
            }
        }
    }

    default T times(double value)
    {
        return newInstance((long) (milliseconds() * value));
    }

    /**
     * Converts a value to a unit-suffixed value, taking care of English singular/plural suffix.
     *
     * @param value a double value to format
     * @param units the units to apply singular or plural suffix to
     * @return a String representation
     */
    default String unitString(double value, String units)
    {
        var format = new DecimalFormat("###,###.##");
        return format.format(value) + " " + units + (value > 1.0 ? "s" : "");
    }

    /**
     * Wait for this duration on the given monitor. Note that a duration of NONE is considered to be a wait time of zero
     * milliseconds, whereas the underlying Java {@link #wait(long)} considers zero milliseconds to be infinite wait
     * time.
     *
     * @param monitor The monitor to wait on
     * @return True if the thread waited, false if it was interrupted
     */
    @SuppressWarnings({ "UnusedReturnValue", "SynchronizationOnLocalVariableOrMethodParameter" })
    default boolean wait(Object monitor)
    {
        synchronized (monitor)
        {
            try
            {
                var milliseconds = milliseconds();
                if (milliseconds > 0)
                {
                    monitor.wait(milliseconds);
                }
                return true;
            }
            catch (InterruptedException e)
            {
                return false;
            }
        }
    }

    default void waitThen(Callback<Timer> onTimer)
    {
        var timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onTimer.callback(timer);
            }
        }, milliseconds());
    }
}
