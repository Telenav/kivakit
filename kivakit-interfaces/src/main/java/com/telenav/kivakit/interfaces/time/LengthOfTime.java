package com.telenav.kivakit.interfaces.time;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.interfaces.code.Callback;
import com.telenav.kivakit.interfaces.internal.lexakai.DiagramTime;
import com.telenav.kivakit.interfaces.string.StringFormattable;
import com.telenav.kivakit.interfaces.value.LongValued;
import com.telenav.lexakai.annotations.UmlClassDiagram;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.telenav.kivakit.annotations.code.ApiStability.STABLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.FULLY_DOCUMENTED;
import static com.telenav.kivakit.annotations.code.TestingQuality.UNTESTED;
import static com.telenav.kivakit.interfaces.time.WakeState.COMPLETED;
import static com.telenav.kivakit.interfaces.time.WakeState.INTERRUPTED;
import static com.telenav.kivakit.interfaces.time.WakeState.TIMED_OUT;

/**
 * Interface to an object having a length of time, measured in nanoseconds.
 *
 * <p><b>Measurement</b></p>
 *
 * <ul>
 *     <li>{@link #nanoseconds()}</li>
 *     <li>{@link #milliseconds()}</li>
 * </ul>
 *
 * <p><b>Conversion</b></p>
 *
 * <p>
 * A length of time can be converted to specific time units by calling one of the following methods:
 * </p>
 *
 * <ul>
 *     <li>{@link #asString()}</li>
 *     <li>{@link #asString(Format)}</li>
 *     <li>{@link #asNanoseconds()}</li>
 *     <li>{@link #asMicroseconds()}</li>
 *     <li>{@link #asMilliseconds()}</li>
 *     <li>{@link #asSeconds()}</li>
 *     <li>{@link #asMinutes()}</li>
 *     <li>{@link #asHours()}</li>
 *     <li>{@link #asDays()}</li>
 *     <li>{@link #asWeeks()}</li>
 *     <li>{@link #asYears()}</li>
 *     <li>{@link #asJavaDuration()}</li>
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
 *     <li>{@link #isLessThan(Duration)}</li>
 *     <li>{@link #isLessThanOrEqualTo(Duration)}</li>
 *     <li>{@link #isGreaterThan(Duration)}</li>
 *     <li>{@link #isLessThanOrEqualTo(Duration)}</li>
 *     <li>{@link #isApproximately(Duration, Duration)}</li>
 *     <li>{@link #isZero()}</li>
 *     <li>{@link #isNonZero()}</li>
 * </ul>
 *
 * <p><b>Operations</b></p>
 *
 * <ul>
 *     <li>{@link #await(Awaitable)}</li>
 *     <li>{@link #repeat(Callback)}</li>
 *     <li>{@link #sleep()}</li>
 *     <li>{@link #wait(Object)}</li>
 *     <li>{@link #waitThen(Callback)}</li>
 * </ul>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@UmlClassDiagram(diagram = DiagramTime.class)
@ApiQuality(stability = STABLE,
            testing = UNTESTED,
            documentation = FULLY_DOCUMENTED)
public interface LengthOfTime<Duration extends LongValued & LengthOfTime<Duration>> extends
        LongValued,
        Comparable<LengthOfTime<?>>,
        StringFormattable,
        TimeMeasurement
{

    /**
     * Returns a java.time.Duration object for the number of milliseconds
     */
    default java.time.Duration asJavaDuration()
    {
        return java.time.Duration.ofMillis(milliseconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "SpellCheckingInspection", "DuplicatedCode" })
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
     * Wait for the given operation to complete or time out.
     */
    default WakeState await(Awaitable awaitable)
    {
        try
        {
            if (awaitable.await(milliseconds(), TimeUnit.MILLISECONDS))
            {
                return COMPLETED;
            }
            return TIMED_OUT;
        }
        catch (InterruptedException e)
        {
            return INTERRUPTED;
        }
        catch (Exception e)
        {
            return WakeState.terminated(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(LengthOfTime<?> that)
    {
        return nanoseconds().compareTo(that.nanoseconds());
    }

    /**
     * @return The positive difference between this length of time and that one
     */
    default Duration difference(Duration that)
    {
        if (isGreaterThan(that))
        {
            return minus(that);
        }
        else
        {
            return that.minus(this);
        }
    }

    /**
     * @return This length of time divided by the given divisor
     */
    default Duration dividedBy(double value)
    {
        return newDuration(nanoseconds().dividedBy(value));
    }

    /**
     * @return This length of time divided by the given divisor
     */
    default Duration dividedBy(Duration that)
    {
        return newDuration(nanoseconds().dividedBy(that.nanoseconds().asDouble()));
    }

    /**
     * @return This length of time divided by the given divisor
     */
    default Duration dividedBy(long value)
    {
        return dividedBy((double) value);
    }

    /**
     * @return The number of milliseconds
     */
    @Override
    default long longValue()
    {
        return milliseconds();
    }

    /**
     * @return This length of time minus the given length of time
     */
    default Duration minus(LengthOfTime<?> that)
    {
        return newDuration(nanoseconds().minus(that.nanoseconds()));
    }

    /**
     * @return This length of time modulus the given length of time
     */
    default Duration modulo(Duration that)
    {
        return newDuration(nanoseconds().modulo(that.nanoseconds()));
    }

    /**
     * @return The nearest duration of the given unit
     */
    Duration nearest(Duration unit);

    /**
     * @return A new instance of the class implementing this interface
     */
    Duration newDuration(Nanoseconds nanoseconds);

    /**
     * @return This length of time plus the given length of time
     */
    default Duration plus(LengthOfTime<?> that)
    {
        return newDuration(nanoseconds().plus(that.nanoseconds()));
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
                onTimer.call(timer);
            }
        }, 0L, milliseconds());
    }

    /**
     * @return This duration rounded down to the closest unit
     */
    default Duration roundDown(Duration unit)
    {
        return newDuration(nanoseconds().roundDown(unit.nanoseconds()));
    }

    /**
     * @return This duration rounded up to the closest unit
     */
    default Duration roundUp(Duration unit)
    {
        return newDuration(nanoseconds().roundUp(unit.nanoseconds()));
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

    /**
     * @return This length of time multiplied by the given factor
     */
    default Duration times(double factor)
    {
        return newDuration(nanoseconds().times(factor));
    }

    /**
     * @return This length of time multiplied by the given factor
     */
    default Duration times(int factor)
    {
        return times((double) factor);
    }

    /**
     * Wait for this duration on the given monitor. Note that a duration of NONE is considered to be a wait time of zero
     * milliseconds, whereas the underlying Java {@link Object#wait(long)} considers zero milliseconds to be infinite
     * wait time.
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

    /**
     * Waits for this length of time, then calls the callback method
     */
    default void waitThen(Callback<Timer> onTimer)
    {
        var timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onTimer.call(timer);
            }
        }, milliseconds());
    }
}
