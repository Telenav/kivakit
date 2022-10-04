package com.telenav.kivakit.core.messaging.listeners;

import com.telenav.kivakit.annotations.code.ApiQuality;
import com.telenav.kivakit.core.messaging.Broadcaster;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.messaging.Message;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.time.Duration;
import com.telenav.kivakit.core.time.Frequency;
import com.telenav.kivakit.core.time.Rate;
import com.telenav.kivakit.core.time.RateCalculator;
import com.telenav.kivakit.core.time.Time;

import static com.telenav.kivakit.annotations.code.ApiStability.API_STABLE_EXTENSIBLE;
import static com.telenav.kivakit.annotations.code.DocumentationQuality.DOCUMENTATION_COMPLETE;
import static com.telenav.kivakit.annotations.code.TestingQuality.TESTING_NONE;
import static com.telenav.kivakit.core.time.Duration.ONE_MINUTE;

/**
 * Base class for implementing message alarms that trigger when alarming messages are received at a high rate.
 *
 * <p><b>Installing a Message Alarm</b></p>
 *
 * <p>
 * A {@link MessageAlarm} is a {@link Listener} which can listen to messages from any {@link Broadcaster} or
 * {@link Repeater} when installed with {@link MessageAlarm#listenTo(Broadcaster)}. For example:
 * </p>
 *
 * <pre>
 * public void onRun()
 * {
 *     // Send alarm email if this application exceeds 10 errors per minute
 *     new EmailAlarm(...).listenTo(this);
 * }</pre>
 *
 * <p>
 * The installed alarm will be triggered when the error {@link #rate()} exceeds {@link #triggerRate()}, which defaults
 * to 10 errors per minute. This default value can be overridden with {@link #triggerRate(Rate)}. The maximum alarm
 * trigger frequency defaults to once every 30 minutes. This value can be overridden with
 * {@link #maximumTriggerFrequency(Frequency)}.
 * </p>
 *
 * <p><b>Implementing an Alarm</b></p>
 *
 * <p>
 * To implement an alarm, override {@link #onTrigger(Rate)} and implement the alarm action. By default, the alarm will
 * be triggered when {@link #shouldTrigger()} returns true. By default, {@link #shouldTrigger()} returns true if the
 * current error {@link #rate()} exceeds {@link #triggerRate()}. The current error {@link #rate()} is computed with a
 * {@link RateCalculator} that automatically resets once a minute. MessageTransceiver are categorized as errors (or not)
 * by {@link #isAlarming(Message)}, which returns true if the message status is worse-than-or-equal-to {@link Problem}
 * by default.
 * </p>
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
@ApiQuality(stability = API_STABLE_EXTENSIBLE,
            testing = TESTING_NONE,
            documentation = DOCUMENTATION_COMPLETE)
public abstract class MessageAlarm implements Listener
{
    /** Computes the rate of errors received */
    private final RateCalculator errorRate = new RateCalculator(ONE_MINUTE);

    /** The maximum frequency at which this alarm can be triggered */
    private Frequency maximumTriggerFrequency = Frequency.every(Duration.minutes(30));

    /** The time at which this alarm can next be triggered */
    private Time nextAllowedTriggerTime;

    /** Tracks frequency cycles */
    private Frequency.Cycle triggerCycle;

    /** The maximum rate above which an alarm is triggered */
    private Rate triggerRate = Rate.perMinute(10);

    /**
     * Sets the maximum frequency at which this alarm can be triggered
     */
    public MessageAlarm maximumTriggerFrequency(Frequency maximumTriggerFrequency)
    {
        this.maximumTriggerFrequency = maximumTriggerFrequency;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final synchronized void onMessage(Message message)
    {
        // If the message is an error,
        if (isAlarming(message))
        {
            // increment the number of errors,
            errorRate.increment();

            // and if we should trigger the alarm, and it is allowed,
            if (isTriggerAllowedNow() && shouldTrigger())
            {
                // then do that,
                trigger(rate());

                // and reset the error rate calculator.
                errorRate.reset();
            }
        }
    }

    /**
     * Called to determine if this alarm should be triggered
     *
     * @return True if this alarm should be triggered
     */
    public boolean shouldTrigger()
    {
        return rate().isFasterThan(triggerRate);
    }

    /**
     * {@inheritDoc}
     */
    public final void trigger(Rate rate)
    {
        onTrigger(rate);
    }

    /**
     * Sets the error rate above which this alarm will be triggered
     *
     * @param triggerRate The rate of error messages at which this alarm will be triggered
     */
    public MessageAlarm triggerRate(Rate triggerRate)
    {
        this.triggerRate = triggerRate;
        return this;
    }

    /**
     * @return The rate above which this alarm will be triggered
     */
    public Rate triggerRate()
    {
        return triggerRate;
    }

    /**
     * Returns true if the given message should count towards setting off this alarm
     *
     * @param message The message
     * @return True if the message is a problem that should be counted
     */
    protected boolean isAlarming(Message message)
    {
        return message.isWorseThanOrEqualTo(Problem.class);
    }

    /**
     * Alarm action implementation
     *
     * @param rate The error rate that triggered this alarm
     */
    protected abstract void onTrigger(Rate rate);

    /**
     * @return The current rate of error messages
     */
    protected Rate rate()
    {
        return errorRate.rate();
    }

    /**
     * @return True if it's allowed to trigger the alarm at this time
     */
    private boolean isTriggerAllowedNow()
    {
        // If we haven't triggered yet,
        if (nextAllowedTriggerTime == null)
        {
            // get the next trigger time
            nextAllowedTriggerTime = nextAllowedTriggerTime();

            // and allow this first trigger.
            return true;
        }
        else
        {
            // If we have passed the next allowed trigger time,
            var allowed = Time.now().isGreaterThan(nextAllowedTriggerTime);
            if (allowed)
            {
                // set a new next allowed trigger time
                nextAllowedTriggerTime = nextAllowedTriggerTime();
            }

            // and return whether triggering is allowed.
            return allowed;
        }
    }

    /**
     * @return The time at which this alarm can next be triggered
     */
    private Time nextAllowedTriggerTime()
    {
        // If we haven't initialized,
        if (triggerCycle == null)
        {
            // create a frequency cycle object.
            triggerCycle = maximumTriggerFrequency.start();
        }

        // Return the next allowed trigger time
        return triggerCycle.next();
    }
}
